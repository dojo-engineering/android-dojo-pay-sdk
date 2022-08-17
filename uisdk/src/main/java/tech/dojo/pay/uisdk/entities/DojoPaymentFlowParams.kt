package tech.dojo.pay.uisdk.entities

import java.io.Serializable

data class DojoPaymentFlowParams(
    val paymentToken: String,
    val dojoThemeSettings: DojoThemeSettings? = null
) : Serializable
