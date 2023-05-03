package tech.dojo.pay.sdk.card.data

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import okhttp3.Headers
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response
import tech.dojo.pay.sdk.card.data.entities.BaseUrlRaw
import tech.dojo.pay.sdk.card.data.remote.baseurl.BaseUrlApi

class BaseUrlRepositoryTest {

    private val mockBaseUrlAWS = "https://test-aws.com/"
    private val mockBaseUrlGCP = "https://test-gcp.com/"

    private lateinit var baseUrlRepository: BaseUrlRepository

    @Test
    fun `getBaseUrl should return AWS URL when GCP request fails and AWS request succeeds`() {
        // Given
        val mockApi: BaseUrlApi = mockk()
        baseUrlRepository = BaseUrlRepository
        baseUrlRepository?.api = mockApi
        baseUrlRepository?.dispatchers = Dispatchers.Unconfined
        val responseForGoogle: Response<BaseUrlRaw> = mockk()
        every { responseForGoogle.isSuccessful } returns (false)
        coEvery { mockApi.getBaseUrl(BASE_URL_GOOGLE_PROD) } returns responseForGoogle
        val responseForAWS: Response<BaseUrlRaw> = mockk()
        every { responseForAWS.isSuccessful } returns (true)
        every { responseForAWS.body() }.returns(BaseUrlRaw(baseUrl = "https://test-aws.com", format = "Tue, 03 May 2023 10:20:30 GMT", baseClientEventUrl = "https://example.com/"))
        every { responseForAWS.headers() }.returns(Headers.Builder().add("last-modified", "Tue, 03 May 2023 09:20:30 GMT").build())
        coEvery { mockApi.getBaseUrl(BASE_URL_AWS_PROD) } returns responseForAWS
        // When
        val result = baseUrlRepository?.getBaseUrl()
        // Then
        assertEquals(mockBaseUrlAWS, result)
    }

    @Test
    fun `getBaseUrl should return latest URL when both GCP and AWS requests succeed`() {
        // Given
        val mockApi: BaseUrlApi = mockk()
        baseUrlRepository = BaseUrlRepository
        baseUrlRepository.dispatchers = Dispatchers.Unconfined
        baseUrlRepository.api = mockApi
        val responseForGoogle: Response<BaseUrlRaw> = mockk()
        every { responseForGoogle.isSuccessful } returns (true)
        every { responseForGoogle.body() }.returns(BaseUrlRaw(baseUrl = "https://test-gcp.com", format = "Tue, 03 May 2023 10:20:30 GMT", baseClientEventUrl = "https://example.com/"))
        every { responseForGoogle.headers() }.returns(Headers.Builder().add("last-modified", "Tue, 03 May 2023 10:20:30 GMT").build())
        coEvery { mockApi.getBaseUrl(BASE_URL_GOOGLE_PROD) } returns responseForGoogle
        coEvery { mockApi.getBaseUrl(BASE_URL_GOOGLE_PROD) } returns responseForGoogle
        val responseForAWS: Response<BaseUrlRaw> = mockk()
        every { responseForAWS.isSuccessful } returns (true)
        every { responseForAWS.body() }.returns(BaseUrlRaw(baseUrl = "https://test-aws.com", format = "Tue, 03 May 2023 11:20:30 GMT", baseClientEventUrl = "https://example.com/"))
        every { responseForAWS.headers() }.returns(Headers.Builder().add("last-modified", "Tue, 03 May 2023 11:20:30 GMT").build())
        coEvery { mockApi.getBaseUrl(BASE_URL_AWS_PROD) } returns responseForAWS
        // When
        val result = baseUrlRepository.getBaseUrl()
        // Then
        assertEquals(mockBaseUrlAWS, result)
    }
}
