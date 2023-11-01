package tech.dojo.pay.sdk.card.data.entities

import androidx.annotation.Keep
import tech.dojo.pay.sdk.card.entities.DojoAddressDetails
import tech.dojo.pay.sdk.card.entities.DojoShippingDetails
@Keep
internal data class PaymentDetails(
    val cV2: String?,
    val savePaymentMethod: Boolean? = null, // this is for the SetUp Intent payments it needs to be true
    val mitConsentGiven: Boolean? = null,
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
