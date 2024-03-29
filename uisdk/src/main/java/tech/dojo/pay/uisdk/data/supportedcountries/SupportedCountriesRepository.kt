package tech.dojo.pay.uisdk.data.supportedcountries

import tech.dojo.pay.uisdk.domain.entities.SupportedCountriesDomainEntity
import tech.dojo.pay.uisdk.domain.mapper.SupportedCountriesDomainMapper

internal class SupportedCountriesRepository(
    private val dataSource: SupportedCountriesDataSource,
    private val domainMapper: SupportedCountriesDomainMapper = SupportedCountriesDomainMapper()
) {
    fun getSupportedCountries(): List<SupportedCountriesDomainEntity> =
        dataSource
            .getSupportedCountries()
            .map { domainMapper.apply(it) }
}
