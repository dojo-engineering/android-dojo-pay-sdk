package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.SupportedCountriesRepository

internal class GetSupportedCountriesUseCase(
    private val supportedCountriesRepository: SupportedCountriesRepository = SupportedCountriesRepository()
) {
    fun getSupportedCountries() = supportedCountriesRepository.getSupportedCountries()
}
