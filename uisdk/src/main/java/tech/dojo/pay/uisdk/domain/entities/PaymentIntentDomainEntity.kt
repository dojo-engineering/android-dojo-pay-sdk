package tech.dojo.pay.uisdk.domain.entities

import tech.dojo.pay.uisdk.data.entities.Amount

data class PaymentIntentDomainEntity(
    val id: String,
    val paymentToken: String,
    val amount: AmountDomainEntity,
    val itemLines: List<ItemLinesDomainEntity>? = null,
    val collectionEmailRequired: Boolean = false,
    val collectionBillingAddressRequired: Boolean = false
)

data class AmountDomainEntity(
    val valueLong: Long,
    val valueString: String,
    val currencyCode: String
)

data class ItemLinesDomainEntity(
    val caption: String,
    val amount: Amount
)
