package tech.dojo.pay.sdk.card.entities

import java.io.Serializable

data class DojoGPayPayload(
    val dojoGPayConfig: DojoGPayConfig,
    val email: String? = null,
    val metaData: Map<String, String>? = null
) : Serializable

data class DojoGPayConfig(
    val collectShipping: Boolean = false,
    val collectBilling: Boolean = false,
    val collectEmailAddress: Boolean = false,
    val collectPhoneNumber: Boolean = false,
    val merchantName: String,
    val merchantId: String
) : Serializable