package tech.dojo.pay.sdk.card

import androidx.activity.ComponentActivity
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.entities.DojoGPayParams


internal class DojoGPayHandlerImpl(
    activity: ComponentActivity,
    onResult: (DojoPaymentResult) -> Unit
) : DojoGPayHandler {

    private val cardPayment = activity.registerForActivityResult(DojoGPayResultContract(), onResult)

    override fun executeGPay(token: String) {
        cardPayment.launch(
            DojoGPayParams(token)
        )
    }
}