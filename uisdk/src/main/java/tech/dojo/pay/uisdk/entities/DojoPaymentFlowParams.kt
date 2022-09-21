package tech.dojo.pay.uisdk.entities

import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import java.io.Serializable

data class DojoPaymentFlowParams(
    val paymentId: String,
    val GPayConfig : DojoGPayConfig?= null
) : Serializable
