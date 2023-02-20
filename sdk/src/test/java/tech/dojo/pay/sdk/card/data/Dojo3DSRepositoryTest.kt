package tech.dojo.pay.sdk.card.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.entities.AuthorizationBody
import tech.dojo.pay.sdk.card.data.entities.PaymentResponse
import tech.dojo.pay.sdk.card.data.entities.ValidateCardinalResponse
import tech.dojo.pay.sdk.card.data.remote.cardpayment.CardPaymentApi

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class Dojo3DSRepositoryTest {
    @Mock
    lateinit var api: CardPaymentApi

    private lateinit var repo: Dojo3DSRepository

    @Test
    fun `calling processAuthorization should call processAuthorization from api`() = runTest {
        // arrange
        whenever(api.processAuthorization(any(), any(), any())).thenReturn(
            PaymentResponse(statusCode = DojoPaymentResult.SUCCESSFUL.code)
        )
        // act
        repo = Dojo3DSRepository(api, "token")
        repo.processAuthorization("jwt", "id", null)
        // Assert
        verify(api).processAuthorization(
            "token",
            AuthorizationBody("jwt", "id", ValidateCardinalResponse(null, null, "", ""))
        )
    }
}
