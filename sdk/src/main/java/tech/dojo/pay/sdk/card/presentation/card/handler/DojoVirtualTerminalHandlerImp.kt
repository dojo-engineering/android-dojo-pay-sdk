package tech.dojo.pay.sdk.card.presentation.card.handler

import androidx.activity.ComponentActivity
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.DojoCardVirtualTerminalResultContract
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad

class DojoVirtualTerminalHandlerImp(
    activity: ComponentActivity,
    onResult: (DojoPaymentResult) -> Unit
) : DojoVirtualTerminalHandler {
    private val virtualTerminalPayment =
        activity.registerForActivityResult(DojoCardVirtualTerminalResultContract(), onResult)
    override fun executeVirtualTerminalPayment(
        token: String,
        payload: DojoCardPaymentPayLoad.FullCardPaymentPayload
    ) {
        virtualTerminalPayment.launch(
            DojoCardPaymentParams(token, payload)
        )
    }
}
