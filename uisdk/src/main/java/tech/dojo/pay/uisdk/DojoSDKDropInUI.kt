package tech.dojo.pay.uisdk

import androidx.activity.ComponentActivity
import tech.dojo.pay.uisdk.paymentflow.handler.DojoPaymentFlowHandler
import tech.dojo.pay.uisdk.paymentflow.handler.DojoPaymentFlowHandlerImp

object DojoSDKDropInUI {
    var sandbox: Boolean = false

    /**
     * Returns handler which handle payment process with UI .
     */
    fun createUIPaymentHandler(
        activity: ComponentActivity,
    ): DojoPaymentFlowHandler = DojoPaymentFlowHandlerImp(activity)
}
