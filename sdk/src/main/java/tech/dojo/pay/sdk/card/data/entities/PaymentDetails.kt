package tech.dojo.pay.sdk.card.data.entities

import tech.dojo.pay.sdk.card.entities.DojoAddressDetails
import tech.dojo.pay.sdk.card.entities.DojoShippingDetails

internal data class PaymentDetails(
    val cV2: String?,
    val savePaymentMethod: Boolean? = null,
    val cardName: String? = null,
    val cardNumber: String? = null,
    val expiryDate: String? = null,
    val userEmailAddress: String? = null,
    val userPhoneNumber: String? = null,
    val billingAddress: DojoAddressDetails? = null,
    val shippingDetails: DojoShippingDetails? = null,
    val paymentMethodId: String? = null,
    val metaData: Map<String, String>? = null,
)
