package tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.uisdk.domain.DeletePaymentMethodsUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentMethods
import tech.dojo.pay.uisdk.domain.ObserveWalletState
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory.Companion.paymentMethodsRepository
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory.Companion.walletStateRepository
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.mapper.PaymentMethodItemViewEntityMapper

class MangePaymentViewModelFactory(
    private val customerId: String,
    private val arguments: Bundle?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val customerSecret =
            (arguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as? DojoPaymentFlowParams)?.clientSecret
                ?: ""
        val observeWalletState = ObserveWalletState(walletStateRepository)
        val observePaymentMethods =
            ObservePaymentMethods(paymentMethodsRepository)
        val mapper = PaymentMethodItemViewEntityMapper()

        val deletePaymentMethodsUseCase = DeletePaymentMethodsUseCase(
            customerId,
            customerSecret,
            paymentMethodsRepository
        )
        return MangePaymentViewModel(
            deletePaymentMethodsUseCase,
            observeWalletState,
            observePaymentMethods,
            mapper
        ) as T
    }
}
