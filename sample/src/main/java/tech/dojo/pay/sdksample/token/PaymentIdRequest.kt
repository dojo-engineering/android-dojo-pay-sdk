package tech.dojo.pay.sdksample.token

data class PaymentIdBody(
    val amount: Amount,
    val reference: String,
    val description: String,
    val itemLines: List<ItemLines>
)

data class Amount(
    val value: Long,
    val currencyCode: String
)

data class PaymentIdResponse(
    val id: String
)

data class ItemLines(
    val id :String="",
    val quantity:String="",
    val caption:String="",
    val amountTotal:Amount
)