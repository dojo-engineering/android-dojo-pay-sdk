package tech.dojo.pay.uisdk.data.entities

import androidx.annotation.Keep
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.sdk.card.entities.WalletSchemes

@Keep
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

@Keep
internal data class ItemLines(
    val caption: String?,
    val amountTotal: Amount?,
)

@Keep
internal data class Amount(
    val value: Long?,
    val currencyCode: String?,
)

@Keep
internal data class Config(
    val tradingName: String?,
    val branding: Branding?,
    val customerEmail: CollectCustomerEmail?,
    val billingAddress: CollectBillingAddress?,
    val shippingDetails: CollectShippingAddress?,
)

@Keep
internal data class CollectCustomerEmail(
    val collectionRequired: Boolean?,
)

@Keep
data class CollectBillingAddress(
    val collectionRequired: Boolean?,
)

@Keep
data class CollectShippingAddress(
    val collectionRequired: Boolean?,
)

@Keep
internal data class Branding(
    val logoURL: String?,
    val faviconURL: String?,
)

@Keep
internal data class MerchantConfig(
    val supportedPaymentMethods: SupportedPaymentMethods? = null,
)

@Keep
internal data class SupportedPaymentMethods(
    val cardSchemes: List<CardsSchemes>? = null,
    val wallets: List<WalletSchemes>? = null,
)

@Keep
data class Metadata(val locationID: String?)

@Keep
data class Customer(
    val id: String?,
    val emailAddress: String?,
)

@Keep
data class BillingAddress(
    val postcode: String?,
    val countryCode: String,
    val city: String?,
)

@Keep
data class ShippingDetails(
    val name: String?,
    val deliveryNotes: String?,
)
