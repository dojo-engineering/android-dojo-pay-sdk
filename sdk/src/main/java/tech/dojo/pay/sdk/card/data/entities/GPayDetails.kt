package tech.dojo.pay.sdk.card.data.entities

import androidx.annotation.Keep
import tech.dojo.pay.sdk.card.entities.GooglePayAddressDetails
@Keep
data class GPayDetails(
    var token: String?,
    val email: String?,
    val phoneNumber: String?,
    val billingContact: GooglePayAddressDetails?,
    val shippingContact: GooglePayAddressDetails?,
    val metaData: Map<String, String>? = null,
)
