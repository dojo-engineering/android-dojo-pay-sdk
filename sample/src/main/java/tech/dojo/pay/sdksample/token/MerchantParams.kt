package tech.dojo.pay.sdksample.token

class MerchantParams(
    val gatewayUsername: String,
    val currencyCode: String,
    val amount: String,
    val transactionType: String,
    val orderId: String,
    val orderDescription: String
)
