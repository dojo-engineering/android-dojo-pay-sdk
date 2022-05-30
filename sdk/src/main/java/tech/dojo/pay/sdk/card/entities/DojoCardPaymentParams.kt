package tech.dojo.pay.sdk.card.entities

import java.io.Serializable

internal data class DojoCardPaymentParams(
    val token: String,
    val paymentPayload: DojoCardPaymentPayload
) : Serializable

internal data class DojoGPayParams(
    val token: String
) : Serializable