package tech.dojo.pay.uisdk

import androidx.activity.ComponentActivity
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.entities.DojoThemeSettings
import tech.dojo.pay.uisdk.presentation.handler.DojoPaymentFlowHandler
import tech.dojo.pay.uisdk.presentation.handler.DojoPaymentFlowHandlerImp

object DojoSDKDropInUI {
    var isWalletSandBox: Boolean = false
    var dojoThemeSettings: DojoThemeSettings? = null

    /**
     * Returns handler which handle payment process with UI .
     */
    fun createUIPaymentHandler(
        activity: ComponentActivity,
        onResult: (DojoPaymentResult) -> Unit
    ): DojoPaymentFlowHandler = DojoPaymentFlowHandlerImp(activity, onResult)
}
