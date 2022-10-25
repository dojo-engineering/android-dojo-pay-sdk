package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper

import tech.dojo.pay.uisdk.domain.entities.SupportedCountriesDomainEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity

internal class SupportedCountriesViewEntityMapper {
    fun apply(domainEntity: SupportedCountriesDomainEntity): SupportedCountriesViewEntity {
        return SupportedCountriesViewEntity(
            countryName = domainEntity.countryName,
            countryCode = domainEntity.countryCode,
            isPostalCodeEnabled = domainEntity.isPostalCodeEnabled
        )
    }
}
