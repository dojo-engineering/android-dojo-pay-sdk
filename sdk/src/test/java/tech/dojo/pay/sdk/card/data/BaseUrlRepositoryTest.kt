package tech.dojo.pay.sdk.card.data
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response
import tech.dojo.pay.sdk.card.data.entities.BaseUrlRaw
import tech.dojo.pay.sdk.card.data.remote.baseurl.BaseUrlApi

class BaseUrlRepositoryTest {
    private val api = mockk<BaseUrlApi>()
    private val response = mockk<Response<BaseUrlRaw>>()


    @Test
    fun `test getBaseUrl returns empty string when response is not successful`() = runBlocking {
        val baseUrlRepository = BaseUrlRepository

        // Mock unsuccessful response from API
        every { response.isSuccessful } returns false
        coEvery { api.getBaseUrl() } returns response

        val result = baseUrlRepository.getBaseUrl()

        assertEquals("", result)
    }

}
