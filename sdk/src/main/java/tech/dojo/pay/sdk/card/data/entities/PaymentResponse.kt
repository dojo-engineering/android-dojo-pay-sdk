package tech.dojo.pay.sdk.card.data.entities

class PaymentResponse(
    val statusCode: Int,
    val authCode: String?,
    val acsUrl: String?,
    val md: String?,
    val message: String?,
    val stepUpUrl: String?,
    val jwt: String?
)