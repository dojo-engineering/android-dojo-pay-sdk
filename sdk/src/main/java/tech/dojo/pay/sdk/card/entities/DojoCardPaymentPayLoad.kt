package tech.dojo.pay.sdk.card.entities

import java.io.Serializable

sealed class DojoCardPaymentPayLoad : Serializable {
    data class FullCardPaymentPayload(
        val cardDetails: DojoCardDetails,
        val savePaymentMethod: Boolean? = null,
        val userEmailAddress: String? = null,
        val userPhoneNumber: String? = null,
        val billingAddress: DojoAddressDetails? = null,
        val shippingDetails: DojoShippingDetails? = null,
        val metaData: Map<String, String>? = null
    ) : Serializable, DojoCardPaymentPayLoad()

    data class SavedCardPaymentPayLoad(
        val cv2: String,
        val paymentMethodId: String,
        val userEmailAddress: String? = null,
        val userPhoneNumber: String? = null,
        val shippingDetails: DojoShippingDetails? = null,
        val metaData: Map<String, String>? = null
    ) : Serializable, DojoCardPaymentPayLoad()
}
