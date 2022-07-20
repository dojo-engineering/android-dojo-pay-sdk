package tech.dojo.pay.sdk.card.entities

import java.io.Serializable

data class GooglePayAddressDetails(
    val address1: String? = null,
    val address2: String? = null,
    val address3: String? = null,
    val administrativeArea: String? = null,
    val countryCode: String? = null,
    val locality: String? = null,
    val name: String? = null,
    val phoneNumber: String? = null,
    val postcode: String? = null,
    val sortingCode: String? = null,
) : Serializable