package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandler
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory

class CardDetailsCheckoutViewModelFactory(
    private val dojoCardPaymentHandler: DojoCardPaymentHandler
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val observePaymentIntent = ObservePaymentIntent(PaymentFlowViewModelFactory.repo)
        return CardDetailsCheckoutViewModel(observePaymentIntent, dojoCardPaymentHandler) as T
    }
}
