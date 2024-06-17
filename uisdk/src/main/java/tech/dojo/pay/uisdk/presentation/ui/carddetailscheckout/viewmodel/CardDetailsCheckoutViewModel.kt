package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandler
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.core.StringProvider
import tech.dojo.pay.uisdk.domain.GetSupportedCountriesUseCase
import tech.dojo.pay.uisdk.domain.MakeCardPaymentUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.entities.MakeCardPaymentParams
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.AllowedPaymentMethodsViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.CardCheckOutFullCardPaymentPayloadMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.SupportedCountriesViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardCheckOutHeaderType
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsCheckoutState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CheckBoxItem
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.InputFieldState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.validator.CardCheckoutScreenValidator
import java.util.Currency
import java.util.Locale

internal class CardDetailsCheckoutViewModel(
    private val observePaymentIntent: ObservePaymentIntent,
    private var dojoCardPaymentHandler: DojoCardPaymentHandler,
    private val observePaymentStatus: ObservePaymentStatus,
    private val getSupportedCountriesUseCase: GetSupportedCountriesUseCase,
    private val supportedCountriesViewEntityMapper: SupportedCountriesViewEntityMapper,
    private val allowedPaymentMethodsViewEntityMapper: AllowedPaymentMethodsViewEntityMapper,
    private val cardCheckoutScreenValidator: CardCheckoutScreenValidator,
    private val fullCardPaymentPayloadMapper: CardCheckOutFullCardPaymentPayloadMapper,
    private val stringProvider: StringProvider,
    private val isStartDestination: Boolean,
    private val makeCardPaymentUseCase: MakeCardPaymentUseCase,
    private val navigateToCardResult: (dojoPaymentResult: DojoPaymentResult) -> Unit,
) : ViewModel() {
    private lateinit var paymentIntentId: String
    private var currentState: CardDetailsCheckoutState
    private val mutableState = MutableLiveData<CardDetailsCheckoutState>()
    val state: LiveData<CardDetailsCheckoutState>
        get() = mutableState

    fun updateCardPaymentHandler(newDojoCardPaymentHandler: DojoCardPaymentHandler) {
        dojoCardPaymentHandler = newDojoCardPaymentHandler
    }

    init {
        currentState = CardDetailsCheckoutState(
            toolbarTitle = getToolBarTitle(),
            isLoading = isStartDestination,
            checkBoxItem = CheckBoxItem(
                isVisible = false,
                isChecked = !isStartDestination,
                messageText = "",
            ),
        )
        pushStateToUi(currentState)
        viewModelScope.launch { observePaymentIntent() }
        viewModelScope.launch { observePaymentStatus() }
    }

    fun onCardHolderValueChanged(newValue: String) {
        currentState = currentState.copy(
            cardHolderInputField = InputFieldState(value = newValue),
            actionButtonState = currentState.actionButtonState.updateIsEnabled(
                newValue = isPayButtonEnabled(
                    cardHolderValue = newValue,
                ),
            ),
        )
        pushStateToUi(currentState)
    }

    fun validateCardHolder(cardHolderValue: String) {
        if (cardHolderValue.isBlank()) {
            currentState = currentState.copy(
                cardHolderInputField = InputFieldState(
                    value = "",
                    isError = true,
                    errorMessages = stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_error_empty_card_holder),
                ),
                actionButtonState = currentState.actionButtonState.updateIsEnabled(newValue = false),
            )
            pushStateToUi(currentState)
        }
    }

    fun onPostalCodeValueChanged(newValue: String) {
        currentState = currentState.copy(
            postalCodeField = InputFieldState(value = newValue),
            actionButtonState = currentState.actionButtonState.updateIsEnabled(
                newValue = isPayButtonEnabled(
                    postalCodeValue = newValue,
                ),
            ),
        )
        pushStateToUi(currentState)
    }

    fun validatePostalCode(postalCodeValue: String) {
        if (postalCodeValue.isBlank()) {
            currentState = currentState.copy(
                postalCodeField = InputFieldState(
                    value = "",
                    errorMessages = stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_error_empty_billing_postcode),
                    isError = true,
                ),
                actionButtonState = currentState.actionButtonState.updateIsEnabled(newValue = false),
            )
            pushStateToUi(currentState)
        }
    }

    fun onCardNumberValueChanged(newValue: String) {
        currentState = currentState.copy(
            cardNumberInputField = InputFieldState(value = newValue),
            actionButtonState = currentState.actionButtonState.updateIsEnabled(
                newValue = isPayButtonEnabled(
                    cardNumberValue = newValue,
                ),
            ),
        )
        pushStateToUi(currentState)
    }

    fun validateCardNumber(cardNumberValue: String) {
        if (cardNumberValue.isBlank()) {
            currentState = currentState.copy(
                cardNumberInputField = InputFieldState(
                    value = "",
                    isError = true,
                    errorMessages = stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_error_empty_card_number),
                ),
                actionButtonState = currentState.actionButtonState.updateIsEnabled(newValue = false),
            )
        } else if (!cardCheckoutScreenValidator.isCardNumberValidAndSupported(cardNumberValue, currentState.allowedCardSchemes)) {
            currentState = currentState.copy(
                cardNumberInputField = InputFieldState(
                    value = cardNumberValue,
                    isError = true,
                    errorMessages = stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_error_invalid_card_number),
                ),
                actionButtonState = currentState.actionButtonState.updateIsEnabled(newValue = false),
            )
        }
        pushStateToUi(currentState)
    }

    fun onCvvValueChanged(newValue: String) {
        currentState = currentState.copy(
            cvvInputFieldState = InputFieldState(value = newValue),
            actionButtonState = currentState.actionButtonState.updateIsEnabled(
                newValue = isPayButtonEnabled(
                    cvvValue = newValue,
                ),
            ),
        )
        pushStateToUi(currentState)
    }

    fun validateCvv(cvvValue: String) {
        if (cvvValue.isBlank()) {
            currentState = currentState.copy(
                cvvInputFieldState = InputFieldState(
                    value = "",
                    isError = true,
                    errorMessages = stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_error_empty_cvv),
                ),
                actionButtonState = currentState.actionButtonState.updateIsEnabled(newValue = false),
            )
        } else if (!cardCheckoutScreenValidator.isCvvValid(cvvValue)) {
            currentState = currentState.copy(
                cvvInputFieldState = InputFieldState(
                    value = cvvValue,
                    isError = true,
                    errorMessages = stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_error_invalid_cvv),
                ),
                actionButtonState = currentState.actionButtonState.updateIsEnabled(newValue = false),
            )
        }
        pushStateToUi(currentState)
    }

    fun onExpireDateValueChanged(newValue: String) {
        currentState = currentState.copy(
            cardExpireDateInputField = InputFieldState(newValue),
            actionButtonState = currentState.actionButtonState.updateIsEnabled(
                newValue = isPayButtonEnabled(
                    cardExpireDate = newValue,
                ),
            ),
        )
        pushStateToUi(currentState)
    }

    fun validateExpireDate(expireDateValue: String) {
        if (expireDateValue.isBlank()) {
            currentState = currentState.copy(
                cardExpireDateInputField = InputFieldState(
                    value = "",
                    isError = true,
                    errorMessages = stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_error_empty_expiry),
                ),
                actionButtonState = currentState.actionButtonState.updateIsEnabled(newValue = false),
            )
        } else if (!cardCheckoutScreenValidator.isCardExpireDateValid(expireDateValue)) {
            currentState = currentState.copy(
                cardExpireDateInputField = InputFieldState(
                    value = expireDateValue,
                    isError = true,
                    errorMessages = stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_error_invalid_expiry),
                ),
                actionButtonState = currentState.actionButtonState.updateIsEnabled(newValue = false),
            )
        }
        pushStateToUi(currentState)
    }

    fun onEmailValueChanged(newValue: String) {
        currentState = currentState.copy(
            emailInputField = InputFieldState(value = newValue),
            actionButtonState = currentState.actionButtonState.updateIsEnabled(
                newValue = isPayButtonEnabled(
                    emailValue = newValue,
                ),
            ),
        )
        pushStateToUi(currentState)
    }

    fun validateEmailValue(emailValue: String) {
        if (emailValue.isBlank()) {
            currentState = currentState.copy(
                emailInputField = InputFieldState(
                    value = emailValue,
                    isError = true,
                    errorMessages = stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_error_empty_email),
                ),
                actionButtonState = currentState.actionButtonState.updateIsEnabled(newValue = false),
            )
        } else if (!cardCheckoutScreenValidator.isEmailValid(emailValue)) {
            currentState = currentState.copy(
                emailInputField = InputFieldState(
                    value = emailValue,
                    isError = true,
                    errorMessages = stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_error_invalid_email),
                ),
                actionButtonState = currentState.actionButtonState.updateIsEnabled(newValue = false),
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
        isCheckBoxChecked: Boolean = currentState.checkBoxItem.isChecked,
    ) =
        cardHolderValue.isNotBlank() &&
            cardCheckoutScreenValidator.isCardNumberValidAndSupported(cardNumberValue, currentState.allowedCardSchemes) &&
            cardCheckoutScreenValidator.isCardExpireDateValid(cardExpireDate) &&
            cardCheckoutScreenValidator.isCvvValid(cvvValue) &&
            cardCheckoutScreenValidator.isEmailFieldValidWithInputFieldVisibility(
                emailValue,
                currentState.isEmailInputFieldRequired,
            ) &&
            cardCheckoutScreenValidator.isPostalCodeFieldWithInputFieldVisibility(
                postalCodeValue,
                currentState.isPostalCodeFieldRequired,
            ) &&
            cardCheckoutScreenValidator.isCheckBoxValid(isStartDestination, isCheckBoxChecked)

    private suspend fun observePaymentIntent() {
        observePaymentIntent.observePaymentIntent().collect { handlePaymentIntent(it) }
    }

    private fun handlePaymentIntent(paymentIntentResult: PaymentIntentResult) {
        if (paymentIntentResult is PaymentIntentResult.Success) {
            val countryList =
                getSupportedCountriesList(
                    paymentIntentResult.result.collectionBillingAddressRequired,
                    paymentIntentResult.result.billingAddress?.countryCode,
                )
            val currentSelectedCountry = if (countryList.isNotEmpty()) {
                countryList[0]
            } else {
                SupportedCountriesViewEntity("", "", true)
            }
            paymentIntentId = paymentIntentResult.result.id
            currentState = updateCurrentStateWithPaymentIntent(
                paymentIntentResult,
                countryList,
                currentSelectedCountry,
            )
            pushStateToUi(currentState)
        }
    }

    private fun updateCurrentStateWithPaymentIntent(
        paymentIntentResult: PaymentIntentResult.Success,
        countryList: List<SupportedCountriesViewEntity>,
        currentSelectedCountry: SupportedCountriesViewEntity,
    ) = currentState.copy(
        isLoading = false,
        headerType = getHeaderType(),
        orderId = paymentIntentResult.result.orderId,
        merchantName = paymentIntentResult.result.merchantName,
        totalAmount = paymentIntentResult.result.totalAmount.valueString,
        amountCurrency = Currency.getInstance(paymentIntentResult.result.totalAmount.currencyCode).symbol,
        checkBoxItem = currentState
            .checkBoxItem
            .updateIsVisible(isStartDestination || !paymentIntentResult.result.customerId.isNullOrBlank())
            .updateText(getCheckBoxMessage(paymentIntentResult)),
        allowedPaymentMethodsIcons = allowedPaymentMethodsViewEntityMapper.apply(
            paymentIntentResult.result.supportedCardsSchemes,
        ),
        allowedCardSchemes = paymentIntentResult.result.supportedCardsSchemes,
        isEmailInputFieldRequired = paymentIntentResult.result.collectionEmailRequired,
        isBillingCountryFieldRequired = paymentIntentResult.result.collectionBillingAddressRequired,
        supportedCountriesList = countryList,
        currentSelectedCountry = currentSelectedCountry,
        isPostalCodeFieldRequired = applyIsPostalCodeFieldRequiredLogic(currentSelectedCountry, paymentIntentResult.result.collectionBillingAddressRequired),
        actionButtonState = currentState.actionButtonState.updateText(
            newValue = getActionButtonTitle(
                paymentIntentResult,
            ),
        ),
        emailInputField = InputFieldState(value = paymentIntentResult.result.customerEmailAddress ?: ""),
        postalCodeField = InputFieldState(value = paymentIntentResult.result.billingAddress?.postcode ?: ""),
    )

    private fun getHeaderType() = if (isStartDestination) {
        CardCheckOutHeaderType.MERCHANT_HEADER
    } else {
        CardCheckOutHeaderType.AMOUNT_HEADER
    }

    private fun getToolBarTitle() = if (isStartDestination) {
        stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_title_setup_intent)
    } else {
        stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_title)
    }

    private fun getCheckBoxMessage(paymentIntentResult: PaymentIntentResult.Success) =
        if (isStartDestination) {
            String.format(
                Locale.getDefault(),
                "%s %s",
                paymentIntentResult.result.merchantName,
                stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_consent_terms),
            )
        } else {
            stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_save_card)
        }

    private fun getActionButtonTitle(paymentIntentResult: PaymentIntentResult.Success) =
        if (isStartDestination) {
            stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_pay_button_setup_intent)
        } else {
            String.format(
                Locale.getDefault(),
                "%s %s %s",
                stringProvider.getString(R.string.dojo_ui_sdk_card_details_checkout_button_pay),
                Currency.getInstance(paymentIntentResult.result.totalAmount.currencyCode).symbol,
                paymentIntentResult.result.totalAmount.valueString,
            )
        }

    private fun getSupportedCountriesList(
        collectionBillingAddressRequired: Boolean,
        selectedCountryCode: String?,
    ): List<SupportedCountriesViewEntity> {
        return if (collectionBillingAddressRequired) {
            supportedCountriesViewEntityMapper
                .mapToSupportedCountriesViewEntityWithPreSelectedCountry(
                    getSupportedCountriesUseCase.getSupportedCountries(),
                    selectedCountryCode,
                )
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
            pushStateToUi(
                currentState.copy(
                    actionButtonState = currentState.actionButtonState.updateIsLoading(newValue = it),
                ),
            )
        }
    }

    fun onCheckBoxChecked(isChecked: Boolean) {
        val newCheckBoxItem = currentState.checkBoxItem.copy(isChecked = isChecked)
        currentState = currentState.copy(
            checkBoxItem = newCheckBoxItem,
            actionButtonState = currentState.actionButtonState.updateIsEnabled(
                isPayButtonEnabled(isCheckBoxChecked = isChecked),
            ),
        )
        pushStateToUi(currentState)
    }

    fun onPayWithCardClicked() {
        viewModelScope.launch {
            makeCardPaymentUseCase
                .makeCardPayment(
                    params = MakeCardPaymentParams(
                        fullCardPaymentPayload = fullCardPaymentPayloadMapper.getPaymentPayLoad(
                            currentState,
                            isStartDestination,
                        ),
                        dojoCardPaymentHandler = dojoCardPaymentHandler,
                        paymentId = paymentIntentId,
                    ),
                    onError = { navigateToCardResult(DojoPaymentResult.SDK_INTERNAL_ERROR) },
                )
        }
    }

    private fun pushStateToUi(state: CardDetailsCheckoutState) {
        mutableState.postValue(state)
    }
}
