package tech.dojo.pay.uisdk.domain

import android.content.Context
import tech.dojo.pay.uisdk.data.supportedcountries.SupportedCountriesRepository

internal class GetSupportedCountriesUseCase(
    private val supportedCountriesRepository: SupportedCountriesRepository,
    private val context: Context
) {
    fun getSupportedCountries() =
        supportedCountriesRepository
            .getSupportedCountries(context)
}
