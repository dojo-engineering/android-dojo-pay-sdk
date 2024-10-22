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
import tech.dojo.pay.uisdk.presentation.handler.DojoPaymentFlowHandlerWithFragmentImpl

object DojoSDKDropInUI {
    private val REQUEST_CODE_DROP_IN_UI = "DOJO_PAY_UI".hashCode()

    var dojoThemeSettings: DojoThemeSettings? = DojoThemeSettings()
    var dojoSDKDebugConfig: DojoSDKDebugConfig? = null

    internal var language: String? = null

    /**
     * Returns handler which handle payment process with activity  .
     */
    fun createUIPaymentHandler(
        activity: ComponentActivity,
        onResult: (DojoPaymentResult) -> Unit,
    ): DojoPaymentFlowHandler = DojoPaymentFlowHandlerImp(activity, onResult)

    /**
     * Returns handler which handle payment process with fragment .
     */
    fun createUIPaymentHandler(
        fragment: Fragment,
        onResult: (DojoPaymentResult) -> Unit,
    ): DojoPaymentFlowHandler = DojoPaymentFlowHandlerWithFragmentImpl(fragment, onResult)

    /**
     * Starts payment UI FLOW.
     * You should receive result via onActivityResult callback of the passed activity
     * if you call this directly with out using the handler.
     */
    fun startUIPaymentFlowForResult(
        activity: Activity,
        dojoPaymentFlowParams: DojoPaymentFlowParams,
    ) {
        val intent = DojoPaymentFlowHandlerResultContract().createIntent(
            activity,
            dojoPaymentFlowParams,
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
        dojoPaymentFlowParams: DojoPaymentFlowParams,
    ) {
        val intent = DojoPaymentFlowHandlerResultContract().createIntent(
            fragment.requireContext(),
            dojoPaymentFlowParams,
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
        intent: Intent?,
    ): DojoPaymentResult? {
        if (requestCode != REQUEST_CODE_DROP_IN_UI) return null
        return DojoPaymentFlowHandlerResultContract().parseResult(resultCode, intent)
    }

    /**
     * Set Locale to be used in all the flows, will force ui to use the passed locale instead of the device locale.
     * If language is not recognized or supported ui will fallback to english.
     * If no language is set we will use the device locale with the supported languages
     */
    fun setSdkLocale(language: String) {
        this.language = language
    }
}
