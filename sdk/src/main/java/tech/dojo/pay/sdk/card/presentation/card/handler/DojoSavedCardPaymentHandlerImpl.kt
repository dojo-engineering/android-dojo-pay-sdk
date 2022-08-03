package tech.dojo.pay.sdk.card.presentation.card.handler

import androidx.activity.ComponentActivity
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad

class DojoSavedCardPaymentHandlerImpl(
    activity: ComponentActivity,
    onResult: (DojoPaymentResult) -> Unit
) : DojoSavedCardPaymentHandler {
    private val savedCardPayment =
        activity.registerForActivityResult(DojoCardPaymentResultContract(), onResult)

    override fun executeSavedCardPayment(
        token: String,
        payload: DojoCardPaymentPayLoad.SavedCardPaymentPayLoad
    ) {
        savedCardPayment.launch(
            DojoCardPaymentParams(token, payload)
        )
    }
}
