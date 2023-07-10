package tech.dojo.pay.uisdk.domain

import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import tech.dojo.pay.uisdk.data.supportedcountries.SupportedCountriesRepository
import tech.dojo.pay.uisdk.domain.entities.SupportedCountriesDomainEntity

class GetSupportedCountriesUseCaseTest {

    @Test
    fun `getSupportedCountries should return supported countries`() {
        // given
        val expectedCountries = listOf(
            SupportedCountriesDomainEntity("US", "United States", true),
            SupportedCountriesDomainEntity("GB", "United Kingdom", true),
            SupportedCountriesDomainEntity("JP", "Japan", true)
        )
        val repository = mockk<SupportedCountriesRepository>()
        every { repository.getSupportedCountries() } returns expectedCountries
        val useCase = GetSupportedCountriesUseCase(repository)

        // when
        val result = useCase.getSupportedCountries()

        // then
        assert(result == expectedCountries)
    }
}
