package tech.dojo.pay.uisdk.data.paymentintent

import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.domain.mapper.PaymentIntentDomainEntityMapper

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class PaymentIntentRepositoryTest {
    private val dataSource: PaymentIntentDataSource = mockk()
    private val gson: Gson = mock()
    private val mapper: PaymentIntentDomainEntityMapper = mockk()

    @Test
    fun `when fetchPaymentIntent fails PaymentIntent stream should emits FetchFailure`() = runTest {
        // arrange
        val paymentId = "paymentId"
        every {
            dataSource.fetchPaymentIntent(
                any(),
                any(),
                any()
            )
        }.answers { thirdArg<() -> Unit>().invoke() }
        val expectedValue = PaymentIntentResult.FetchFailure
        // act
        val sut = PaymentIntentRepository(dataSource, gson, mapper)
        sut.fetchPaymentIntent(paymentId)
        val stream = sut.observePaymentIntent()

        // assert
        val job = launch { stream.collectLatest { assertEquals(expectedValue, it) } }
        job.cancel()
    }

    @Test
    fun `when fetchPaymentIntent success  PaymentIntent stream should emits Success with domainEntity`() =
        runTest {
            // arrange
            val paymentId = "paymentId"
            val paymentIntentJson = "paymentIntentJson"
            val paymentIntentDomainEntity = PaymentIntentDomainEntity(
                id = "id", paymentToken = "clientSessionSecret",
                amount = AmountDomainEntity(
                    10L, "0.10", "GBP"
                ),
                supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD)
            )
            every {
                dataSource.fetchPaymentIntent(
                    any(),
                    any(),
                    any()
                )
            }.answers { secondArg<(paymentIntentJson: String) -> Unit>().invoke(paymentIntentJson) }
            every { mapper.apply(any()) } returns paymentIntentDomainEntity
            val expectedValue = PaymentIntentResult.Success(paymentIntentDomainEntity)
            // act
            val sut = PaymentIntentRepository(dataSource, gson, mapper)
            sut.fetchPaymentIntent(paymentId)
            val stream = sut.observePaymentIntent()

            // assert
            val job = launch { stream.collectLatest { assertEquals(expectedValue, it) } }
            job.cancel()
        }

    @Test
    fun `when fetchPaymentIntent success PaymentIntent stream should emits FetchFailure if something wrong happened on mapping `() =
        runTest {
            // arrange
            val paymentId = "paymentId"
            val paymentIntentJson = "paymentIntentJson"
            every {
                dataSource.fetchPaymentIntent(
                    any(),
                    any(),
                    any()
                )
            }.answers { secondArg<(paymentIntentJson: String) -> Unit>().invoke(paymentIntentJson) }
            every { mapper.apply(any()) } throws RuntimeException("A mock exception occurred!")
            val expectedValue = PaymentIntentResult.FetchFailure
            // act
            val sut = PaymentIntentRepository(dataSource, gson, mapper)
            sut.fetchPaymentIntent(paymentId)
            val stream = sut.observePaymentIntent()

            // assert
            val job = launch { stream.collectLatest { assertEquals(expectedValue, it) } }
            job.cancel()
        }

    @Test
    fun `when refreshPaymentIntent fails PaymentIntent stream should emits RefreshFailure`() =
        runTest {
            // arrange
            val paymentId = "paymentId"
            every {
                dataSource.refreshPaymentIntent(
                    any(),
                    any(),
                    any()
                )
            }.answers { thirdArg<() -> Unit>().invoke() }
            val expectedValue = PaymentIntentResult.RefreshFailure
            // act
            val sut = PaymentIntentRepository(dataSource, gson, mapper)
            sut.refreshPaymentIntent(paymentId)
            val stream = sut.observePaymentIntent()

            // assert
            val job = launch { stream.collectLatest { assertEquals(expectedValue, it) } }
            job.cancel()
        }

    @Test
    fun `when refreshPaymentIntent success  PaymentIntent stream should emits Success with domainEntity`() =
        runTest {
            // arrange
            val paymentId = "paymentId"
            val paymentIntentJson = "paymentIntentJson"
            val paymentIntentDomainEntity = PaymentIntentDomainEntity(
                id = "id", paymentToken = "clientSessionSecret",
                amount = AmountDomainEntity(
                    10L, "0.10", "GBP"
                ),
                supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD)
            )
            every {
                dataSource.refreshPaymentIntent(
                    any(),
                    any(),
                    any()
                )
            }.answers { secondArg<(paymentIntentJson: String) -> Unit>().invoke(paymentIntentJson) }
            every { mapper.apply(any()) } returns paymentIntentDomainEntity
            val expectedValue = PaymentIntentResult.Success(paymentIntentDomainEntity)
            // act
            val sut = PaymentIntentRepository(dataSource, gson, mapper)
            sut.refreshPaymentIntent(paymentId)
            val stream = sut.observePaymentIntent()

            // assert
            val job = launch { stream.collectLatest { assertEquals(expectedValue, it) } }
            job.cancel()
        }

    @Test
    fun `when refreshPaymentIntent success PaymentIntent stream should emits FetchFailure if something wrong happened on mapping `() =
        runTest {
            // arrange
            val paymentId = "paymentId"
            val paymentIntentJson = "paymentIntentJson"
            every {
                dataSource.refreshPaymentIntent(
                    any(),
                    any(),
                    any()
                )
            }.answers { secondArg<(paymentIntentJson: String) -> Unit>().invoke(paymentIntentJson) }
            every { mapper.apply(any()) } throws RuntimeException("A mock exception occurred!")
            val expectedValue = PaymentIntentResult.FetchFailure
            // act
            val sut = PaymentIntentRepository(dataSource, gson, mapper)
            sut.refreshPaymentIntent(paymentId)
            val stream = sut.observePaymentIntent()

            // assert
            val job = launch { stream.collectLatest { assertEquals(expectedValue, it) } }
            job.cancel()
        }
}
