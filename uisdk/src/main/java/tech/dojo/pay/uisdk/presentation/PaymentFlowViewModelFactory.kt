package tech.dojo.pay.uisdk.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.uisdk.data.PaymentIntentRepository
import tech.dojo.pay.uisdk.domain.FetchPaymentIntentUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent

class PaymentFlowViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = PaymentIntentRepository()
        val fetchPaymentIntentUseCase = FetchPaymentIntentUseCase(repo)
        val observePaymentIntent = ObservePaymentIntent(repo)
        return PaymentFlowViewModel(fetchPaymentIntentUseCase, observePaymentIntent) as T
    }
}
