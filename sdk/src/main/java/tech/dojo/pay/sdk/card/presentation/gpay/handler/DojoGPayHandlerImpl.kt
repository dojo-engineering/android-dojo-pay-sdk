package tech.dojo.pay.sdk.card.presentation.gpay.handler

import androidx.activity.ComponentActivity
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.DojoGPayResultContract
import tech.dojo.pay.sdk.card.entities.DojoGPayParams
import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent


internal class DojoGPayHandlerImpl(
    activity: ComponentActivity,
    onResult: (DojoPaymentResult) -> Unit
) : DojoGPayHandler {

    private val cardPayment = activity.registerForActivityResult(DojoGPayResultContract(), onResult)

    override fun executeGPay(
        GPayPayload: DojoGPayPayload, PaymentIntent: DojoPaymentIntent
    ) {
        cardPayment.launch(
            DojoGPayParams(GPayPayload, PaymentIntent)
        )
    }
}