package tech.dojo.pay.uisdk.data.supportedcountries

import android.content.Context
import tech.dojo.pay.uisdk.domain.entities.SupportedCountriesDomainEntity
import tech.dojo.pay.uisdk.domain.mapper.SupportedCountriesDomainMapper

internal class SupportedCountriesRepository(
    private val dataSource: SupportedCountriesDataSource = SupportedCountriesDataSource(),
    private val domainMapper: SupportedCountriesDomainMapper = SupportedCountriesDomainMapper()
) {
    fun getSupportedCountries(context: Context): List<SupportedCountriesDomainEntity> =
        dataSource
            .getSupportedCountries(context)
            .map { domainMapper.apply(it) }
}
