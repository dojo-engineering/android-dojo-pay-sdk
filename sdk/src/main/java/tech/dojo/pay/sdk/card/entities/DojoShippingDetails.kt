package tech.dojo.pay.sdk.card.entities

import java.io.Serializable

data class DojoShippingDetails(
    val name: String? = null,
    val address: DojoAddressDetails? = null
) : Serializable