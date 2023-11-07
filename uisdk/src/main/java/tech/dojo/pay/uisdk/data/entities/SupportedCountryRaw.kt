package tech.dojo.pay.uisdk.data.entities

import androidx.annotation.Keep

@Keep
internal data class SupportedCountryRaw(
    val countryName: String,
    val countryCode: String,
)
