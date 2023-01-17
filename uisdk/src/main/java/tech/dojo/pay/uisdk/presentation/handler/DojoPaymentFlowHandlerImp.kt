package tech.dojo.pay.uisdk.presentation.handler

import androidx.activity.ComponentActivity
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract

internal class DojoPaymentFlowHandlerImp(
    activity: ComponentActivity,
    onResult: (DojoPaymentResult) -> Unit

) : DojoPaymentFlowHandler {
    private val paymentFlowLauncher =
        activity.registerForActivityResult(DojoPaymentFlowHandlerResultContract(), onResult)

    override fun startPaymentFlow(dojoPaymentFlowParams: DojoPaymentFlowParams) {
        paymentFlowLauncher.launch(dojoPaymentFlowParams)
    }
}
