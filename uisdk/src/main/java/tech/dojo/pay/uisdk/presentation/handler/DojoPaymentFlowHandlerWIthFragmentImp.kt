package tech.dojo.pay.uisdk.presentation.handler

import androidx.fragment.app.Fragment
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract

internal class DojoPaymentFlowHandlerWIthFragmentImp(
    fragment: Fragment,
    onResult: (DojoPaymentResult) -> Unit,

) : DojoPaymentFlowHandler {
    private val paymentFlowLauncher =
        fragment.registerForActivityResult(DojoPaymentFlowHandlerResultContract(), onResult)

    override fun startPaymentFlow(dojoPaymentFlowParams: DojoPaymentFlowParams) {
        paymentFlowLauncher.launch(dojoPaymentFlowParams)
    }
}
