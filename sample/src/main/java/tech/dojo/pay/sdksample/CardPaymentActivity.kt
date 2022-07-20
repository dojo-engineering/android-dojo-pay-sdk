package tech.dojo.pay.sdksample

import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent

class CardPaymentActivity : CardPaymentBaseActivity() {

    private val cardPayment = DojoSdk.createCardPaymentHandler(this) { result ->
        setProgressIndicatorVisible(false)
        showResult(result)
    }

    private val gPayment = DojoSdk.createGPayHandler(this) { result ->
        showResult(result)
    }

    override fun onSandboxChecked(isChecked: Boolean) {
        DojoSdk.sandbox = isChecked
    }

    override fun onPayClicked(token: String, payload: DojoCardPaymentPayload) {
        setProgressIndicatorVisible(true)
        cardPayment.executeCardPayment(token, payload)
    }

    override fun onGPayClicked(
        dojoGPayPayload: DojoGPayPayload,
        dojoPaymentIntent: DojoPaymentIntent
    ) {
        gPayment.executeGPay(dojoGPayPayload, dojoPaymentIntent)
    }
}
