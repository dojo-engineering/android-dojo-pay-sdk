package tech.dojo.pay.uisdk.entities

import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import java.io.Serializable

data class DojoPaymentFlowParams(
    val paymentId: String,
    val GPayPayload : DojoGPayPayload?= null
) : Serializable
