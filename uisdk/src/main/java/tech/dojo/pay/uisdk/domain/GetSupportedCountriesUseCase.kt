package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.supportedcountries.SupportedCountriesRepository

internal class GetSupportedCountriesUseCase(
    private val supportedCountriesRepository: SupportedCountriesRepository
) {
    fun getSupportedCountries() =
        supportedCountriesRepository
            .getSupportedCountries()
}
