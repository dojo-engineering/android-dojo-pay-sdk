package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandler
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract

class CardDetailsCheckoutViewModelFactory(
    private val arguments: Bundle?,
    private val dojoCardPaymentHandler: DojoCardPaymentHandler
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val paymentToken =
            (arguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as? DojoPaymentFlowParams)?.paymentToken
                ?: ""
        return CardDetailsCheckoutViewModel(paymentToken, dojoCardPaymentHandler) as T
    }
}
