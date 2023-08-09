package tech.dojo.pay.sdk.payemntintent.data

import com.google.gson.JsonObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response
import tech.dojo.pay.sdk.DojoPaymentIntentResult

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class PaymentIntentRepositoryTest {
    @Mock
    lateinit var api: PaymentIntentApi

    @Test
    fun `calling getPaymentIntent with Successful response should return Success DojoPaymentIntentResult `() =
        runTest {
            // arrange
            val result = JsonObject()
            result.addProperty("test_key", "test")
            val response: Response<JsonObject> = mock()
            whenever(response.isSuccessful).thenReturn(true)
            whenever(response.body()).thenReturn(result)
            whenever(api.fetchPaymentIntent(any(), any(), any())).thenReturn(response)
            val expected = DojoPaymentIntentResult.Success("{\"test_key\":\"test\"}")
            // act
            val actual = PaymentIntentRepository(api).getPaymentIntent("test")
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `calling getPaymentIntent with Failed response should return Failed DojoPaymentIntentResult `() =
        runTest {
            // arrange
            val response: Response<JsonObject> = mock()
            whenever(response.isSuccessful).thenReturn(false)
            whenever(api.fetchPaymentIntent(any(), any(), any())).thenReturn(response)
            val expected = DojoPaymentIntentResult.Failed
            // act
            val actual = PaymentIntentRepository(api).getPaymentIntent("test")
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `calling getSetUpIntent with Successful response should return Success DojoPaymentIntentResult `() =
        runTest {
            // arrange
            val result = JsonObject()
            result.addProperty("test_key", "test")
            val response: Response<JsonObject> = mock()
            whenever(response.isSuccessful).thenReturn(true)
            whenever(response.body()).thenReturn(result)
            whenever(api.fetchSetUpIntent(any(), any(), any())).thenReturn(response)
            val expected = DojoPaymentIntentResult.Success("{\"test_key\":\"test\"}")
            // act
            val actual = PaymentIntentRepository(api).getSetUpIntent("test")
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `calling getSetUpIntent with Failed response should return Failed DojoPaymentIntentResult `() =
        runTest {
            // arrange
            val response: Response<JsonObject> = mock()
            whenever(response.isSuccessful).thenReturn(false)
            whenever(api.fetchSetUpIntent(any(), any(), any())).thenReturn(response)
            val expected = DojoPaymentIntentResult.Failed
            // act
            val actual = PaymentIntentRepository(api).getSetUpIntent("test")
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `calling refreshPaymentIntent with Successful response should return Success DojoPaymentIntentResult `() =
        runTest {
            // arrange
            val result = JsonObject()
            result.addProperty("test_key", "test")
            val response: Response<JsonObject> = mock()
            whenever(response.isSuccessful).thenReturn(true)
            whenever(response.body()).thenReturn(result)
            whenever(api.refreshPaymentIntent(any(), any(), any())).thenReturn(response)
            val expected = DojoPaymentIntentResult.Success("{\"test_key\":\"test\"}")
            // act
            val actual = PaymentIntentRepository(api).refreshPaymentIntent("test")
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `calling refreshPaymentIntent with Failed response should return Failed DojoPaymentIntentResult `() =
        runTest {
            // arrange
            val response: Response<JsonObject> = mock()
            whenever(response.isSuccessful).thenReturn(false)
            whenever(api.refreshPaymentIntent(any(), any(), any())).thenReturn(response)
            val expected = DojoPaymentIntentResult.Failed
            // act
            val actual = PaymentIntentRepository(api).refreshPaymentIntent("test")
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `calling refreshSetUpIntent with Successful response should return Success DojoPaymentIntentResult `() =
        runTest {
            // arrange
            val result = JsonObject()
            result.addProperty("test_key", "test")
            val response: Response<JsonObject> = mock()
            whenever(response.isSuccessful).thenReturn(true)
            whenever(response.body()).thenReturn(result)
            whenever(api.refreshSetupIntent(any(), any(), any())).thenReturn(response)
            val expected = DojoPaymentIntentResult.Success("{\"test_key\":\"test\"}")
            // act
            val actual = PaymentIntentRepository(api).refreshSetUpIntent("test")
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `calling refreshSetUpIntent with Failed response should return Failed DojoPaymentIntentResult `() =
        runTest {
            // arrange
            val response: Response<JsonObject> = mock()
            whenever(response.isSuccessful).thenReturn(false)
            whenever(api.refreshSetupIntent(any(), any(), any())).thenReturn(response)
            val expected = DojoPaymentIntentResult.Failed
            // act
            val actual = PaymentIntentRepository(api).refreshSetUpIntent("test")
            // assert
            Assert.assertEquals(expected, actual)
        }
}
