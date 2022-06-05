package tech.dojo.pay.sdksample

import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload

class CardPaymentActivity : CardPaymentBaseActivity() {

    private val cardPayment = DojoSdk.createCardPaymentHandler(this) { result ->
        setProgressIndicatorVisible(false)
        showResult(result)
    }

    override fun onSandboxChecked(isChecked: Boolean) {
        DojoSdk.sandbox = isChecked
    }

    override fun onPayClicked(token: String, payload: DojoCardPaymentPayload) {
        setProgressIndicatorVisible(true)
        cardPayment.executeCardPayment(token, payload)
    }
}