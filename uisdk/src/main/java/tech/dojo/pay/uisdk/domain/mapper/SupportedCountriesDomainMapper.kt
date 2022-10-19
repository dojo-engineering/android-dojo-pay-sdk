package tech.dojo.pay.uisdk.domain.mapper

import tech.dojo.pay.uisdk.data.entities.SupportedCountryRaw
import tech.dojo.pay.uisdk.domain.entities.PostalCodeSupportedCountries
import tech.dojo.pay.uisdk.domain.entities.SupportedCountriesDomainEntity

internal class SupportedCountriesDomainMapper {
    fun apply(raw: SupportedCountryRaw): SupportedCountriesDomainEntity {
            return SupportedCountriesDomainEntity(
                countryName = raw.countryName,
                countryCode = raw.countryCode,
                isPostalCodeEnabled = PostalCodeSupportedCountries.fromCountryCode(raw.countryCode) != PostalCodeSupportedCountries.NOT_SUPPORTED
            )
    }
}