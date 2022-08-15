package tech.dojo.pay.uisdk.presentation.ui.result.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.DojoPaymentResult

class PaymentResultViewModelFactory(private val result: DojoPaymentResult) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PaymentResultViewModel(result) as T
    }
}
