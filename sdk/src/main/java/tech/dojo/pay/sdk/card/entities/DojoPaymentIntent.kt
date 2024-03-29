package tech.dojo.pay.sdk.card.entities

import java.io.Serializable

data class DojoPaymentIntent(
    val token: String,
    val totalAmount: DojoTotalAmount
) : Serializable

data class DojoTotalAmount(
    val amount: Long,
    val currencyCode: String,
) : Serializable
