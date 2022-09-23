package tech.dojo.pay.uisdk.domain.mapper

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import tech.dojo.pay.uisdk.data.entities.Amount
import tech.dojo.pay.uisdk.data.entities.PaymentIntentPayload
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.EssentialParamMissingException
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class PaymentIntentDomainEntityMapperTest {
    @Test
    fun `calling apply with valid PaymentIntentPayload should map to PaymentIntentDomainEntity `() =
        runTest {
            // arrange
            val raw = PaymentIntentPayload(
                id = "id",
                clientSessionSecret = "clientSessionSecret",
                amount = Amount(
                    10L,
                    "GBP"
                )
            )
            val expected = PaymentIntentDomainEntity(
                id = "id",
                paymentToken = "clientSessionSecret",
                amount = AmountDomainEntity(
                    10L,
                    "0.10",
                    "GBP"
                )
            )
            // act
            val actual = PaymentIntentDomainEntityMapper().apply(raw)
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `calling apply with invalid PaymentIntentPayload should Thrown EssentialParamMissingException `() =
        runTest {
            // arrange
            val raw = PaymentIntentPayload()
            // act
            val actual = assertThrows(
                EssentialParamMissingException::class.java
            ) { PaymentIntentDomainEntityMapper().apply(raw) }
            // assert
            assertTrue(actual.message?.contains("clientSessionSecret") ?: false)
            assertTrue(actual.message?.contains("amount") ?: false)
            assertTrue(actual.message?.contains("id") ?: false)
        }
}
