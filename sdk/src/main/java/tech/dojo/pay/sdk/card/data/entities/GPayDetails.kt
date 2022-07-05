package tech.dojo.pay.sdk.card.data.entities

import tech.dojo.pay.sdk.card.entities.GooglePayAddressDetails

data class GPayDetails(
    var token: String?,
    val email: String?,
    val phoneNumber: String?,
    val billingAddress: GooglePayAddressDetails?,
    val shippingDetails: GooglePayAddressDetails?,
    val metaData: Map<String, String>?,
)