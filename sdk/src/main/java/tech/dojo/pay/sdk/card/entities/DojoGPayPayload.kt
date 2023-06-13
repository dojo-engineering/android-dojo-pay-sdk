package tech.dojo.pay.sdk.card.entities

import java.io.Serializable

data class DojoGPayPayload @JvmOverloads constructor(
    val dojoGPayConfig: DojoGPayConfig,
    val email: String? = null,
    val metaData: Map<String, String>? = null
) : Serializable

data class DojoGPayConfig @JvmOverloads constructor(
    val collectShipping: Boolean = false,
    val allowedCountryCodesForShipping: List<String>? = null,
    val collectBilling: Boolean = false,
    val collectEmailAddress: Boolean = false,
    val collectPhoneNumber: Boolean = false,
    val merchantName: String,
    val merchantId: String,
    val gatewayMerchantId: String,
    val allowedCardNetworks: List<CardsSchemes> = emptyList()
) : Serializable
