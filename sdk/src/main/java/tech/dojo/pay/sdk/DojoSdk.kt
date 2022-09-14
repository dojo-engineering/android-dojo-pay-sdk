package tech.dojo.pay.sdk

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract
import tech.dojo.pay.sdk.card.DojoGPayResultContract
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad.FullCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad.SavedCardPaymentPayLoad
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.entities.DojoGPayParams
import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandler
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandlerImpl
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoSavedCardPaymentHandler
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoSavedCardPaymentHandlerImpl
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandlerImpl
import tech.dojo.pay.sdk.card.presentation.gpay.util.DojoGPayEngine
import tech.dojo.pay.sdk.payemntintent.PaymentIntentProvider
import tech.dojo.pay.sdk.payemntintent.data.PaymentIntentApiBuilder
import tech.dojo.pay.sdk.payemntintent.data.PaymentIntentRepository

object DojoSdk {
    private val REQUEST_CODE_SAVED_CARD = "DOJO_PAY_SAVED_CARD".hashCode()
    private val REQUEST_CODE_CARD = "DOJO_PAY".hashCode()
    private val REQUEST_CODE_G_PAY = "DOJO_G_PAY".hashCode()

    var cardSandbox: Boolean = false
    var walletSandBox: Boolean = false

    /**
     * Returns handler which starts payment process for normal card payment .
     */
    fun createCardPaymentHandler(
        activity: ComponentActivity,
        onResult: (DojoPaymentResult) -> Unit
    ): DojoCardPaymentHandler = DojoCardPaymentHandlerImpl(activity, onResult)

    /**
     * Returns handler which starts payment process for saved card payment .
     */
    fun createSavedCardPaymentHandler(
        activity: ComponentActivity,
        onResult: (DojoPaymentResult) -> Unit
    ): DojoSavedCardPaymentHandler = DojoSavedCardPaymentHandlerImpl(activity, onResult)

    /**
     * Returns handler which starts payment process G pay.
     */
    fun createGPayHandler(
        activity: ComponentActivity,
        onResult: (DojoPaymentResult) -> Unit
    ): DojoGPayHandler = DojoGPayHandlerImpl(activity, onResult)

    /**
     * Starts card payment activity.
     * You should receive result via onActivityResult callback
     * if you call this directly with out using the handler.
     */
    fun startCardPayment(
        activity: Activity,
        token: String,
        payload: FullCardPaymentPayload
    ) {
        val intent = DojoCardPaymentResultContract().createIntent(
            activity,
            DojoCardPaymentParams(token, payload)
        )
        activity.startActivityForResult(intent, REQUEST_CODE_CARD)
    }

    /**
     * Starts saved card payment activity.
     * You should receive result via onActivityResult callback
     * if you call this directly with out using the handler.
     */
    fun startSavedCardPayment(
        activity: Activity,
        token: String,
        payload: SavedCardPaymentPayLoad
    ) {
        val intent = DojoCardPaymentResultContract().createIntent(
            activity,
            DojoCardPaymentParams(token, payload)
        )
        activity.startActivityForResult(intent, REQUEST_CODE_SAVED_CARD)
    }

    /**
     * Starts google pay payment activity.
     * You should receive result via onActivityResult callback
     * if you call this directly with out using the handler.
     */
    fun startGPay(
        activity: Activity,
        GPayPayload: DojoGPayPayload,
        paymentIntent: DojoPaymentIntent

    ) {
        val intent = DojoGPayResultContract().createIntent(
            activity,
            DojoGPayParams(GPayPayload, paymentIntent)
        )
        activity.startActivityForResult(intent, REQUEST_CODE_G_PAY)
    }

    /**
     * check if google pay available for this device.
     */
    fun isGpayAvailable(
        activity: Activity,
        dojoGPayConfig: DojoGPayConfig,
        onGpayAvailable: () -> Unit,
        onGpayUnavailable: () -> Unit
    ) {
        DojoGPayEngine(activity)
            .isReadyToPay(dojoGPayConfig, { onGpayAvailable() }, { onGpayUnavailable() })
    }

    /**
     * Parses activity result to DojoGPayResultContract.
     * If the result was not initiated by startGPay payment, then null will be returned.
     */
    fun parseGPayPaymentResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ): DojoPaymentResult? {
        if (requestCode != REQUEST_CODE_G_PAY) return null
        return DojoGPayResultContract().parseResult(resultCode, intent)
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
        if (requestCode != REQUEST_CODE_CARD) return null
        return DojoCardPaymentResultContract().parseResult(resultCode, intent)
    }

    /**
     * Parses activity result to DojoCardPaymentResult for savedCard.
     * If the result was not initiated by card payment, then null will be returned.
     */
    fun parseSavedCardPaymentResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ): DojoPaymentResult? {
        if (requestCode != REQUEST_CODE_SAVED_CARD) return null
        return DojoCardPaymentResultContract().parseResult(resultCode, intent)
    }

    /**
     * fetch payment intent object in format of json for specific payment id
     */
    fun fetchPaymentIntent(
        paymentId: String,
        onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit,
        onPaymentIntentFailed: () -> Unit
    ) {
        return PaymentIntentProvider(PaymentIntentRepository(PaymentIntentApiBuilder().create()))
            .fetchPaymentIntent(paymentId, onPaymentIntentSuccess, onPaymentIntentFailed)
    }

    /**
     * refresh payment intent object in format of json for specific payment id
     */
    fun refreshPaymentIntent(
        paymentId: String,
        onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit,
        onPaymentIntentFailed: () -> Unit
    ) {
        return PaymentIntentProvider(PaymentIntentRepository(PaymentIntentApiBuilder().create()))
            .refreshPaymentIntent(paymentId, onPaymentIntentSuccess, onPaymentIntentFailed)
    }
}
