package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.presentation.PaymentFlowViewModelFactory
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract

class PaymentMethodCheckoutViewModelFactory(
    private val gpayPaymentHandler: DojoGPayHandler,
    private val arguments: Bundle?,
    private val isMangePaymentEnabled:Boolean
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val observePaymentIntent = ObservePaymentIntent(PaymentFlowViewModelFactory.paymentIntentRepository)
        val gPayPayload =
            (arguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as? DojoPaymentFlowParams)?.GPayPayload
        return PaymentMethodCheckoutViewModel(observePaymentIntent, gpayPaymentHandler,gPayPayload,isMangePaymentEnabled) as T
    }
}
