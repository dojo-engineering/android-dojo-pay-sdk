package tech.dojo.pay.sdk.card.entities

import java.io.Serializable

data class DojoAddressDetails(
    val address1: String? = null,
    val address2: String? = null,
    val address3: String? = null,
    val address4: String? = null,
    val city: String? = null,
    val state: String? = null,
    val postcode: String? = null,
    val countryCode: String? = null
) : Serializable
