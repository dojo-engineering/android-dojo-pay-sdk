package tech.dojo.pay.uisdk.data.entities

import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.sdk.card.entities.WalletSchemes

internal data class PaymentIntentPayload(
    val id: String? = null,
    val captureMode: String? = null,
    val billingAddress: BillingAddress? = null,
    val shippingDetails: ShippingDetails? = null,
    val transactionSource: String? = null,
    val clientSessionSecret: String? = null,
    val clientSessionSecretExpirationDate: String? = null,
    val status: String? = null,
    val paymentMethods: List<String>? = null,
    val amount: Amount? = null,
    val intendedAmount: Amount? = null,
    val tipsAmount: Amount? = null,
    val requestedAmount: Amount? = null,
    val customer: Customer? = null,
    val totalAmount: Amount? = null,
    val refundedAmount: Long? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val reference: String? = null,
    val description: String? = null,
    val merchantConfig: MerchantConfig? = null,
    val paymentSource: String? = null,
    val config: Config? = null,
    val itemLines: List<ItemLines>? = null,
    val merchantInitiatedTransactionType: String? = null,
    val metadata: Metadata? = null,
)

internal data class ItemLines(
    val caption: String?,
    val amountTotal: Amount?,
)

internal data class Amount(
    val value: Long?,
    val currencyCode: String?,
)

internal data class Config(
    val tradingName: String?,
    val branding: Branding?,
    val customerEmail: CollectCustomerEmail?,
    val billingAddress: CollectBillingAddress?,
    val shippingDetails: CollectShippingAddress?,
)

internal data class CollectCustomerEmail(
    val collectionRequired: Boolean?,
)

data class CollectBillingAddress(
    val collectionRequired: Boolean?,
)

data class CollectShippingAddress(
    val collectionRequired: Boolean?,
)

internal data class Branding(
    val logoURL: String?,
    val faviconURL: String?,
)

internal data class MerchantConfig(
    val supportedPaymentMethods: SupportedPaymentMethods? = null,
)

internal data class SupportedPaymentMethods(
    val cardSchemes: List<CardsSchemes>? = null,
    val wallets: List<WalletSchemes>? = null,
)

data class Metadata(val locationID: String?)

data class Customer(
    val id: String?,
    val emailAddress: String?,
)

data class BillingAddress(
    val postcode: String?,
    val countryCode: String,
    val city: String?,
)

data class ShippingDetails(
    val name: String?,
    val deliveryNotes: String?,
)
