package tech.dojo.pay.sdk.card.presentation.gpay.handler

import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent

interface DojoGPayHandler {

    fun executeGPay(
        GPayPayload: DojoGPayPayload, PaymentIntent: DojoPaymentIntent
    )
}