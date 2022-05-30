package tech.dojo.pay.sdksample

import android.content.Intent
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload

class CardPaymentOldSchoolActivity : CardPaymentBaseActivity() {

    override fun onSandboxChecked(isChecked: Boolean) {
        DojoSdk.sandbox = isChecked
    }

    override fun onPayClicked(token: String, payload: DojoCardPaymentPayload) {
        setProgressIndicatorVisible(true)
        DojoSdk.startCardPayment(this, token, payload)
    }

    override fun onGPayClicked(token: String) {
        DojoSdk.startGPay(this, token)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = DojoSdk.parseCardPaymentResult(requestCode, resultCode, data)
        setProgressIndicatorVisible(false)
        if (result != null) showResult(result)
    }
}
