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
    private var virtualTerminalHandler: DojoVirtualTerminalHandler
) : ViewModel() {
    private lateinit var paymentToken: String
    private var currentState: VirtualTerminalViewState
    private val mutableState = MutableLiveData<VirtualTerminalViewState>()
    val state: LiveData<VirtualTerminalViewState>
        get() = mutableState

    init {
        currentState = VirtualTerminalViewState(isLoading = true)
        viewModelScope.launch { observePaymentIntent() }
        pushStateToUi(currentState)
    }

    private suspend fun observePaymentIntent() {
        observePaymentIntent.observePaymentIntent().collect { it?.let { handlePaymentIntent(it) } }
    }

    private fun handlePaymentIntent(paymentIntentResult: PaymentIntentResult) {
        if (paymentIntentResult is PaymentIntentResult.Success) {
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

    private fun getPaymentDetailsSectionWithPaymentIntent(paymentIntentResult: PaymentIntentResult.Success) =
        PaymentDetailsViewState(
            totalAmount = paymentIntentResult.result.amount.valueString,
            amountCurrency = Currency.getInstance(paymentIntentResult.result.amount.currencyCode).symbol,
            merchantName = paymentIntentResult.result.merchantName
        )

    private fun getShippingAddressSectionWithPaymentIntent(paymentIntentResult: PaymentIntentResult.Success) =
        ShippingAddressViewState(
            isVisible = paymentIntentResult.result.collectionShippingAddressRequired,
            supportedCountriesList = getSupportedCountriesList(paymentIntentResult.result.collectionBillingAddressRequired),
            currentSelectedCountry = SupportedCountriesViewEntity("", "", true),
        )

    private fun getBillingAddressSectionWithPaymentIntent(paymentIntentResult: PaymentIntentResult.Success) =
        BillingAddressViewState(
            isVisible = paymentIntentResult.result.collectionBillingAddressRequired,
            supportedCountriesList = getSupportedCountriesList(paymentIntentResult.result.collectionBillingAddressRequired),
            currentSelectedCountry = SupportedCountriesViewEntity("", "", false),
        )

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
            currentState.shippingAddressSection?.name = InputFieldState(value = newValue)
            updatePayButtonState()
            pushStateToUi(currentState)
        }
    }

    fun onValidateShippingNameField(finalValue: String) {
        currentState.shippingAddressSection?.let {
            currentState.shippingAddressSection?.name =
                virtualTerminalValidator.validateInputFieldIsNotEmpty(
                    finalValue, InputFieldType.NAME
                )
            pushStateToUi(currentState)
        }
    }

    fun onAddress1FieldChanged(newValue: String, isShipping: Boolean) {
        if (isShipping) {
            currentState.shippingAddressSection?.let {
                currentState.shippingAddressSection?.addressLine1 =
                    InputFieldState(value = newValue)
                updatePayButtonState()
                pushStateToUi(currentState)
            }
        } else {
            currentState.billingAddressSection?.let {
                currentState.billingAddressSection?.addressLine1 = InputFieldState(value = newValue)
                updatePayButtonState()
                pushStateToUi(currentState)
            }
        }
    }

    fun onValidateAddress1Field(finalValue: String, isShipping: Boolean) {
        if (isShipping) {
            currentState.shippingAddressSection?.let {
                currentState.shippingAddressSection?.addressLine1 =
                    virtualTerminalValidator.validateInputFieldIsNotEmpty(
                        finalValue, InputFieldType.ADDRESS1
                    )
                pushStateToUi(currentState)
            }
        } else {
            currentState.billingAddressSection?.let {
                currentState.billingAddressSection?.addressLine1 =
                    virtualTerminalValidator.validateInputFieldIsNotEmpty(
                        finalValue, InputFieldType.ADDRESS1
                    )
                pushStateToUi(currentState)
            }
        }
    }

    fun onShippingAddress2FieldChanged(newValue: String, isShipping: Boolean) {
        if (isShipping) {
            currentState.shippingAddressSection?.let {
                currentState.shippingAddressSection?.addressLine2 =
                    InputFieldState(value = newValue)
                pushStateToUi(currentState)
            }
        } else {
            currentState.billingAddressSection?.let {
                currentState.billingAddressSection?.addressLine2 = InputFieldState(value = newValue)
                pushStateToUi(currentState)
            }
        }
    }

    fun onCityFieldChanged(newValue: String, isShipping: Boolean) {
        if (isShipping) {
            currentState.shippingAddressSection?.let {
                currentState.shippingAddressSection?.city = InputFieldState(value = newValue)
                updatePayButtonState()
                pushStateToUi(currentState)
            }
        } else {
            currentState.billingAddressSection?.let {
                currentState.billingAddressSection?.city = InputFieldState(value = newValue)
                updatePayButtonState()
                pushStateToUi(currentState)
            }
        }
    }

    fun onValidateCityField(finalValue: String, isShipping: Boolean) {
        if (isShipping) {
            currentState.shippingAddressSection?.let {
                currentState.shippingAddressSection?.city =
                    virtualTerminalValidator.validateInputFieldIsNotEmpty(
                        finalValue, InputFieldType.CITY
                    )
                pushStateToUi(currentState)
            }
        } else {
            currentState.billingAddressSection?.let {
                currentState.billingAddressSection?.city =
                    virtualTerminalValidator.validateInputFieldIsNotEmpty(
                        finalValue, InputFieldType.CITY
                    )
                pushStateToUi(currentState)
            }
        }
    }

    fun onSPostalCodeFieldChanged(newValue: String, isShipping: Boolean) {
        if (isShipping) {
            currentState.shippingAddressSection?.let {
                currentState.shippingAddressSection?.postalCode = InputFieldState(value = newValue)
                updatePayButtonState()
                pushStateToUi(currentState)
            }
        } else {
            currentState.billingAddressSection?.let {
                currentState.billingAddressSection?.postalCode = InputFieldState(value = newValue)
                updatePayButtonState()
                pushStateToUi(currentState)
            }
        }
    }

    fun onValidatePostalCodeField(finalValue: String, isShipping: Boolean) {
        if (isShipping) {
            currentState.shippingAddressSection?.let {
                currentState.shippingAddressSection?.postalCode =
                    virtualTerminalValidator.validateInputFieldIsNotEmpty(
                        finalValue, InputFieldType.POSTAL_CODE
                    )
                pushStateToUi(currentState)
            }
        } else {
            currentState.billingAddressSection?.let {
                currentState.billingAddressSection?.postalCode =
                    virtualTerminalValidator.validateInputFieldIsNotEmpty(
                        finalValue, InputFieldType.POSTAL_CODE
                    )
                pushStateToUi(currentState)
            }
        }
    }

    fun onCountrySelected(newValue: SupportedCountriesViewEntity, isShipping: Boolean) {
        if (isShipping) {
            currentState.shippingAddressSection?.let {
                currentState.shippingAddressSection?.currentSelectedCountry =
                    SupportedCountriesViewEntity(
                        countryCode = newValue.countryCode,
                        countryName = newValue.countryName,
                        isPostalCodeEnabled = newValue.isPostalCodeEnabled
                    )
                pushStateToUi(currentState)
            }
        } else {
            currentState.billingAddressSection?.let {
                currentState.billingAddressSection?.currentSelectedCountry =
                    SupportedCountriesViewEntity(
                        countryCode = newValue.countryCode,
                        countryName = newValue.countryName,
                        isPostalCodeEnabled = newValue.isPostalCodeEnabled
                    )
                pushStateToUi(currentState)
            }
        }
    }

    fun onShippingSameAsBillingChecked(isChecked: Boolean) {
        currentState.shippingAddressSection?.let {
            currentState.shippingAddressSection?.isShippingSameAsBillingCheckBox = CheckBoxItem(
                messageText = R.string.dojo_ui_sdk_card_details_checkout_save_card,
                isChecked = isChecked,
                isVisible = true
            )
            currentState.billingAddressSection?.isVisible = isChecked
            pushStateToUi(currentState)
        }
    }

    fun onCardHolderChanged(newValue: String) {
        currentState.cardDetailsSection?.let {
            currentState.cardDetailsSection?.cardHolderInputField =
                InputFieldState(value = newValue)
            updatePayButtonState()
            pushStateToUi(currentState)
        }
    }

    fun onValidateCardHolder(finalValue: String) {
        currentState.cardDetailsSection?.let {
            currentState.cardDetailsSection?.cardHolderInputField =
                virtualTerminalValidator.validateInputFieldIsNotEmpty(
                    finalValue, InputFieldType.CARD_HOLDER_NAME
                )
            pushStateToUi(currentState)
        }
    }

    fun onCardNumberChanged(newValue: String) {
        currentState.cardDetailsSection?.let {
            currentState.cardDetailsSection?.cardNumberInputField =
                InputFieldState(value = newValue)
            updatePayButtonState()
            pushStateToUi(currentState)
        }
    }

    fun onValidateCardNumber(finalValue: String) {
        currentState.cardDetailsSection?.let {
            currentState.cardDetailsSection?.cardHolderInputField =
                virtualTerminalValidator.validateCardNumberInputField(finalValue)
            pushStateToUi(currentState)
        }
    }

    fun onCardCvvChanged(newValue: String) {
        currentState.cardDetailsSection?.let {
            currentState.cardDetailsSection?.cvvInputFieldState = InputFieldState(value = newValue)
            updatePayButtonState()
            pushStateToUi(currentState)
        }
    }

    fun onValidateCvv(finalValue: String) {
        currentState.cardDetailsSection?.let {
            currentState.cardDetailsSection?.cvvInputFieldState =
                virtualTerminalValidator.validateCVVInputField(finalValue)
            pushStateToUi(currentState)
        }
    }

    fun onCardDateChanged(newValue: String) {
        currentState.cardDetailsSection?.let {
            currentState.cardDetailsSection?.cardExpireDateInputField = InputFieldState(value = newValue)
            updatePayButtonState()
            pushStateToUi(currentState)
        }
    }

    fun onValidateCardDate(finalValue: String) {
        currentState.cardDetailsSection?.let {
            currentState.cardDetailsSection?.cardExpireDateInputField =
                virtualTerminalValidator.validateExpireDateInputField(finalValue)
            pushStateToUi(currentState)
        }
    }
    fun onEmailChanged(newValue: String) {
        currentState.cardDetailsSection?.let {
            currentState.cardDetailsSection?.emailInputField = InputFieldState(value = newValue)
            updatePayButtonState()
            pushStateToUi(currentState)
        }
    }

    fun onValidateEmail(finalValue: String) {
        currentState.cardDetailsSection?.let {
            currentState.cardDetailsSection?.emailInputField =
                virtualTerminalValidator.validateEmailInputField(finalValue)
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

    private fun pushStateToUi(state: VirtualTerminalViewState) {
        mutableState.postValue(state)
    }
}
