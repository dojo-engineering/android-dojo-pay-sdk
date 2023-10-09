package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoSavedCardPaymentHandler
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.ObservePaymentMethods
import tech.dojo.pay.uisdk.domain.ObservePaymentStatus
import tech.dojo.pay.uisdk.domain.ObserveWalletState
import tech.dojo.pay.uisdk.domain.UpdatePaymentStateUseCase
import tech.dojo.pay.uisdk.domain.UpdateWalletState
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory.Companion.paymentMethodsRepository
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory.Companion.walletStateRepository
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract

class PaymentMethodCheckoutViewModelFactory(
    private val savedCardPaymentHandler: DojoSavedCardPaymentHandler,
    private val gpayPaymentHandler: DojoGPayHandler,
    private val arguments: Bundle?,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val observePaymentIntent =
            ObservePaymentIntent(PaymentFlowViewModelFactory.paymentIntentRepository)
        val updateWalletState = UpdateWalletState(walletStateRepository)
        val observePaymentMethods = ObservePaymentMethods(paymentMethodsRepository)
        val observePaymentStatus =
            ObservePaymentStatus(PaymentFlowViewModelFactory.paymentStatusRepository)
        val updatePaymentStateUseCase =
            UpdatePaymentStateUseCase(PaymentFlowViewModelFactory.paymentStatusRepository)
        val gPayConfig =
            (arguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as? DojoPaymentFlowParams)?.GPayConfig

        val observeWalletState = ObserveWalletState(walletStateRepository)
        return PaymentMethodCheckoutViewModel(
            savedCardPaymentHandler,
            updateWalletState,
            observePaymentIntent,
            observePaymentMethods,
            gpayPaymentHandler,
            gPayConfig,
            observePaymentStatus,
            updatePaymentStateUseCase,
            observeWalletState,
        ) as T
    }
}
