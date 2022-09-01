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
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsCheckoutState
import java.util.Currency

class CardDetailsCheckoutViewModel(
    private val observePaymentIntent: ObservePaymentIntent,
    private val dojoCardPaymentHandler: DojoCardPaymentHandler,
    private val observePaymentStatus: ObservePaymentStatus
) : ViewModel() {
    private lateinit var paymentToken: String
    private lateinit var currentState: CardDetailsCheckoutState
    private val mutableState = MutableLiveData<CardDetailsCheckoutState>()
    val state: LiveData<CardDetailsCheckoutState>
        get() = mutableState

    init {
        viewModelScope.launch {
            observePaymentIntent()
            observePaymentStatus()
        }
    }

    private suspend fun observePaymentIntent() {
        observePaymentIntent.observePaymentIntent().collect {
            it?.let {
                when (it) {
                    is PaymentIntentResult.Success -> {
                        paymentToken = it.result.paymentToken
                        currentState = CardDetailsCheckoutState(
                            totalAmount = it.result.amount.value.toString(),
                            amountCurrency = Currency.getInstance(it.result.amount.currencyCode).symbol,
                            isLoading = false

                        )
                        pushStateToUi(currentState)
                    }
                }
            }
        }
    }

    private suspend fun observePaymentStatus() {
        observePaymentStatus.observePaymentStates().collect {
            if (!it) { pushStateToUi(currentState.copy(isLoading = false)) }
        }

    }

    fun onPayWithCardClicked() {
        pushStateToUi(currentState.copy(isLoading = true))
        dojoCardPaymentHandler.executeCardPayment(
            paymentToken,
            getPaymentPayLoad()
        )
    }

    private fun pushStateToUi(state: CardDetailsCheckoutState) {
        mutableState.postValue(state)
    }

    private fun getPaymentPayLoad(): DojoCardPaymentPayLoad.FullCardPaymentPayload =
        DojoCardPaymentPayLoad.FullCardPaymentPayload(
            DojoCardDetails(
                cardNumber = "4456530000001096",
                cardName = "Card holder",
                expiryMonth = "12",
                expiryYear = "24",
                cv2 = "020"
            )
        )
}
