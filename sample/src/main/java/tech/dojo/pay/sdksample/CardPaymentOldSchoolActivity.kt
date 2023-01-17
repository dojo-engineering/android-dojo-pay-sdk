package tech.dojo.pay.sdksample

import android.content.Intent
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent

class CardPaymentOldSchoolActivity : CardPaymentBaseActivity() {

    override fun onSandboxChecked(isChecked: Boolean) {
        DojoSdk.walletSandBox = isChecked
    }

    override fun onPayClicked(token: String, payload: DojoCardPaymentPayLoad.FullCardPaymentPayload) {
        setProgressIndicatorVisible(true)
        DojoSdk.startCardPayment(this, token, payload)
    }

    override fun onPaySavedCardClicked(
        token: String,
        payload: DojoCardPaymentPayLoad.SavedCardPaymentPayLoad
    ) {
        setProgressIndicatorVisible(true)
        DojoSdk.startSavedCardPayment(this, token, payload)
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
        val savedCardResult = DojoSdk.parseSavedCardPaymentResult(requestCode, resultCode, data)
        val gPayResult = DojoSdk.parseGPayPaymentResult(requestCode, resultCode, data)
        setProgressIndicatorVisible(false)
        if (result != null) showResult(result)
        if (savedCardResult != null) showResult(savedCardResult)
        if (gPayResult != null) showResult(gPayResult)
    }
}
