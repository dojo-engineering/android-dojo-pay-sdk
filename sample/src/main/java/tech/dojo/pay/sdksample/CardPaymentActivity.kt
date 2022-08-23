package tech.dojo.pay.sdksample

import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent

class CardPaymentActivity : CardPaymentBaseActivity() {

    private val cardPayment = DojoSdk.createCardPaymentHandler(this) { result ->
        setProgressIndicatorVisible(false)
        showResult(result)
    }
    private val savedCardPayment = DojoSdk.createSavedCardPaymentHandler(this) { result ->
        setProgressIndicatorVisible(false)
        showResult(result)
    }
    private val gPayment = DojoSdk.createGPayHandler(this) { result ->
        showResult(result)
    }

    override fun onSandboxChecked(isChecked: Boolean) {
        DojoSdk.sandbox = isChecked
    }

    override fun onPayClicked(token: String, payload: DojoCardPaymentPayLoad.FullCardPaymentPayload) {
        setProgressIndicatorVisible(true)
//        cardPayment.executeCardPayment(token, payload)
        DojoSdk.fetchPaymentIntent("pi_sandbox_ONH5tKT6YU2JY8_AqhB3og",::onPaymentIntentGreen,{ println("=====================red")})
    }

    private fun onPaymentIntentGreen(json:String){
        println("================================${json}")
    }

    override fun onPaySavedCardClicked(
        token: String,
        payload: DojoCardPaymentPayLoad.SavedCardPaymentPayLoad
    ) {
        setProgressIndicatorVisible(true)
        savedCardPayment.executeSavedCardPayment(token, payload)
    }

    override fun onGPayClicked(
        dojoGPayPayload: DojoGPayPayload,
        dojoPaymentIntent: DojoPaymentIntent
    ) {
        gPayment.executeGPay(dojoGPayPayload, dojoPaymentIntent)
    }
}
