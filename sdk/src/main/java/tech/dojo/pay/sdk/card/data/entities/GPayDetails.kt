package tech.dojo.pay.sdk.card.data.entities

import tech.dojo.pay.sdk.card.entities.DojoAddressDetails
import tech.dojo.pay.sdk.card.entities.DojoShippingDetails

data class GPayDetails(
    var token: String?,
    val userEmailAddress: String?,
    val userPhoneNumber: String?,
    val billingAddress: DojoAddressDetails?,
    val shippingDetails: DojoShippingDetails?,
    val metaData: Map<String, String>?,
)