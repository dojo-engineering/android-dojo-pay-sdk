package tech.dojo.pay.sdk

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import tech.dojo.pay.sdk.card.*
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandlerImpl
import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandlerImpl
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoGPayParams
import tech.dojo.pay.sdk.card.entities.DojoTotalAmountPayload
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandler
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.sdk.card.presentation.gpay.util.DojoGPayEngine

object DojoSdk {

    private val REQUEST_CODE = "DOJO_PAY".hashCode()

    var sandbox: Boolean = false

    /**
     * Returns handler which starts payment process.
     */
    fun createCardPaymentHandler(
        activity: ComponentActivity,
        onResult: (DojoPaymentResult) -> Unit
    ): DojoCardPaymentHandler = DojoCardPaymentHandlerImpl(activity, onResult)

    fun createGPayHandler(
        activity: ComponentActivity,
        onResult: (DojoPaymentResult) -> Unit
    ): DojoGPayHandler = DojoGPayHandlerImpl(activity, onResult)

    /**
     * Starts card payment activity.
     * You should receive result via onActivityResult callback.
     */
    fun startCardPayment(
        activity: Activity,
        token: String,
        payload: DojoCardPaymentPayload
    ) {
        val intent = DojoCardPaymentResultContract().createIntent(
            activity,
            DojoCardPaymentParams(token, payload)
        )
        activity.startActivityForResult(intent, REQUEST_CODE)
    }

    fun startGPay(
        activity: Activity,
        token: String,
        payload: DojoTotalAmountPayload

    ) {
        val intent = DojoGPayResultContract().createIntent(
            activity,
            DojoGPayParams(token, payload)
        )
        activity.startActivityForResult(intent, REQUEST_CODE)
    }

    fun isGpayAvailable(
        activity: Activity,
        onGpayAvailable: () -> Unit,
        onGpayUnavailable: () -> Unit
    ) {
        DojoGPayEngine(activity)
            .isReadyToPay({ onGpayAvailable() }, { onGpayUnavailable() })
    }

    /**
     * Parses activity result to DojoCardPaymentResult.
     * If the result was not initiated by card payment, then null will be returned.
     */
    fun parseCardPaymentResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ): DojoPaymentResult? {
        if (requestCode != REQUEST_CODE) return null
        return DojoCardPaymentResultContract().parseResult(resultCode, intent)
    }
}
