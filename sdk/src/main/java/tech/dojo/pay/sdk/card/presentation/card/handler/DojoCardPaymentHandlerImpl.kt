package tech.dojo.pay.sdk.card.presentation.card.handler

import androidx.activity.ComponentActivity
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad

internal class DojoCardPaymentHandlerImpl(
    activity: ComponentActivity,
    onResult: (DojoPaymentResult) -> Unit
) : DojoCardPaymentHandler {

    private val cardPayment =
        activity.registerForActivityResult(DojoCardPaymentResultContract(), onResult)

    override fun executeCardPayment(
        token: String,
        payload: DojoCardPaymentPayLoad.FullCardPaymentPayload
    ) {
        cardPayment.launch(
            DojoCardPaymentParams(token, payload)
        )
    }
}
