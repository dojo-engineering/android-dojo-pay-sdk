package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import tech.dojo.pay.uisdk.domain.entities.SupportedCountriesDomainEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity

class SupportedCountriesViewEntityMapperTest {

    private lateinit var mapper: SupportedCountriesViewEntityMapper

    @Before
    fun setUp() {
        mapper = SupportedCountriesViewEntityMapper()
    }

    @Test
    fun `when calling mapToSupportedCountriesViewEntityWithPreSelectedCountry with preSelectedCountryCode contained in the list should move preSelectedCountry to the beginning`() {
        // Arrange
        val preSelectedCountryCode = "US"
        val countryList = listOf(
            SupportedCountriesDomainEntity(
                countryName = "Canada",
                countryCode = "CA",
                isPostalCodeEnabled = false,
            ),
            SupportedCountriesDomainEntity(
                countryName = "United Kingdom",
                countryCode = "UK",
                isPostalCodeEnabled = true,
            ),
            SupportedCountriesDomainEntity(
                countryName = "United States",
                countryCode = "US",
                isPostalCodeEnabled = true,
            ),
        )
        val expected = listOf(
            SupportedCountriesViewEntity(
                countryName = "United States",
                countryCode = "US",
                isPostalCodeEnabled = true,
            ),
            SupportedCountriesViewEntity(
                countryName = "Canada",
                countryCode = "CA",
                isPostalCodeEnabled = false,
            ),
            SupportedCountriesViewEntity(
                countryName = "United Kingdom",
                countryCode = "UK",
                isPostalCodeEnabled = true,
            ),
        )

        // Act
        val actual = mapper.mapToSupportedCountriesViewEntityWithPreSelectedCountry(
            countryList,
            preSelectedCountryCode,
        )

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when calling mapToSupportedCountriesViewEntityWithPreSelectedCountry with preSelectedCountryCode not contained in the list should not change order if preSelectedCountry is not in the list`() {
        // Arrange
        val preSelectedCountryCode = "AU"
        val countryList = listOf(
            SupportedCountriesDomainEntity(
                countryName = "Canada",
                countryCode = "CA",
                isPostalCodeEnabled = false,
            ),
            SupportedCountriesDomainEntity(
                countryName = "United Kingdom",
                countryCode = "UK",
                isPostalCodeEnabled = true,
            ),
            SupportedCountriesDomainEntity(
                countryName = "United States",
                countryCode = "US",
                isPostalCodeEnabled = true,
            ),
        )

        val expected = listOf(
            SupportedCountriesViewEntity(
                countryName = "Canada",
                countryCode = "CA",
                isPostalCodeEnabled = false,
            ),
            SupportedCountriesViewEntity(
                countryName = "United Kingdom",
                countryCode = "UK",
                isPostalCodeEnabled = true,
            ),
            SupportedCountriesViewEntity(
                countryName = "United States",
                countryCode = "US",
                isPostalCodeEnabled = true,
            ),
        )
        // Act
        val actual = mapper.mapToSupportedCountriesViewEntityWithPreSelectedCountry(
            countryList,
            preSelectedCountryCode,
        )

        // Assert
        assertEquals(expected, actual)
    }

    @Test
    fun `when calling mapToSupportedCountriesViewEntityWithPreSelectedCountry with empty list should return empty lost `() {
        // Arrange
        val preSelectedCountryCode = "US"
        val countryList = emptyList<SupportedCountriesDomainEntity>()

        // Act
        val result = mapper.mapToSupportedCountriesViewEntityWithPreSelectedCountry(
            countryList,
            preSelectedCountryCode,
        )

        // Assert
        assertEquals(emptyList<SupportedCountriesViewEntity>(), result)
    }

    @Test
    fun `when calling apply should map SupportedCountriesDomainEntity to SupportedCountriesViewEntity`() {
        // Arrange
        val domainEntity = SupportedCountriesDomainEntity("US", "United States", true)

        // Act
        val result = mapper.apply(domainEntity)

        // Assert
        assertEquals(domainEntity.countryCode, result.countryCode)
        assertEquals(domainEntity.countryName, result.countryName)
        assertEquals(domainEntity.isPostalCodeEnabled, result.isPostalCodeEnabled)
    }
}
