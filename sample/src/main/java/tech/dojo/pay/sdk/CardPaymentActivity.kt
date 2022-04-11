package tech.dojo.pay.sdk

import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams

class CardPaymentActivity : CardPaymentBaseActivity() {

    private val cardPayment = registerForActivityResult(DojoCardPaymentResultContract()) { result ->
        setProgressIndicatorVisible(false)
        showResult(result)
    }

    override fun onPayClicked(params: DojoCardPaymentParams) {
        setProgressIndicatorVisible(true)
        cardPayment.launch(params)
    }
}