package tech.dojo.pay.sdk.token

class MerchantParams(
    val merchantUrl: String,
    val currencyCode: String,
    val amount: String,
    val transactionType: String,
    val orderId: String,
    val orderDescription: String
)