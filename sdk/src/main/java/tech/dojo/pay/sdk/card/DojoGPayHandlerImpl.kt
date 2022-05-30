package tech.dojo.pay.sdk.card

import androidx.activity.ComponentActivity
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult
import tech.dojo.pay.sdk.card.entities.DojoGPayParams


internal class DojoGPayHandlerImpl(
    activity: ComponentActivity,
    onResult: (DojoCardPaymentResult) -> Unit
) : DojoGPayHandler {

    private val cardPayment = activity.registerForActivityResult(DojoGPayResultContract(), onResult)

    override fun executeGPay(token: String) {
        cardPayment.launch(
            DojoGPayParams(token)
        )
    }
}