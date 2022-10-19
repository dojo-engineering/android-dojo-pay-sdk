package tech.dojo.pay.sdksample.token

import tech.dojo.pay.uisdk.data.entities.BillingAddress
import tech.dojo.pay.uisdk.data.entities.CustomerEmail

data class PaymentIdBody(
    val amount: Amount,
    val reference: String,
    val description: String,
    val config: Config? = null,
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
    val id: String = "",
    val quantity: String = "",
    val caption: String = "",
    val amountTotal: Amount
)
data class Config(
    val customerEmail: CustomerEmail,
    val billingAddress: BillingAddress
)
data class CustomerEmail(
    val collectionRequired: Boolean
)

data class BillingAddress(
    val collectionRequired: Boolean
)