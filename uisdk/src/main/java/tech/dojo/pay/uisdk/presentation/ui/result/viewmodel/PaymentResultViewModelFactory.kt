package tech.dojo.pay.uisdk.presentation.ui.result.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.uisdk.presentation.ui.result.PaymentResultFragmentArgs

class PaymentResultViewModelFactory(private val arguments: PaymentResultFragmentArgs) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PaymentResultViewModel(arguments.result) as T
    }
}
