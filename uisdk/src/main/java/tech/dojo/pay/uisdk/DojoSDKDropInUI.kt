package tech.dojo.pay.uisdk

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.entities.DojoSDKDebugConfig
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.entities.DojoThemeSettings
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract
import tech.dojo.pay.uisdk.presentation.handler.DojoPaymentFlowHandler
import tech.dojo.pay.uisdk.presentation.handler.DojoPaymentFlowHandlerImp

object DojoSDKDropInUI {
    private val REQUEST_CODE_DROP_IN_UI = "DOJO_PAY_UI".hashCode()

    var dojoThemeSettings: DojoThemeSettings? = DojoThemeSettings()
    var dojoSDKDebugConfig: DojoSDKDebugConfig? = null

    /**
     * Returns handler which handle payment process with UI .
     */
    fun createUIPaymentHandler(
        activity: ComponentActivity,
        onResult: (DojoPaymentResult) -> Unit
    ): DojoPaymentFlowHandler = DojoPaymentFlowHandlerImp(activity, onResult)

    /**
     * Starts payment UI FLOW.
     * You should receive result via onActivityResult callback of the passed activity
     * if you call this directly with out using the handler.
     */
    fun startUIPaymentFlowForResult(
        activity: Activity,
        dojoPaymentFlowParams: DojoPaymentFlowParams
    ) {
        val intent = DojoPaymentFlowHandlerResultContract().createIntent(
            activity,
            dojoPaymentFlowParams
        )
        activity.startActivityForResult(intent, REQUEST_CODE_DROP_IN_UI)
    }

    /**
     * Starts payment UI FLOW.
     * You should receive result via onActivityResult callback of the passed fragment
     * if you call this directly with out using the handler.
     */
    fun startUIPaymentFlowForResult(
        fragment: Fragment,
        dojoPaymentFlowParams: DojoPaymentFlowParams
    ) {
        val intent = DojoPaymentFlowHandlerResultContract().createIntent(
            fragment.requireContext(),
            dojoPaymentFlowParams
        )
        fragment.startActivityForResult(intent, REQUEST_CODE_DROP_IN_UI)
    }

    /**
     * Parses activity result to UI FLow.
     * If the result was not initiated by startUIPaymentFlowForResult , then null will be returned.
     */
    fun parseUIPaymentFlowResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ): DojoPaymentResult? {
        if (requestCode != REQUEST_CODE_DROP_IN_UI) return null
        return DojoPaymentFlowHandlerResultContract().parseResult(resultCode, intent)
    }
}
