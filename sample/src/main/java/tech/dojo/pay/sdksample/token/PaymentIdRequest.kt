package tech.dojo.pay.sdksample.token

data class PaymentIdBody (
    val amount: Amount,
    val reference: String,
    val description: String
)

data class Amount (
    val value: Long,
    val currencyCode: String
)

data class PaymentIdResponse(
    val id:String
)