package tech.dojo.pay.uisdk.data.supportedcountries

import android.content.Context
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.data.entities.SupportedCountryRaw
import java.io.InputStream

internal class SupportedCountriesDataSource {
    fun getSupportedCountries(context: Context): MutableList<SupportedCountryRaw> {
        val stream: InputStream = context.resources.openRawResource(R.raw.countries)
        val supportedCountriesList = mutableListOf<SupportedCountryRaw>()
        csvReader()
            .open(stream) {
                readAllAsSequence()
                    .forEach { row ->
                        supportedCountriesList
                            .add(SupportedCountryRaw(countryName = row[0], countryCode = row[1]))
                    }
            }
        return supportedCountriesList
    }
}
