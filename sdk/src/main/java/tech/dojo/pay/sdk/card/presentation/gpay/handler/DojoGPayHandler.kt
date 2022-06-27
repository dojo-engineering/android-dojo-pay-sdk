package tech.dojo.pay.sdk.card.presentation.gpay.handler

import tech.dojo.pay.sdk.card.entities.DojoTotalAmountPayload

interface DojoGPayHandler {

    fun executeGPay(token: String, payload: DojoTotalAmountPayload)
}