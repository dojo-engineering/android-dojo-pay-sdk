package tech.dojo.pay.sdksample

import android.content.Intent
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent

class CardPaymentOldSchoolActivity : CardPaymentBaseActivity() {

    override fun onSandboxChecked(isChecked: Boolean) {
        DojoSdk.sandbox = isChecked
    }

    override fun onPayClicked(token: String, payload: DojoCardPaymentPayload) {
        setProgressIndicatorVisible(true)
        DojoSdk.startCardPayment(this, token, payload)
    }

    override fun onGPayClicked(
        dojoGPayPayload: DojoGPayPayload,
        dojoPaymentIntent: DojoPaymentIntent
    ) {
        DojoSdk.startGPay(this, dojoGPayPayload, dojoPaymentIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = DojoSdk.parseCardPaymentResult(requestCode, resultCode, data)
        setProgressIndicatorVisible(false)
        if (result != null) showResult(result)
    }
}
