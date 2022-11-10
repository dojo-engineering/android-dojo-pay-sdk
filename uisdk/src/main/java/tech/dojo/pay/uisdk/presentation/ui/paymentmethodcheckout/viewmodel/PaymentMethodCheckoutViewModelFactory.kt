package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentMethods
import tech.dojo.pay.uisdk.domain.UpdateWalletState
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory.Companion.paymentMethodsRepository
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory.Companion.walletStateRepository
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract

class PaymentMethodCheckoutViewModelFactory(
    private val gpayPaymentHandler: DojoGPayHandler,
    private val arguments: Bundle?
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val observePaymentIntent =
            ObservePaymentIntent(PaymentFlowViewModelFactory.paymentIntentRepository)
        val updateWalletState = UpdateWalletState(walletStateRepository)
        val observePaymentMethods = ObservePaymentMethods(paymentMethodsRepository)

        val gPayConfig =
            (arguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as? DojoPaymentFlowParams)?.GPayConfig
        return PaymentMethodCheckoutViewModel(
            updateWalletState,
            observePaymentIntent,
            observePaymentMethods,
            gpayPaymentHandler,
            gPayConfig
        ) as T
    }
}
