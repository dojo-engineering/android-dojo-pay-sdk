package tech.dojo.pay.sdk.card.data.entities

import androidx.annotation.Keep

@Keep
data class BaseUrlRaw(
    val format: String?,
    val baseUrl: String?,
    val baseClientEventUrl: String?,
)

@Keep
data class BaseUrlResult(
    val baseUrl: String,
    val lastModifiedDate: String?,
)
