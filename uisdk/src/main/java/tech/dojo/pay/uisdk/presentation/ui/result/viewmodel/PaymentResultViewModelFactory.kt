package tech.dojo.pay.uisdk.presentation.ui.result.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.RefreshPaymentIntentUseCase
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory

class PaymentResultViewModelFactory(private val result: DojoPaymentResult) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val observePaymentIntent =
            ObservePaymentIntent(PaymentFlowViewModelFactory.paymentIntentRepository)
        val refreshPaymentIntent =
            RefreshPaymentIntentUseCase(PaymentFlowViewModelFactory.paymentIntentRepository)
        return PaymentResultViewModel(
            result,
            observePaymentIntent,
            refreshPaymentIntent,
            false
        ) as T
    }
}
