package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory
class PaymentMethodCheckoutViewModelFactory(
    private val gpayPaymentHandler: DojoGPayHandler
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val observePaymentIntent = ObservePaymentIntent(PaymentFlowViewModelFactory.paymentIntentRepository)
        return PaymentMethodCheckoutViewModel(observePaymentIntent, gpayPaymentHandler) as T
    }
}
