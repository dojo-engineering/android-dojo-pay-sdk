package tech.dojo.pay.sdk.card.data.entities

import java.io.Serializable

internal data class DeviceData(
    val formAction: String,
    val token: String
) : Serializable
