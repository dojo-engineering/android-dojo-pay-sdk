package tech.dojo.pay.uisdk.data.entities

import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.sdk.card.entities.WalletSchemes

data class PaymentIntentPayload(
    val id: String? = null,
    val captureMode: String? = null,
    val transactionSource: String? = null,
    val clientSessionSecret: String? = null,
    val clientSessionSecretExpirationDate: String? = null,
    val status: String? = null,
    val paymentMethods: List<String>? = null,
    val amount: Amount? = null,
    val tipsAmount: Amount? = null,
    val requestedAmount: Amount? = null,
    val totalAmount: Amount? = null,
    val refundedAmount: Long? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val reference: String? = null,
    val description: String? = null,
    val merchantConfig: MerchantConfig? = null,
    val config: Config? = null,
    val itemLines: List<ItemLines>? = null,
    val metadata: Metadata? = null
)

data class ItemLines(
    val caption: String,
    val amountTotal: Amount
)

data class Amount(
    val value: Long,
    val currencyCode: String
)

data class Config(
    val tradingName: String,
    val branding: Branding,
    val customerEmail: CustomerEmail,
    val billingAddress: BillingAddress
)

data class CustomerEmail(
    val collectionRequired: Boolean
)

data class BillingAddress(
    val collectionRequired: Boolean
)

data class Branding(
    val logoURL: String,
    val faviconURL: String
)

data class MerchantConfig(
    val supportedPaymentMethods: SupportedPaymentMethods?
)

data class SupportedPaymentMethods(
    val cardSchemes: List<CardsSchemes>?,
    val wallets: List<WalletSchemes>? = null
)

data class Metadata(
    val locationID: String
)
