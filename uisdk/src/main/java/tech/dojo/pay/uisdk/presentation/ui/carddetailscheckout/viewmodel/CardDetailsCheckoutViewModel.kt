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
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsCheckoutState

class CardDetailsCheckoutViewModel(
    private val observePaymentIntent: ObservePaymentIntent,
    private val dojoCardPaymentHandler: DojoCardPaymentHandler
) : ViewModel() {
    private lateinit var paymentToken: String
    private val mutableState = MutableLiveData<CardDetailsCheckoutState>()
    val state: LiveData<CardDetailsCheckoutState>
        get() = mutableState
    init {
        viewModelScope.launch { observePaymentIntent() }
    }

    private suspend fun observePaymentIntent() {
        observePaymentIntent.observePaymentIntent().collect {
            it?.let {
                when (it) {
                    is PaymentIntentResult.Success -> paymentToken = it.result.clientSessionSecret
                }
            }
        }
    }

    fun onPayWithCardClicked() {
        mutableState.postValue(CardDetailsCheckoutState(isLoading= true))
        dojoCardPaymentHandler.executeCardPayment(
            paymentToken,
            getPaymentPayLoad()
        )
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
