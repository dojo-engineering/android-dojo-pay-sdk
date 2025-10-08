package tech.dojo.pay.uisdk.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.presentation.ui.result.mapper.PaymentResultViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.result.viewmodel.PaymentResultViewModel

internal class PaymentResultViewModelFactory(
    private val result: DojoPaymentResult,
    private val observePaymentIntent: ObservePaymentIntent,
    private val paymentResultViewEntityMapper: PaymentResultViewEntityMapper
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PaymentResultViewModel(
            result = result,
            observePaymentIntent = observePaymentIntent,
            paymentResultViewEntityMapper = paymentResultViewEntityMapper,
        ) as T
    }
}
