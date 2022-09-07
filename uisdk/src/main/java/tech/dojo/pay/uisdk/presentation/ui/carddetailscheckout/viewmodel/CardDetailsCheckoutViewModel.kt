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
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsCheckoutState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsInputFieldState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.InputFieldState
import java.util.Currency

class CardDetailsCheckoutViewModel(
    private val observePaymentIntent: ObservePaymentIntent,
    private val dojoCardPaymentHandler: DojoCardPaymentHandler,
    private val observePaymentStatus: ObservePaymentStatus,
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase
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
            cardDetailsInPutField = CardDetailsInputFieldState(
                cardNumberValue = "",
                cvvValue = "",
                expireDateValueValue = "",
            ),
            isLoading = false
        )
        pushStateToUi(currentState)
        viewModelScope.launch { observePaymentIntent() }
        viewModelScope.launch { observePaymentStatus() }
    }

    fun onCardHolderValueChanged(newValue: String) {
        currentState = currentState.copy(
            cardHolderInputField = InputFieldState(value = newValue)
        )
        pushStateToUi(currentState)
    }

    fun onCardNumberValueChanged(newValue: String) {
        currentState = currentState.copy(
            cardDetailsInPutField = CardDetailsInputFieldState(
                cardNumberValue = newValue,
                cvvValue = currentState.cardDetailsInPutField.cvvValue,
                expireDateValueValue = currentState.cardDetailsInPutField.expireDateValueValue,
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
            )
        )
        pushStateToUi(currentState)
    }

    private suspend fun observePaymentIntent() {
        observePaymentIntent.observePaymentIntent().collect { it?.let { handlePaymentIntent(it) } }
    }

    private fun handlePaymentIntent(paymentIntentResult: PaymentIntentResult) {
        if (paymentIntentResult is PaymentIntentResult.Success) {
            paymentToken = paymentIntentResult.result.paymentToken
            currentState = CardDetailsCheckoutState(
                totalAmount = paymentIntentResult.result.amount.value,
                amountCurrency = Currency.getInstance(paymentIntentResult.result.amount.currencyCode).symbol,
                cardHolderInputField = InputFieldState(value = ""),
                cardDetailsInPutField = CardDetailsInputFieldState(
                    cardNumberValue = "",
                    cvvValue = "",
                    expireDateValueValue = "",
                ),
                isLoading = false
            )
            pushStateToUi(currentState)
        }
    }

    private suspend fun observePaymentStatus() {
        observePaymentStatus.observePaymentStates().collect {
            if (!it) {
                pushStateToUi(currentState.copy(isLoading = false))
            }
        }
    }

    fun onPayWithCardClicked() {
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
        if (expireDateValueValue.isNotBlank() && expireDateValueValue.length> 2) {
            currentState.cardDetailsInPutField.expireDateValueValue.substring(2, 4)
        } else {
            ""
        }
}
