package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.card.entities.DojoAddressDetails
import tech.dojo.pay.sdk.card.entities.DojoCardDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandler
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.GetSupportedCountriesUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.AllowedPaymentMethodsViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.SupportedCountriesViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.ActionButtonState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsCheckoutState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.InputFieldState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.validator.CardCheckoutScreenValidator
import java.util.Currency

internal class CardDetailsCheckoutViewModel(
    private val observePaymentIntent: ObservePaymentIntent,
    private var dojoCardPaymentHandler: DojoCardPaymentHandler,
    private val observePaymentStatus: ObservePaymentStatus,
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase,
    private val getSupportedCountriesUseCase: GetSupportedCountriesUseCase,
    private val supportedCountriesViewEntityMapper: SupportedCountriesViewEntityMapper,
    private val allowedPaymentMethodsViewEntityMapper: AllowedPaymentMethodsViewEntityMapper,
    private val cardCheckoutScreenValidator: CardCheckoutScreenValidator,
) : ViewModel() {
    private lateinit var paymentToken: String
    private var isVirtualTerminal = false
    private var currentState: CardDetailsCheckoutState
    private val mutableState = MutableLiveData<CardDetailsCheckoutState>()
    val state: LiveData<CardDetailsCheckoutState>
        get() = mutableState

    fun updateCardPaymentHandler(newDojoCardPaymentHandler: DojoCardPaymentHandler) {
        dojoCardPaymentHandler = newDojoCardPaymentHandler
    }

    init {
        currentState = CardDetailsCheckoutState()
        pushStateToUi(currentState)
        viewModelScope.launch { observePaymentIntent() }
        viewModelScope.launch { observePaymentStatus() }
    }

    fun onCardHolderValueChanged(newValue: String) {
        currentState = if (newValue.isBlank()) {
            currentState.copy(
                cardHolderInputField = InputFieldState(
                    value = "",
                    isError = true,
                    errorMessages = R.string.dojo_ui_sdk_card_details_checkout_error_empty_card_holder,
                ),
                actionButtonState = ActionButtonState(isEnabled = false),
            )
        } else {
            currentState.copy(
                cardHolderInputField = InputFieldState(value = newValue),
                actionButtonState = ActionButtonState(isEnabled = isPayButtonEnabled(cardHolderValue = newValue)),
            )
        }

        pushStateToUi(currentState)
    }

    fun onPostalCodeValueChanged(newValue: String) {
        currentState = if (newValue.isBlank()) {
            currentState.copy(
                postalCodeField = InputFieldState(
                    value = "",
                    errorMessages = R.string.dojo_ui_sdk_card_details_checkout_error_empty_billing_postcode,
                    isError = true,
                ),
                actionButtonState = ActionButtonState(isEnabled = false),
            )
        } else {
            currentState.copy(
                postalCodeField = InputFieldState(value = newValue),
                actionButtonState = ActionButtonState(isEnabled = isPayButtonEnabled(postalCodeValue = newValue)),
            )
        }
        pushStateToUi(currentState)
    }

    fun onCardNumberValueChanged(newValue: String) {
        currentState = currentState.copy(
            cardNumberInputField = InputFieldState(value = newValue),
            actionButtonState = ActionButtonState(isEnabled = isPayButtonEnabled(cardNumberValue = newValue)),
        )
        pushStateToUi(currentState)
    }

    fun validateCardNumber(cardNumberValue: String) {
        if (cardNumberValue.isBlank()) {
            currentState = currentState.copy(
                cardNumberInputField = InputFieldState(
                    value = "",
                    isError = true,
                    errorMessages = R.string.dojo_ui_sdk_card_details_checkout_error_empty_card_number,
                ),
                actionButtonState = ActionButtonState(isEnabled = false),
            )
        } else if (!cardCheckoutScreenValidator.isCardNumberValid(cardNumberValue)) {
            currentState = currentState.copy(
                cardNumberInputField = InputFieldState(
                    value = cardNumberValue,
                    isError = true,
                    errorMessages = R.string.dojo_ui_sdk_card_details_checkout_error_invalid_card_number,
                ),
                actionButtonState = ActionButtonState(isEnabled = false),
            )
        }
        pushStateToUi(currentState)
    }

    fun onCvvValueChanged(newValue: String) {
        currentState = if (newValue.isBlank()) {
            currentState.copy(
                cvvInputFieldState = InputFieldState(
                    value = "",
                    isError = true,
                    errorMessages = R.string.dojo_ui_sdk_card_details_checkout_error_empty_cvv,
                ),
                actionButtonState = ActionButtonState(isEnabled = false),
            )
        } else {
            currentState.copy(
                cvvInputFieldState = InputFieldState(value = newValue),
                actionButtonState = ActionButtonState(isEnabled = isPayButtonEnabled(cvvValue = newValue)),
            )
        }

        pushStateToUi(currentState)
    }

    fun validateCvv(cvvValue: String, focused: Boolean) {
        if (!focused &&
            cvvValue.isNotBlank() &&
            !cardCheckoutScreenValidator.isCvvValid(cvvValue)
        ) {
            currentState = currentState.copy(
                cvvInputFieldState = InputFieldState(
                    value = cvvValue,
                    isError = true,
                    errorMessages = R.string.dojo_ui_sdk_card_details_checkout_error_invalid_cvv,
                ),
                actionButtonState = ActionButtonState(isEnabled = false),
            )
        }
        pushStateToUi(currentState)
    }

    fun onExpireDateValueChanged(newValue: String) {
        currentState = if (newValue.isBlank()) {
            currentState.copy(
                cardExpireDateInputField = InputFieldState(
                    value = "",
                    isError = true,
                    errorMessages = R.string.dojo_ui_sdk_card_details_checkout_error_empty_expiry,
                ),
                actionButtonState = ActionButtonState(isEnabled = false),
            )
        } else {
            currentState.copy(
                cardExpireDateInputField = InputFieldState(newValue),
                actionButtonState = ActionButtonState(isEnabled = isPayButtonEnabled(cardExpireDate = newValue)),
            )
        }
        pushStateToUi(currentState)
    }

    fun validateExpireDate(expireDateValue: String, focused: Boolean) {
        if (!focused &&
            expireDateValue.isNotBlank() &&
            !cardCheckoutScreenValidator.isCardExpireDateValid(expireDateValue)
        ) {
            currentState = currentState.copy(
                cardExpireDateInputField = InputFieldState(
                    value = expireDateValue,
                    isError = true,
                    errorMessages = R.string.dojo_ui_sdk_card_details_checkout_error_invalid_expiry,
                ),
                actionButtonState = ActionButtonState(isEnabled = false),
            )
        }
        pushStateToUi(currentState)
    }

    fun onEmailValueChanged(newValue: String) {
        currentState = if (newValue.isBlank()) {
            currentState.copy(
                emailInputField = InputFieldState(
                    value = newValue,
                    isError = true,
                    errorMessages = R.string.dojo_ui_sdk_card_details_checkout_error_empty_email,
                ),
                actionButtonState = ActionButtonState(isEnabled = false),
            )
        } else {
            currentState.copy(
                emailInputField = InputFieldState(value = newValue),
                actionButtonState = ActionButtonState(isEnabled = isPayButtonEnabled(emailValue = newValue)),
            )
        }
        pushStateToUi(currentState)
    }

    fun validateEmailValue(emailValue: String, focused: Boolean) {
        if (!focused &&
            emailValue.isNotBlank() &&
            !cardCheckoutScreenValidator.isEmailValid(emailValue)
        ) {
            currentState = currentState.copy(
                emailInputField = InputFieldState(
                    value = emailValue,
                    isError = true,
                    errorMessages = R.string.dojo_ui_sdk_card_details_checkout_error_invalid_email,
                ),
                actionButtonState = ActionButtonState(isEnabled = false),
            )
        }
        pushStateToUi(currentState)
    }

    fun onCountrySelected(newValue: SupportedCountriesViewEntity) {
        currentState = currentState.copy(
            currentSelectedCountry = newValue,
            isPostalCodeFieldRequired = applyIsPostalCodeFieldRequiredLogic(
                newValue,
                currentState.isBillingCountryFieldRequired,
            ),
        )
        pushStateToUi(currentState)
    }

    private fun isPayButtonEnabled(
        cardHolderValue: String = currentState.cardHolderInputField.value,
        cardNumberValue: String = currentState.cardNumberInputField.value,
        cvvValue: String = currentState.cvvInputFieldState.value,
        cardExpireDate: String = currentState.cardExpireDateInputField.value,
        emailValue: String = currentState.emailInputField.value,
        postalCodeValue: String = currentState.postalCodeField.value,
    ) =
        cardHolderValue.isNotBlank() &&
            cardCheckoutScreenValidator.isCardNumberValid(cardNumberValue) &&
            cardCheckoutScreenValidator.isCvvValid(cvvValue) &&
            cardCheckoutScreenValidator.isCardExpireDateValid(cardExpireDate) &&
            cardCheckoutScreenValidator.isEmailFieldValidWithInputFieldVisibility(
                emailValue,
                currentState.isEmailInputFieldRequired,
            ) &&
            cardCheckoutScreenValidator.isPostalCodeFieldWithInputFieldVisibility(
                postalCodeValue,
                currentState.isPostalCodeFieldRequired,
            )

    private suspend fun observePaymentIntent() {
        observePaymentIntent.observePaymentIntent().collect { it?.let { handlePaymentIntent(it) } }
    }

    private fun handlePaymentIntent(paymentIntentResult: PaymentIntentResult) {
        if (paymentIntentResult is PaymentIntentResult.Success) {
            val countryList =
                getSupportedCountriesList(paymentIntentResult.result.collectionBillingAddressRequired)
            val currentSelectedCountry = if (countryList.isNotEmpty()) {
                getSupportedCountriesList(paymentIntentResult.result.collectionBillingAddressRequired)[0]
            } else {
                SupportedCountriesViewEntity("", "", true)
            }
            paymentToken = paymentIntentResult.result.paymentToken
            isVirtualTerminal = paymentIntentResult.result.isVirtualTerminalPayment
            currentState = currentState.copy(
                totalAmount = paymentIntentResult.result.amount.valueString,
                amountCurrency = Currency.getInstance(paymentIntentResult.result.amount.currencyCode).symbol,
                saveCardCheckBox = currentState.saveCardCheckBox.copy(
                    isVisible = !paymentIntentResult.result.customerId.isNullOrBlank(),
                    isChecked = !paymentIntentResult.result.customerId.isNullOrBlank(),
                ),
                allowedPaymentMethodsIcons = allowedPaymentMethodsViewEntityMapper.apply(
                    paymentIntentResult.result.supportedCardsSchemes,
                ),
                isEmailInputFieldRequired = paymentIntentResult.result.collectionEmailRequired,
                isBillingCountryFieldRequired = paymentIntentResult.result.collectionBillingAddressRequired,
                supportedCountriesList = countryList,
                currentSelectedCountry = currentSelectedCountry,
                isPostalCodeFieldRequired = paymentIntentResult.result.collectionBillingAddressRequired,
            )
            pushStateToUi(currentState)
        }
    }

    private fun getSupportedCountriesList(collectionBillingAddressRequired: Boolean): List<SupportedCountriesViewEntity> {
        return if (collectionBillingAddressRequired) {
            getSupportedCountriesUseCase
                .getSupportedCountries()
                .map { supportedCountriesViewEntityMapper.apply(it) }
        } else {
            emptyList()
        }
    }

    private fun applyIsPostalCodeFieldRequiredLogic(
        supportedCountryItem: SupportedCountriesViewEntity,
        collectionBillingAddressRequired: Boolean,
    ): Boolean {
        return supportedCountryItem.isPostalCodeEnabled && collectionBillingAddressRequired
    }

    private suspend fun observePaymentStatus() {
        observePaymentStatus.observePaymentStates().collect {
            if (!it) {
                pushStateToUi(
                    currentState.copy(
                        actionButtonState = ActionButtonState(isLoading = false),
                    ),
                )
            }
        }
    }

    fun onSaveCardChecked(isChecked: Boolean) {
        val newCheckBoxItem = currentState.saveCardCheckBox.copy(isChecked = isChecked)
        currentState = currentState.copy(saveCardCheckBox = newCheckBoxItem)
    }

    fun onPayWithCardClicked() {
        viewModelScope.launch { observePaymentIntent() }
        updatePaymentStateUseCase.updatePaymentSate(isActive = true)
        pushStateToUi(
            currentState.copy(
                actionButtonState = ActionButtonState(isLoading = true),
            ),
        )
        dojoCardPaymentHandler.executeCardPayment(paymentToken, getPaymentPayLoad())
    }

    private fun pushStateToUi(state: CardDetailsCheckoutState) {
        mutableState.postValue(state)
    }

    private fun getPaymentPayLoad(): DojoCardPaymentPayLoad.FullCardPaymentPayload =
        DojoCardPaymentPayLoad.FullCardPaymentPayload(
            userEmailAddress = if (currentState.isEmailInputFieldRequired) currentState.emailInputField.value else null,
            billingAddress = DojoAddressDetails(
                countryCode = if (currentState.isBillingCountryFieldRequired) currentState.currentSelectedCountry.countryCode else null,
                postcode = if (currentState.isPostalCodeFieldRequired) currentState.postalCodeField.value else null,
            ),
            savePaymentMethod = currentState.saveCardCheckBox.isChecked,
            cardDetails = DojoCardDetails(
                cardNumber = currentState.cardNumberInputField.value,
                cardName = currentState.cardHolderInputField.value,
                expiryMonth = getExpiryMonth(currentState.cardExpireDateInputField.value),
                expiryYear = getExpiryYear(currentState.cardExpireDateInputField.value),
                cv2 = currentState.cvvInputFieldState.value,
            ),
        )

    private fun getExpiryMonth(expireDateValueValue: String) =
        if (expireDateValueValue.isNotBlank()) {
            currentState.cardExpireDateInputField.value.substring(0, 2)
        } else {
            ""
        }

    private fun getExpiryYear(expireDateValueValue: String) =
        if (expireDateValueValue.isNotBlank() && expireDateValueValue.length > 2) {
            currentState.cardExpireDateInputField.value.substring(2, 4)
        } else {
            ""
        }
}
