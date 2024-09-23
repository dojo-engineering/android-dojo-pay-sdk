package tech.dojo.pay.uisdk.domain.entities

import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.sdk.card.entities.WalletSchemes

internal data class PaymentIntentDomainEntity(
    val id: String,
    val paymentToken: String,
    val totalAmount: AmountDomainEntity,
    val supportedCardsSchemes: List<CardsSchemes>,
    val supportedWalletSchemes: List<WalletSchemes> = emptyList(),
    val itemLines: List<ItemLinesDomainEntity>? = null,
    val customerId: String? = null,
    val customerEmailAddress: String? = null,
    val collectionEmailRequired: Boolean = false,
    val isVirtualTerminalPayment: Boolean = false,
    val collectionBillingAddressRequired: Boolean = false,
    val isPreAuthPayment: Boolean = false,
    val orderId: String = "",
    val collectionShippingAddressRequired: Boolean = false,
    val isSetUpIntentPayment: Boolean = false,
    val merchantName: String = "",
    val isPaymentAlreadyCollected: Boolean = false,
    val billingAddress: BillingAddressDomainEntity? = null,
    val shippingDetails: ShippingDetailsDomainEntity? = null,
    val urls: DojoUrls = DojoUrls.Uk(),
)

data class AmountDomainEntity(
    val valueLong: Long,
    val valueString: String,
    val currencyCode: String,
)

internal data class ItemLinesDomainEntity(
    val caption: String,
    val amount: ItemLinesAmountDomainEntity,
)

internal data class ItemLinesAmountDomainEntity(
    val value: Long,
    val currencyCode: String,
)

internal enum class PaymentIntentStatusDomainEntity(val status: String) {
    CREATED("Created"),
    AUTHORIZED("Authorized"),
    CAPTURED("Captured"),
    REVERSED("Reversed"),
    REFUNDED("Refunded"),
    CANCELED("Canceled"),
    NOT_SUPPORTED(""),
    ;

    companion object {
        fun fromStatus(status: String): PaymentIntentStatusDomainEntity =
            PaymentIntentStatusDomainEntity.values()
                .find { it.status == status } ?: NOT_SUPPORTED
    }
}

data class BillingAddressDomainEntity(
    val postcode: String?,
    val countryCode: String?,
    val city: String?,
)

data class ShippingDetailsDomainEntity(
    val name: String?,
    val deliveryNotes: String?,
)
