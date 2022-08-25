package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract
class PaymentMethodCheckoutViewModelFactory(
    private val arguments: Bundle?,
    private val gpayPaymentHandler: DojoGPayHandler
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val paymentToken =
            (arguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as? DojoPaymentFlowParams)?.paymentId
                ?: ""
        return PaymentMethodCheckoutViewModel(paymentToken, gpayPaymentHandler) as T
    }
}
