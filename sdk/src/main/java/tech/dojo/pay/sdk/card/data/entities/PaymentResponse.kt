package tech.dojo.pay.sdk.card.data.entities

class PaymentResponse(
    val statusCode: Int,
    val md: String? = null,
    val stepUpUrl: String? = null,
    val jwt: String? = null
)