package tech.dojo.pay.uisdk.domain.entities

import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoVirtualTerminalHandler

internal data class MakeVTPaymentParams(
    val fullCardPaymentPayload: DojoCardPaymentPayLoad.FullCardPaymentPayload,
    val virtualTerminalHandler: DojoVirtualTerminalHandler,
    val paymentId: String,
)
