package tech.dojo.pay.sdk.card.data.entities

internal data class DecryptGPayTokenResponse(
    val paymentMethod: String,
    val paymentMethodDetails: PaymentMethodDetailsRaw,
    val gatewayMerchantId: String,
    val messageId: String,
    val messageExpiration: String
)

internal data class PaymentMethodDetailsRaw(
    val authMethod: String,
    val pan: String,
    val expirationMonth: String,
    val expirationYear: String
)
