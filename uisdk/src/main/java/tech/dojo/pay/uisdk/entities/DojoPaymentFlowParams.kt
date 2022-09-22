package tech.dojo.pay.uisdk.entities

import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import java.io.Serializable

data class DojoPaymentFlowParams(
    val paymentId: String,
    val GPayConfig: DojoGPayConfig? = null
) : Serializable
