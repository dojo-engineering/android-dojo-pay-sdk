package tech.dojo.pay.uisdk.domain.entities

data class PaymentIntentDomainEntity(
    val paymentToken: String,
    val amount: AmountDomainEntity
)

data class AmountDomainEntity(
    val value: Long,
    val currencyCode: String
)
