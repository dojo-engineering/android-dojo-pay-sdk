package tech.dojo.pay.uisdk.domain.entities

import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandler

internal data class MakeCardPaymentParams(
    val fullCardPaymentPayload: DojoCardPaymentPayLoad.FullCardPaymentPayload,
    val dojoCardPaymentHandler: DojoCardPaymentHandler,
    val paymentId: String,
)
