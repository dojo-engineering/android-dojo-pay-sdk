package tech.dojo.pay.sdk.card.entities

import java.io.Serializable

internal data class DojoCardPaymentParams(
    val token: String,
    val paymentPayload: DojoCardPaymentPayLoad
) : Serializable

internal data class DojoGPayParams(
    val dojoGPayPayload: DojoGPayPayload,
    val dojoPaymentIntent: DojoPaymentIntent
) : Serializable
