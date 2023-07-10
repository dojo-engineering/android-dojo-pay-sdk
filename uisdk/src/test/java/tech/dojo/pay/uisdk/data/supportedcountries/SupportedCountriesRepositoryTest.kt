package tech.dojo.pay.uisdk.data.supportedcountries

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import tech.dojo.pay.uisdk.data.entities.SupportedCountryRaw
import tech.dojo.pay.uisdk.domain.entities.SupportedCountriesDomainEntity
import tech.dojo.pay.uisdk.domain.mapper.SupportedCountriesDomainMapper

class SupportedCountriesRepositoryTest {

    private val dataSource = mockk<SupportedCountriesDataSource>()
    private val domainMapper = mockk<SupportedCountriesDomainMapper>()
    private val context: Context = mockk()
    private lateinit var repository: SupportedCountriesRepository

    @Before
    fun setup() {
        repository = SupportedCountriesRepository(dataSource, domainMapper)
    }

    @Test
    fun `test getSupportedCountries returns empty list`() {
        every { dataSource.getSupportedCountries(context) } returns mutableListOf()
        every { domainMapper.apply(any()) } returns SupportedCountriesDomainEntity("", "", false)

        val result = repository.getSupportedCountries(context)

        assert(result.isEmpty())
        verify(exactly = 1) { dataSource.getSupportedCountries(context) }
        verify(exactly = 0) { domainMapper.apply(any()) }
    }

    @Test
    fun `test getSupportedCountries returns non-empty list`() {
        val countryRawList = mutableListOf(
            SupportedCountryRaw("United States", "US"),
            SupportedCountryRaw("Canada", "CA"),
        )
        val countryDomainList = listOf(
            SupportedCountriesDomainEntity("United States", "US", true),
            SupportedCountriesDomainEntity("Canada", "CA", true),
        )

        every { dataSource.getSupportedCountries(context) } returns countryRawList
        every { domainMapper.apply(any()) } answers {
            val raw = firstArg<SupportedCountryRaw>()
            val domain = SupportedCountriesDomainEntity(raw.countryName, raw.countryCode, true)
            domain
        }

        val result = repository.getSupportedCountries(context)

        assert(result == countryDomainList)
        verify(exactly = 1) { dataSource.getSupportedCountries(context) }
        verify(exactly = 2) { domainMapper.apply(any()) }
    }
}
