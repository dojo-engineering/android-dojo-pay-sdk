package tech.dojo.pay.sdk.card.data.mappers

import org.junit.Assert
import org.junit.Test
import tech.dojo.pay.sdk.card.data.entities.DecryptGPayTokenBody

internal class GPayTokenDecryptionRequestMapperTest {
    private val mapper = GPayTokenDecryptionRequestMapper()

    @Test
    fun `calling apply with valid paymentInformationJson should return valid DecryptGPayTokenBody`() {
        // arrange
        val paymentInformationJson = GpayPaymentRequestMapperTestData.paymentInformationFullJson
        val expected = DecryptGPayTokenBody(
            token = "testToken"
        )
        // act
        val result = mapper.apply(paymentInformationJson)
        // assert
        Assert.assertEquals(expected, result)
    }

    @Test
    fun `calling apply with invalid paymentInformationJson should return null`() {
        // arrange
        val paymentInformationJson = ""
        // act
        val result = mapper.apply(paymentInformationJson)
        // assert
        Assert.assertNull(result)
    }
}
