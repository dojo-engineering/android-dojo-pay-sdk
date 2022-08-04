package tech.dojo.pay.uisdk

import androidx.activity.ComponentActivity
import tech.dojo.pay.uisdk.paymentflow.handler.DojoPaymentFlowHandler
import tech.dojo.pay.uisdk.paymentflow.handler.DojoPaymentFlowHandlerImp

object DojoPayUISdk {
    var sandbox: Boolean = false
    /**
     * Returns handler which starts payment process with UI .
     */
    fun startPaymentFLow(
        activity: ComponentActivity,
    ): DojoPaymentFlowHandler = DojoPaymentFlowHandlerImp(activity)
}