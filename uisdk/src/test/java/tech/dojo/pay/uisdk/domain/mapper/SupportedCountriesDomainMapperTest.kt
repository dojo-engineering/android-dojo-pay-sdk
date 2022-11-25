package tech.dojo.pay.uisdk.domain.mapper

import org.junit.Assert
import org.junit.Test
import tech.dojo.pay.uisdk.data.entities.SupportedCountryRaw
import tech.dojo.pay.uisdk.domain.entities.SupportedCountriesDomainEntity


internal class SupportedCountriesDomainMapperTest {
    @Test
    fun `should mao to the correct model`() {
        // arrange
        val raw = SupportedCountryRaw(
            countryCode = "code",
            countryName = "name"
        )
        val expected = SupportedCountriesDomainEntity(
            countryName = "name",
            countryCode = "code",
            isPostalCodeEnabled = false
        )

        // act
        val actual = SupportedCountriesDomainMapper().apply(raw)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun`should map to SupportedCountriesDomainEntity with  isPostalCodeEnabled if country is supported`(){

        // arrange
        val raw = SupportedCountryRaw(
            countryCode = "GB",
            countryName = "UK"
        )
        val expected = SupportedCountriesDomainEntity(
            countryName = "UK",
            countryCode = "GB",
            isPostalCodeEnabled = true
        )

        // act
        val actual = SupportedCountriesDomainMapper().apply(raw)
        // assert
        Assert.assertEquals(expected, actual)
    }

}