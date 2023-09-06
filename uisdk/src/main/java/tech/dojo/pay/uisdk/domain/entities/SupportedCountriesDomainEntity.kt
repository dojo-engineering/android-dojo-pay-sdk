package tech.dojo.pay.uisdk.domain.entities

internal data class SupportedCountriesDomainEntity(
    val countryName: String,
    val countryCode: String,
    val isPostalCodeEnabled: Boolean,
)

internal enum class PostalCodeSupportedCountries(val countryCode: String) {
    UK("GB"),
    USA("US"),
    CANADA("CA"),
    NOT_SUPPORTED(""),
    ;

    companion object {
        fun fromCountryCode(countryCode: String): PostalCodeSupportedCountries =
            values()
                .find { it.countryCode == countryCode } ?: NOT_SUPPORTED
    }
}
