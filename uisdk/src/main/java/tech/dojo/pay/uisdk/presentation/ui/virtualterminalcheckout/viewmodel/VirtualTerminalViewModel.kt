package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoVirtualTerminalHandler
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.GetSupportedCountriesUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.AllowedPaymentMethodsViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.SupportedCountriesViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.mapper.FullCardPaymentPayloadMapper
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.BillingAddressViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.CardDetailsViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.CheckBoxItem
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.InputFieldState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.InputFieldType
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.PayButtonViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.PaymentDetailsViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.ShippingAddressViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.VirtualTerminalViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.validator.VirtualTerminalValidator
import java.util.Currency

internal class VirtualTerminalViewModel(
    private val observePaymentIntent: ObservePaymentIntent,
    private val observePaymentStatus: ObservePaymentStatus,
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase,
    private val getSupportedCountriesUseCase: GetSupportedCountriesUseCase,
    private val supportedCountriesViewEntityMapper: SupportedCountriesViewEntityMapper,
    private val allowedPaymentMethodsViewEntityMapper: AllowedPaymentMethodsViewEntityMapper,
    private val virtualTerminalValidator: VirtualTerminalValidator,
    private var virtualTerminalHandler: DojoVirtualTerminalHandler,
    private val fullCardPaymentPayloadMapper: FullCardPaymentPayloadMapper
) : ViewModel() {
    private lateinit var paymentToken: String
    private var currentState: VirtualTerminalViewState
    private val mutableState = MutableLiveData<VirtualTerminalViewState>()
    val state: LiveData<VirtualTerminalViewState>
        get() = mutableState

    init {
        currentState = VirtualTerminalViewState(isLoading = true)
        pushStateToUi(currentState)
        viewModelScope.launch { observePaymentIntent() }
    }

    private suspend fun observePaymentIntent() {
        observePaymentIntent.observePaymentIntent().collect { it?.let { handlePaymentIntent(it) } }
    }

    private fun handlePaymentIntent(paymentIntentResult: PaymentIntentResult) {
        if (paymentIntentResult is PaymentIntentResult.Success) {
            viewModelScope.launch { observePaymentStatus() }
            paymentToken = paymentIntentResult.result.paymentToken
            currentState = currentState.copy(
                isLoading = false,
                paymentDetailsSection = getPaymentDetailsSectionWithPaymentIntent(
                    paymentIntentResult
                ),
                shippingAddressSection = getShippingAddressSectionWithPaymentIntent(
                    paymentIntentResult
                ),
                billingAddressSection = getBillingAddressSectionWithPaymentIntent(
                    paymentIntentResult
                ),
                cardDetailsSection = getCardDetailsSectionWithPaymentIntent(paymentIntentResult),
                payButtonSection = PayButtonViewState()
            )
            pushStateToUi(currentState)
        }
    }
    private suspend fun observePaymentStatus() {
        observePaymentStatus.observePaymentStates().collect {
            if (!it) {
                currentState = currentState.copy(
                    payButtonSection = PayButtonViewState(
                        isEnabled = virtualTerminalValidator.isAllDataValid(currentState),
                        isLoading = false
                    )
                )
                pushStateToUi(currentState)
            }
        }
    }

    private fun getPaymentDetailsSectionWithPaymentIntent(paymentIntentResult: PaymentIntentResult.Success) =
        PaymentDetailsViewState(
            totalAmount = paymentIntentResult.result.amount.valueString,
            amountCurrency = Currency.getInstance(paymentIntentResult.result.amount.currencyCode).symbol,
            merchantName = paymentIntentResult.result.merchantName,
            orderId = paymentIntentResult.result.orderId
        )

    private fun getShippingAddressSectionWithPaymentIntent(paymentIntentResult: PaymentIntentResult.Success) =
        ShippingAddressViewState(
            isVisible = paymentIntentResult.result.collectionShippingAddressRequired,
            supportedCountriesList = getSupportedCountriesList(paymentIntentResult.result.collectionBillingAddressRequired),
            currentSelectedCountry = SupportedCountriesViewEntity("", "", true),
        )

    private fun getBillingAddressSectionWithPaymentIntent(paymentIntentResult: PaymentIntentResult.Success): BillingAddressViewState {
        val isSectionVisible = if (paymentIntentResult.result.collectionShippingAddressRequired && paymentIntentResult.result.collectionBillingAddressRequired) {
            false
        } else paymentIntentResult.result.collectionBillingAddressRequired
        return BillingAddressViewState(
            isVisible = isSectionVisible,
            supportedCountriesList = getSupportedCountriesList(paymentIntentResult.result.collectionBillingAddressRequired),
            currentSelectedCountry = SupportedCountriesViewEntity("", "", false),
        )
    }

    private fun getCardDetailsSectionWithPaymentIntent(paymentIntentResult: PaymentIntentResult.Success) =
        CardDetailsViewState(
            isVisible = true,
            emailInputField = InputFieldState(
                value = "", isVisible = paymentIntentResult.result.collectionEmailRequired
            ),
            cardHolderInputField = InputFieldState(value = ""),
            cardNumberInputField = InputFieldState(value = ""),
            cardExpireDateInputField = InputFieldState(value = ""),
            cvvInputFieldState = InputFieldState(value = ""),
            allowedPaymentMethodsIcons = allowedPaymentMethodsViewEntityMapper.apply(
                paymentIntentResult.result.supportedCardsSchemes
            )
        )

    private fun getSupportedCountriesList(collectionBillingAddressRequired: Boolean): List<SupportedCountriesViewEntity> {
        return if (collectionBillingAddressRequired) getSupportedCountriesUseCase.getSupportedCountries()
            .map { supportedCountriesViewEntityMapper.apply(it) } else emptyList()
    }

    fun onNameFieldChanged(newValue: String) {
        currentState.shippingAddressSection?.let {
            currentState = currentState.copy(
                shippingAddressSection = currentState
                    .shippingAddressSection?.updateAddressName(InputFieldState(newValue))
            )
            updatePayButtonState()
            pushStateToUi(currentState)
        }
    }

    fun onValidateShippingNameField(finalValue: String) {
        currentState.shippingAddressSection?.let {
            currentState = currentState.copy(
                shippingAddressSection = currentState
                    .shippingAddressSection?.updateAddressName(
                        virtualTerminalValidator.validateInputFieldIsNotEmpty(
                            finalValue, InputFieldType.NAME
                        )
                    )
            )
            pushStateToUi(currentState)
        }
    }

    fun onAddress1FieldChanged(newValue: String, isShipping: Boolean) {
        if (isShipping) {
            currentState.shippingAddressSection?.let {
                currentState = currentState.copy(
                    shippingAddressSection = currentState
                        .shippingAddressSection?.updateAddressLine1(InputFieldState(newValue))
                )
                updatePayButtonState()
                pushStateToUi(currentState)
            }
        } else {
            currentState.billingAddressSection?.let {
                currentState = currentState.copy(
                    billingAddressSection = currentState
                        .billingAddressSection?.updateAddressLine1(InputFieldState(newValue))
                )
                updatePayButtonState()
                pushStateToUi(currentState)
            }
        }
    }

    fun onValidateAddress1Field(finalValue: String, isShipping: Boolean) {
        if (isShipping) {
            currentState.shippingAddressSection?.let {
                currentState = currentState.copy(
                    shippingAddressSection = currentState.shippingAddressSection?.updateAddressLine1(
                        virtualTerminalValidator.validateInputFieldIsNotEmpty(
                            finalValue, InputFieldType.ADDRESS1
                        )
                    )
                )
                pushStateToUi(currentState)
            }
        } else {
            currentState.billingAddressSection?.let {
                currentState = currentState.copy(
                    billingAddressSection = currentState.billingAddressSection?.updateAddressLine1(
                        virtualTerminalValidator.validateInputFieldIsNotEmpty(
                            finalValue, InputFieldType.ADDRESS1
                        )
                    )
                )
                pushStateToUi(currentState)
            }
        }
    }

    fun onAddress2FieldChanged(newValue: String, isShipping: Boolean) {
        if (isShipping) {
            currentState.shippingAddressSection?.let {
                currentState = currentState.copy(
                    shippingAddressSection = currentState
                        .shippingAddressSection?.updateAddressLine2(InputFieldState(newValue))
                )
                pushStateToUi(currentState)
            }
        } else {
            currentState.billingAddressSection?.let {
                currentState = currentState.copy(
                    billingAddressSection = currentState
                        .billingAddressSection?.updateAddressLine2(InputFieldState(newValue))
                )
                pushStateToUi(currentState)
            }
        }
    }

    fun onCityFieldChanged(newValue: String, isShipping: Boolean) {
        if (isShipping) {
            currentState.shippingAddressSection?.let {
                currentState = currentState.copy(
                    shippingAddressSection = currentState
                        .shippingAddressSection?.updateCity(InputFieldState(newValue))
                )
                updatePayButtonState()
                pushStateToUi(currentState)
            }
        } else {
            currentState.billingAddressSection?.let {
                currentState = currentState.copy(
                    billingAddressSection = currentState
                        .billingAddressSection?.updateCity(InputFieldState(newValue))
                )
                updatePayButtonState()
                pushStateToUi(currentState)
            }
        }
    }

    fun onValidateCityField(finalValue: String, isShipping: Boolean) {
        if (isShipping) {
            currentState.shippingAddressSection?.let {
                currentState = currentState.copy(
                    shippingAddressSection = currentState.shippingAddressSection?.updateCity(
                        virtualTerminalValidator.validateInputFieldIsNotEmpty(
                            finalValue, InputFieldType.CITY
                        )
                    )
                )
                pushStateToUi(currentState)
            }
        } else {
            currentState.billingAddressSection?.let {
                currentState = currentState.copy(
                    billingAddressSection = currentState.billingAddressSection?.updateCity(
                        virtualTerminalValidator.validateInputFieldIsNotEmpty(
                            finalValue, InputFieldType.CITY
                        )
                    )
                )
                pushStateToUi(currentState)
            }
        }
    }

    fun onSPostalCodeFieldChanged(newValue: String, isShipping: Boolean) {
        if (isShipping) {
            currentState.shippingAddressSection?.let {
                currentState = currentState.copy(
                    shippingAddressSection = currentState
                        .shippingAddressSection?.updatePostalCode(InputFieldState(newValue))
                )
                updatePayButtonState()
                pushStateToUi(currentState)
            }
        } else {
            currentState.billingAddressSection?.let {
                currentState = currentState.copy(
                    billingAddressSection = currentState
                        .billingAddressSection?.updatePostalCode(InputFieldState(newValue))
                )
                updatePayButtonState()
                pushStateToUi(currentState)
            }
        }
    }

    fun onValidatePostalCodeField(finalValue: String, isShipping: Boolean) {
        if (isShipping) {
            currentState.shippingAddressSection?.let {
                currentState = currentState.copy(
                    shippingAddressSection = currentState.shippingAddressSection?.updatePostalCode(
                        virtualTerminalValidator.validateInputFieldIsNotEmpty(
                            finalValue, InputFieldType.POSTAL_CODE
                        )
                    )
                )
                pushStateToUi(currentState)
            }
        } else {
            currentState.billingAddressSection?.let {
                currentState = currentState.copy(
                    billingAddressSection = currentState.billingAddressSection?.updatePostalCode(
                        virtualTerminalValidator.validateInputFieldIsNotEmpty(
                            finalValue, InputFieldType.POSTAL_CODE
                        )
                    )
                )
                pushStateToUi(currentState)
            }
        }
    }

    fun onCountrySelected(newValue: SupportedCountriesViewEntity, isShipping: Boolean) {
        if (isShipping) {
            currentState.shippingAddressSection?.let {
                currentState = currentState.copy(
                    shippingAddressSection = currentState.shippingAddressSection?.updateCurrentSelectedCountry(
                        SupportedCountriesViewEntity(
                            countryCode = newValue.countryCode,
                            countryName = newValue.countryName,
                            isPostalCodeEnabled = newValue.isPostalCodeEnabled
                        )
                    )
                )
                pushStateToUi(currentState)
            }
        } else {
            currentState.billingAddressSection?.let {
                currentState = currentState.copy(
                    billingAddressSection = currentState.billingAddressSection?.updateCurrentSelectedCountry(
                        SupportedCountriesViewEntity(
                            countryCode = newValue.countryCode,
                            countryName = newValue.countryName,
                            isPostalCodeEnabled = newValue.isPostalCodeEnabled
                        )
                    )
                )
                pushStateToUi(currentState)
            }
        }
    }

    fun onDeliveryNotesFieldChanged(newValue: String) {
        currentState.shippingAddressSection?.let {
            currentState = currentState.copy(
                shippingAddressSection = currentState
                    .shippingAddressSection?.updateDeliveryNotes(InputFieldState(newValue))
            )
            pushStateToUi(currentState)
        }
    }
    fun onShippingSameAsBillingChecked(isChecked: Boolean) {
        currentState.shippingAddressSection?.let {
            currentState = currentState.copy(
                shippingAddressSection = currentState.shippingAddressSection?.updateIsShippingSameAsBillingCheckBox(
                    CheckBoxItem(
                        messageText = R.string.dojo_ui_sdk_card_details_checkout_save_card,
                        isChecked = isChecked,
                        isVisible = true
                    )
                ),
                billingAddressSection = currentState.billingAddressSection?.updateIsVisible(!isChecked)
            )
            pushStateToUi(currentState)
        }
    }

    fun onCardHolderChanged(newValue: String) {
        currentState.cardDetailsSection?.let {
            currentState = currentState.copy(
                cardDetailsSection = currentState
                    .cardDetailsSection?.updateCardHolderInputField(InputFieldState(newValue))
            )
            updatePayButtonState()
            pushStateToUi(currentState)
        }
    }

    fun onValidateCardHolder(finalValue: String) {
        currentState.cardDetailsSection?.let {
            currentState = currentState.copy(
                cardDetailsSection = currentState
                    .cardDetailsSection?.updateCardHolderInputField(
                        virtualTerminalValidator.validateInputFieldIsNotEmpty(
                            finalValue, InputFieldType.CARD_HOLDER_NAME
                        )
                    )
            )
            pushStateToUi(currentState)
        }
    }

    fun onCardNumberChanged(newValue: String) {
        currentState.cardDetailsSection?.let {
            currentState = currentState.copy(
                cardDetailsSection = currentState
                    .cardDetailsSection?.updateCardNumberInputField(InputFieldState(newValue))
            )
            updatePayButtonState()
            pushStateToUi(currentState)
        }
    }

    fun onValidateCardNumber(finalValue: String) {
        currentState.cardDetailsSection?.let {
            currentState = currentState.copy(
                cardDetailsSection = currentState
                    .cardDetailsSection?.updateCardNumberInputField(
                        virtualTerminalValidator.validateCardNumberInputField(finalValue)
                    )
            )
            pushStateToUi(currentState)
        }
    }

    fun onCardCvvChanged(newValue: String) {
        currentState.cardDetailsSection?.let {
            currentState = currentState.copy(
                cardDetailsSection = currentState
                    .cardDetailsSection?.updateCvvInputFieldState(InputFieldState(newValue))
            )
            updatePayButtonState()
            pushStateToUi(currentState)
        }
    }

    fun onValidateCvv(finalValue: String) {
        currentState.cardDetailsSection?.let {
            currentState = currentState.copy(
                cardDetailsSection = currentState
                    .cardDetailsSection?.updateCvvInputFieldState(
                        virtualTerminalValidator.validateCVVInputField(finalValue)
                    )
            )
            pushStateToUi(currentState)
        }
    }

    fun onCardDateChanged(newValue: String) {
        currentState.cardDetailsSection?.let {
            currentState = currentState.copy(
                cardDetailsSection = currentState
                    .cardDetailsSection?.updateCardExpireDateInputField(InputFieldState(newValue))
            )
            updatePayButtonState()
            pushStateToUi(currentState)
        }
    }

    fun onValidateCardDate(finalValue: String) {
        currentState.cardDetailsSection?.let {
            currentState = currentState.copy(
                cardDetailsSection = currentState
                    .cardDetailsSection?.updateCardExpireDateInputField(
                        virtualTerminalValidator.validateExpireDateInputField(finalValue)
                    )
            )
            pushStateToUi(currentState)
        }
    }
    fun onEmailChanged(newValue: String) {
        currentState.cardDetailsSection?.let {
            currentState = currentState.copy(
                cardDetailsSection = currentState
                    .cardDetailsSection?.updateEmailInputField(InputFieldState(newValue))
            )
            updatePayButtonState()
            pushStateToUi(currentState)
        }
    }

    fun onValidateEmail(finalValue: String) {
        currentState.cardDetailsSection?.let {
            currentState = currentState.copy(
                cardDetailsSection = currentState
                    .cardDetailsSection?.updateEmailInputField(
                        virtualTerminalValidator.validateEmailInputField(finalValue)
                    )
            )
            pushStateToUi(currentState)
        }
    }

    private fun updatePayButtonState() {
        currentState = currentState.copy(
            payButtonSection = PayButtonViewState(
                isEnabled = virtualTerminalValidator.isAllDataValid(currentState)
            )
        )
    }

    fun onPayClicked() {
        currentState = currentState.copy(
            payButtonSection = PayButtonViewState(
                isEnabled = virtualTerminalValidator.isAllDataValid(currentState),
                isLoading = true
            )
        )
        updatePaymentStateUseCase.updatePaymentSate(isActive = true)
        virtualTerminalHandler.executeVirtualTerminalPayment(
            paymentToken,
            fullCardPaymentPayloadMapper.apply(currentState)
        )
        pushStateToUi(currentState)
    }

    private fun pushStateToUi(state: VirtualTerminalViewState) {
        mutableState.postValue(state)
    }
}
