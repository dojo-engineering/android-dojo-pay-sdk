package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper

import tech.dojo.pay.uisdk.domain.entities.SupportedCountriesDomainEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity

internal class SupportedCountriesViewEntityMapper {

    fun mapToSupportedCountriesViewEntityWithPreSelectedCountry(
        supportedCountriesDomainEntityList: List<SupportedCountriesDomainEntity>,
        preSelectedCountryCode: String?,
    ): List<SupportedCountriesViewEntity> {
        val preSelectedCountryIndex =
            supportedCountriesDomainEntityList.indexOfFirst { it.countryCode == preSelectedCountryCode }
        val mutableSupportedCountriesDomainEntityList = supportedCountriesDomainEntityList.toMutableList()
        if (preSelectedCountryIndex != -1) {
            val preSelectedCountry = mutableSupportedCountriesDomainEntityList.removeAt(preSelectedCountryIndex)
            mutableSupportedCountriesDomainEntityList.add(0, preSelectedCountry)
        }
        return mutableSupportedCountriesDomainEntityList.map { apply(it) }
    }

    fun apply(domainEntity: SupportedCountriesDomainEntity): SupportedCountriesViewEntity {
        return SupportedCountriesViewEntity(
            countryName = domainEntity.countryName,
            countryCode = domainEntity.countryCode,
            isPostalCodeEnabled = domainEntity.isPostalCodeEnabled,
        )
    }
}
