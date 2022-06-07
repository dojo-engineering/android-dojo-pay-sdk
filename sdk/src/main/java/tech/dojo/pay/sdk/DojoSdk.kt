package tech.dojo.pay.sdk

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import tech.dojo.pay.sdk.card.DojoCardPaymentHandler
import tech.dojo.pay.sdk.card.DojoCardPaymentHandlerImpl
import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult

object DojoSdk {

    private val REQUEST_CODE = "DOJO_PAY".hashCode()

    var sandbox: Boolean = false

    /**
     * Returns handler which starts payment process.
     */
    fun createCardPaymentHandler(
        activity: ComponentActivity,
        onResult: (DojoCardPaymentResult) -> Unit
    ): DojoCardPaymentHandler = DojoCardPaymentHandlerImpl(activity, onResult)

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

    /**
     * Parses activity result to DojoCardPaymentResult.
     * If the result was not initiated by card payment, then null will be returned.
     */
    fun parseCardPaymentResult(requestCode: Int, resultCode: Int, intent: Intent?): DojoCardPaymentResult? {
        if (requestCode != REQUEST_CODE) return null
        return DojoCardPaymentResultContract().parseResult(resultCode, intent)
    }
}
