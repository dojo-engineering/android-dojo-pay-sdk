package tech.dojo.pay.sdk.card.entities

data class DojoCardPaymentPayload(
    val cardDetails: DojoCardDetails,
    val userEmailAddress: String? = null,
    val userPhoneNumber: String? = null,
    val billingAddress: DojoAddressDetails? = null,
    val shippingDetails: DojoShippingDetails? = null,
    val metaData: Map<String, String>? = null
)