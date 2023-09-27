package tech.dojo.pay.sdk.card.data.entities

import androidx.annotation.Keep
import java.io.Serializable
@Keep
internal data class DeviceData(
    val formAction: String,
    val token: String,
) : Serializable
