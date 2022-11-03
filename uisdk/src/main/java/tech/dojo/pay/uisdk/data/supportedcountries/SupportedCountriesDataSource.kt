package tech.dojo.pay.uisdk.data.supportedcountries

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import tech.dojo.pay.uisdk.data.entities.SupportedCountryRaw
import java.io.InputStream

internal class SupportedCountriesDataSource {
    fun getSupportedCountries(): MutableList<SupportedCountryRaw> {
        val stream: InputStream? =
            this.javaClass.classLoader?.getResourceAsStream("res/raw/countries.csv")
        val supportedCountriesList = mutableListOf<SupportedCountryRaw>()
        stream?.let {
            csvReader().open(it) {
                readAllAsSequence().forEach { row ->
                    supportedCountriesList.add(
                        SupportedCountryRaw(
                            countryName = row[0],
                            countryCode = row[1]
                        )
                    )
                }
            }
        }

        return supportedCountriesList
    }
}
