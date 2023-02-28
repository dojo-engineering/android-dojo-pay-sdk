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
        DojoSdk.walletSandBox = isChecked
        DojoSdk.isCardSandBox = isChecked
    }

    override fun onPayClicked(token: String, payload: DojoCardPaymentPayLoad.FullCardPaymentPayload) {
        setProgressIndicatorVisible(true)
        cardPayment.executeCardPayment(token, payload)
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
