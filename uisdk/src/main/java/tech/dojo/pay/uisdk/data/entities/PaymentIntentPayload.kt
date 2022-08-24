package tech.dojo.pay.uisdk.data.entities

data class PaymentIntentPayload(
    val id: String,
    val captureMode: String,
    val clientSessionSecret: String,
    val clientSessionSecretExpirationDate: String,
    val status: String,
    val paymentMethods: List<String>,
    val amount: Amount,
    val totalAmount: Amount,
    val refundedAmount: Long,
    val createdAt: String,
    val updatedAt: String,
    val reference: String,
    val description: String,
    val config: Config,
    val metadata: Metadata
)

data class Amount(
    val value: Long,
    val currencyCode: String
)

data class Config(
    val tradingName: String,
    val branding: Branding
)

data class Branding(
    val logoURL: String,
    val faviconURL: String
)

data class Metadata(
    val locationID: String
)
