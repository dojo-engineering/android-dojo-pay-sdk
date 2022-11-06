package tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.uisdk.domain.ObservePaymentMethods
import tech.dojo.pay.uisdk.domain.ObserveWalletState
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory.Companion.paymentMethodsRepository
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory.Companion.walletStateRepository
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.mapper.PaymentMethodItemViewEntityMapper

class MangePaymentViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val observeWalletState = ObserveWalletState(walletStateRepository)
        val observePaymentMethods =
            ObservePaymentMethods(paymentMethodsRepository)
        val mapper = PaymentMethodItemViewEntityMapper()

        return MangePaymentViewModel(
            observeWalletState,
            observePaymentMethods,
            mapper
        ) as T
    }
}