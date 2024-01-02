package tech.dojo.pay.uisdk.domain.entities

import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.entities.DojoTotalAmount
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler

internal data class MakeGpayPaymentParams(
    val dojoGPayConfig: DojoGPayConfig,
    val dojoTotalAmount: DojoTotalAmount,
    val gpayPaymentHandler: DojoGPayHandler,
    val paymentId: String,
)
