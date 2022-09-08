package tech.dojo.pay.uisdk.domain.entities

data class PaymentIntentDomainEntity(
    val id :String,
    val paymentToken: String,
    val amount: AmountDomainEntity
)

data class AmountDomainEntity(
    val value: String,
    val currencyCode: String
)