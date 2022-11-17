package tech.dojo.pay.uisdk.domain.entities

import tech.dojo.pay.sdk.card.entities.CardsSchemes

internal data class PaymentMethodsDomainEntity(
    val items: List<PaymentMethodsDomainEntityItem>
)

internal data class PaymentMethodsDomainEntityItem(
    val id: String,
    val pan: String,
    val expiryDate: String,
    val scheme: CardsSchemes
)
