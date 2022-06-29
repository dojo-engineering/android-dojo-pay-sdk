package tech.dojo.pay.sdksample

import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoTotalAmountPayload

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

    override fun onGPayClicked(token: String,payload: DojoTotalAmountPayload) {
        gPayment.executeGPay(token,payload)
    }
}
