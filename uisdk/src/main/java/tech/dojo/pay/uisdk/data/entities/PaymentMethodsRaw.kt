package tech.dojo.pay.uisdk.data.entities

import tech.dojo.pay.sdk.card.entities.CardsSchemes

internal data class PaymentMethodsRaw(
    val customerId: String,
    val merchantId: String,
    val savedPaymentMethods: List<SavedPaymentMethods>,
    val supportedPaymentMethods: SupportedPaymentMethods
)

internal data class SavedPaymentMethods(
    val id: String,
    val cardDetails: CardDetails
)

internal data class CardDetails(
    val pan: String,
    val expiryDate: String,
    val scheme: CardsSchemes
)
