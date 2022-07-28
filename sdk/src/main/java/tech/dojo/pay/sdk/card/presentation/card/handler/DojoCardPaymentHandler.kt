package tech.dojo.pay.sdk.card.presentation.card.handler

import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad

interface DojoCardPaymentHandler {
    fun executeCardPayment(token: String, payload: DojoCardPaymentPayLoad.NormalCardPaymentPayload)
}
