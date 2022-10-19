package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.card.entities.DojoCardDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandler
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.GetSupportedCountriesUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper.SupportedCountriesViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsCheckoutState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsInputFieldState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.InputFieldState
import java.util.Currency

internal class CardDetailsCheckoutViewModel(
    private val observePaymentIntent: ObservePaymentIntent,
    private val dojoCardPaymentHandler: DojoCardPaymentHandler,
    private val observePaymentStatus: ObservePaymentStatus,
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase,
    private val getSupportedCountriesUseCase: GetSupportedCountriesUseCase,
    private val supportedCountriesViewEntityMapper: SupportedCountriesViewEntityMapper
) : ViewModel() {
    private lateinit var paymentToken: String
    private var currentState: CardDetailsCheckoutState
    private val mutableState = MutableLiveData<CardDetailsCheckoutState>()
    val state: LiveData<CardDetailsCheckoutState>
        get() = mutableState

    init {
        currentState = CardDetailsCheckoutState(
            totalAmount = "",
            amountCurrency = "",
            cardHolderInputField = InputFieldState(value = ""),
            emailInputField = InputFieldState(value = ""),
            isEmailInputFieldRequired = false,
            isBillingCountryFieldRequired = false,
            supportedCountriesList = emptyList(),
            currentSelectedCountry = SupportedCountriesViewEntity("", "", false),
            isPostalCodeFieldRequired = false,
            postalCodeField = InputFieldState(value = ""),
            cardDetailsInPutField = CardDetailsInputFieldState(
                cardNumberValue = "",
                cvvValue = "",
                expireDateValueValue = "",
            ),
            isLoading = false,
            isEnabled = false
        )
        pushStateToUi(currentState)
        viewModelScope.launch { observePaymentIntent() }
        viewModelScope.launch { observePaymentStatus() }
    }

    fun onCardHolderValueChanged(newValue: String) {
        currentState = currentState.copy(
            cardHolderInputField = InputFieldState(value = newValue),
            isEnabled = isPayButtonEnabled(
                newValue,
                currentState.cardDetailsInPutField.cardNumberValue,
                currentState.cardDetailsInPutField.cvvValue,
                currentState.cardDetailsInPutField.expireDateValueValue,
                currentState.emailInputField.value
            )
        )
        pushStateToUi(currentState)
    }

    fun onPostalCodeValueChanged(newValue: String) {
        currentState = currentState.copy(postalCodeField = InputFieldState(value = newValue),)
        pushStateToUi(currentState)
    }

    fun onCardNumberValueChanged(newValue: String) {
        currentState = currentState.copy(
            cardDetailsInPutField = CardDetailsInputFieldState(
                cardNumberValue = newValue,
                cvvValue = currentState.cardDetailsInPutField.cvvValue,
                expireDateValueValue = currentState.cardDetailsInPutField.expireDateValueValue,
            ),
            isEnabled = isPayButtonEnabled(
                currentState.cardHolderInputField.value,
                newValue,
                currentState.cardDetailsInPutField.cvvValue,
                currentState.cardDetailsInPutField.expireDateValueValue,
                currentState.emailInputField.value
            )
        )
        pushStateToUi(currentState)
    }

    fun onCvvValueChanged(newValue: String) {
        currentState =
            currentState.copy(
                cardDetailsInPutField = CardDetailsInputFieldState(
                    cardNumberValue = currentState.cardDetailsInPutField.cardNumberValue,
                    cvvValue = newValue,
                    expireDateValueValue = currentState.cardDetailsInPutField.expireDateValueValue,
                ),
                isEnabled = isPayButtonEnabled(
                    currentState.cardHolderInputField.value,
                    currentState.cardDetailsInPutField.cardNumberValue,
                    newValue,
                    currentState.cardDetailsInPutField.expireDateValueValue,
                    currentState.emailInputField.value

                )
            )
        pushStateToUi(currentState)
    }

    fun onExpireDareValueChanged(newValue: String) {
        currentState = currentState.copy(
            cardDetailsInPutField = CardDetailsInputFieldState(
                cardNumberValue = currentState.cardDetailsInPutField.cardNumberValue,
                cvvValue = currentState.cardDetailsInPutField.cvvValue,
                expireDateValueValue = newValue,
            ),
            isEnabled = isPayButtonEnabled(
                currentState.cardHolderInputField.value,
                currentState.cardDetailsInPutField.cardNumberValue,
                currentState.cardDetailsInPutField.cvvValue,
                newValue,
                currentState.emailInputField.value
            )
        )
        pushStateToUi(currentState)
    }

    fun onEmailValueChanged(newValue: String) {
        currentState = currentState.copy(
            emailInputField = InputFieldState(value = newValue),
            isEnabled = isPayButtonEnabled(
                currentState.cardHolderInputField.value,
                currentState.cardDetailsInPutField.cardNumberValue,
                currentState.cardDetailsInPutField.cvvValue,
                currentState.cardDetailsInPutField.expireDateValueValue,
                newValue
            )
        )
        pushStateToUi(currentState)
    }

    fun onCountrySelected(newValue: SupportedCountriesViewEntity) {
        currentState = currentState.copy(
            currentSelectedCountry = newValue,
            isPostalCodeFieldRequired = applyIsPostalCodeFieldRequiredLogic(
                newValue, currentState.isBillingCountryFieldRequired
            )
        )
    }

    private fun isPayButtonEnabled(
        cardHolder: String,
        cardNumber: String,
        cvv: String,
        expireDateValue: String,
        emailAddressValue: String
    ) =
        cardHolder.isNotBlank() && cardNumber.isNotBlank() && cvv.isNotBlank() && expireDateValue.isNotBlank() && isMailFieldValid(
            emailAddressValue
        )

    private fun isMailFieldValid(emailAddressValue: String): Boolean {
        return if (currentState.isEmailInputFieldRequired) {
            emailAddressValue.isNotBlank()
        } else {
            true
        }
    }

    private suspend fun observePaymentIntent() {
        observePaymentIntent.observePaymentIntent().collect { it?.let { handlePaymentIntent(it) } }
    }

    private fun handlePaymentIntent(paymentIntentResult: PaymentIntentResult) {
        if (paymentIntentResult is PaymentIntentResult.Success) {
            paymentToken = paymentIntentResult.result.paymentToken
            currentState = currentState.copy(
                totalAmount = paymentIntentResult.result.amount.valueString,
                amountCurrency = Currency.getInstance(paymentIntentResult.result.amount.currencyCode).symbol,
                isEmailInputFieldRequired = paymentIntentResult.result.collectionEmailRequired,
                isBillingCountryFieldRequired = paymentIntentResult.result.collectionBillingAddressRequired,
                supportedCountriesList = getSupportedCountriesList(paymentIntentResult.result.collectionBillingAddressRequired),
                isPostalCodeFieldRequired = paymentIntentResult.result.collectionBillingAddressRequired
            )
            pushStateToUi(currentState)
        }
    }

    private fun getSupportedCountriesList(collectionBillingAddressRequired: Boolean): List<SupportedCountriesViewEntity> {
        return if (collectionBillingAddressRequired) getSupportedCountriesUseCase
            .getSupportedCountries()
            .map { supportedCountriesViewEntityMapper.apply(it) } else emptyList()
    }

    private fun applyIsPostalCodeFieldRequiredLogic(
        SupportedCountryItem: SupportedCountriesViewEntity,
        collectionBillingAddressRequired: Boolean
    ): Boolean {
        return SupportedCountryItem.isPostalCodeEnabled && collectionBillingAddressRequired
    }

    private suspend fun observePaymentStatus() {
        observePaymentStatus.observePaymentStates().collect {
            if (!it) {
                pushStateToUi(currentState.copy(isLoading = false))
            }
        }
    }

    fun onPayWithCardClicked() {
        viewModelScope.launch { observePaymentIntent() }
        updatePaymentStateUseCase.updatePaymentSate(isActive = true)
        pushStateToUi(currentState.copy(isLoading = true))
        dojoCardPaymentHandler.executeCardPayment(paymentToken, getPaymentPayLoad())
    }

    private fun pushStateToUi(state: CardDetailsCheckoutState) {
        mutableState.postValue(state)
    }

    private fun getPaymentPayLoad(): DojoCardPaymentPayLoad.FullCardPaymentPayload =
        DojoCardPaymentPayLoad.FullCardPaymentPayload(
            DojoCardDetails(
                cardNumber = currentState.cardDetailsInPutField.cardNumberValue,
                cardName = currentState.cardHolderInputField.value,
                expiryMonth = getExpiryMonth(currentState.cardDetailsInPutField.expireDateValueValue),
                expiryYear = getExpiryYear(currentState.cardDetailsInPutField.expireDateValueValue),
                cv2 = currentState.cardDetailsInPutField.cvvValue
            )
        )

    private fun getExpiryMonth(expireDateValueValue: String) =
        if (expireDateValueValue.isNotBlank()) {
            currentState.cardDetailsInPutField.expireDateValueValue.substring(0, 2)
        } else {
            ""
        }

    private fun getExpiryYear(expireDateValueValue: String) =
        if (expireDateValueValue.isNotBlank() && expireDateValueValue.length > 2) {
            currentState.cardDetailsInPutField.expireDateValueValue.substring(2, 4)
        } else {
            ""
        }
}
