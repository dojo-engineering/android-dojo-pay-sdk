package tech.dojo.pay.sdk.card.entities

import java.io.Serializable

data class DojoCardPaymentParams(
    val token: String,
    val paymentPayload: DojoCardPaymentPayload,
    val sandboxMode: Boolean = false
) : Serializable