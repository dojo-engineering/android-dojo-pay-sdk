package tech.dojo.pay.sdk.card.entities

import java.io.Serializable

internal data class ThreeDSParams(
    val stepUpUrl: String,
    val jwt: String,
    val md: String
) : Serializable
