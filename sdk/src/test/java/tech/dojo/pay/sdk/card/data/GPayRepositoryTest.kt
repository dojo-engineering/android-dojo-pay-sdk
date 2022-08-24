package tech.dojo.pay.sdk.card.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.entities.GPayDetails
import tech.dojo.pay.sdk.card.data.entities.PaymentResponse
import tech.dojo.pay.sdk.card.data.remote.cardpayment.CardPaymentApi
import tech.dojo.pay.sdk.card.entities.GooglePayAddressDetails
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class GPayRepositoryTest {
    @Mock
    lateinit var api: CardPaymentApi
    private lateinit var repo: GPayRepository

    @Before
    fun setup() {
        repo = GPayRepository(api, TOKEN)
    }

    @Test
    fun `WHEN result code is AUTHORIZING THEN threeDs params are returned`() = runTest {
        // arrange
        val threeDSParams = ThreeDSParams(
            stepUpUrl = "url",
            jwt = "jwt",
            md = "md"
        )

        whenever(api.processGPay(any(), any())).thenReturn(
            PaymentResponse(
                statusCode = DojoPaymentResult.AUTHORIZING.code,
                stepUpUrl = threeDSParams.stepUpUrl,
                jwt = threeDSParams.jwt,
                md = threeDSParams.md
            )
        )
        val expected = PaymentResult.ThreeDSRequired(threeDSParams)

        // act
        val result = repo.processPayment(G_PAY_PAYLOAD)
        // Assert
        Assert.assertEquals(expected, result)
    }

    @Test
    fun `WHEN result code is not AUTHORIZING THEN completed result is returned`() = runTest {
        // arrange
        whenever(api.processGPay(any(), any())).thenReturn(
            PaymentResponse(statusCode = DojoPaymentResult.SUCCESSFUL.code)
        )
        val expected = PaymentResult.Completed(DojoPaymentResult.SUCCESSFUL)
        // act
        val result = repo.processPayment(G_PAY_PAYLOAD)
        // Assert
        Assert.assertEquals(expected, result)
    }

    private companion object {
        const val TOKEN = "TOKEN"

        val G_PAY_PAYLOAD = GPayDetails(
            token = "Goaytoken",
            email = "a@test.con",
            phoneNumber = "phoneNumber",
            billingContact = GooglePayAddressDetails(
                address1 = "Address",
                postcode = "Postcode",
                countryCode = "Country"
            ),

            shippingContact = GooglePayAddressDetails(
                address1 = "Address",
                postcode = "Postcode",
                countryCode = "Country"
            )
        )

        val ADDRESS_DETAILS = GooglePayAddressDetails(
            address1 = "Address",
            postcode = "Postcode",
            countryCode = "Country"
        )
    }
}
