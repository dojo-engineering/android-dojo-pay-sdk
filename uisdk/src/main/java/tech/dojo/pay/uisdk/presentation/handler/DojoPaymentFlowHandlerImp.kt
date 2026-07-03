package tech.dojo.pay.uisdk.presentation.handler

import android.util.Log
import androidx.activity.ComponentActivity
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract

internal class DojoPaymentFlowHandlerImp(
    activity: ComponentActivity,
    onResult: (DojoPaymentResult) -> Unit
) : DojoPaymentFlowHandler {
    companion object { private const val TAG = "PaymentFlowHandlerImp" }

    private val paymentFlowLauncher =
        activity.registerForActivityResult(DojoPaymentFlowHandlerResultContract(), onResult)

    override fun startPaymentFlow(dojoPaymentFlowParams: DojoPaymentFlowParams) {
        Log.d(TAG, "startPaymentFlow: Launching PaymentFlowContainerActivity. paymentId='${dojoPaymentFlowParams.paymentId}', paymentType=${dojoPaymentFlowParams.paymentType}, clientSecret='${dojoPaymentFlowParams.clientSecret?.take(10)}...'")
        paymentFlowLauncher.launch(dojoPaymentFlowParams)
    }
}
