package tech.dojo.pay.sdk.card

import androidx.activity.ComponentActivity
import tech.dojo.pay.sdk.DojoCardPaymentHandler
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult

internal class DojoCardPaymentHandlerImpl(
    activity: ComponentActivity,
    onResult: (DojoCardPaymentResult) -> Unit
) : DojoCardPaymentHandler {

    private val cardPayment = activity.registerForActivityResult(DojoCardPaymentResultContract(), onResult)

    override fun executeCardPayment(token: String, payload: DojoCardPaymentPayload) {
        cardPayment.launch(
            DojoCardPaymentParams(token, payload)
        )
    }
}