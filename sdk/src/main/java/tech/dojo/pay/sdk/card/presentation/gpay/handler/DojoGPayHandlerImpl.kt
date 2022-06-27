package tech.dojo.pay.sdk.card.presentation.gpay.handler

import androidx.activity.ComponentActivity
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.DojoGPayResultContract
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoGPayParams
import tech.dojo.pay.sdk.card.entities.DojoTotalAmountPayload


internal class DojoGPayHandlerImpl(
    activity: ComponentActivity,
    onResult: (DojoPaymentResult) -> Unit
) : DojoGPayHandler {

    private val cardPayment = activity.registerForActivityResult(DojoGPayResultContract(), onResult)

    override fun executeGPay(token: String, payload: DojoTotalAmountPayload) {
        cardPayment.launch(
            DojoGPayParams(token, payload)
        )
    }
}