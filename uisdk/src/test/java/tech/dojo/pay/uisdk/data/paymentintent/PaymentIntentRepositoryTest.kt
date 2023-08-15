package tech.dojo.pay.uisdk.data.paymentintent

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.uisdk.data.entities.PaymentIntentPayload
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.data.mapper.PaymentIntentPayLoadMapper
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.domain.mapper.PaymentIntentDomainEntityMapper

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class PaymentIntentRepositoryTest {
    private val dataSource: PaymentIntentDataSource = mock()
    private val paymentIntentDomainEntityMapper: PaymentIntentDomainEntityMapper = mock()
    private val paymentIntentPayLoadMapper: PaymentIntentPayLoadMapper = mock()
    private lateinit var sut: PaymentIntentRepository

    @Before
    fun setUp() {
        sut = PaymentIntentRepository(
            dataSource,
            paymentIntentDomainEntityMapper,
            paymentIntentPayLoadMapper,
        )
    }

    @Test
    fun `when fetchPaymentIntent fails PaymentIntent stream should emits FetchFailure`() = runTest {
        // arrange
        val paymentId = "paymentId"
        given(dataSource.fetchPaymentIntent(any(), any(), any()))
            .willAnswer {
                val onFailure: () -> Unit = it.arguments[2] as () -> Unit
                onFailure.invoke()
            }
        val expectedValue = PaymentIntentResult.FetchFailure
        // act
        sut.fetchPaymentIntent(paymentId)
        val actual = sut.observePaymentIntent().first()

        // assert
        assertEquals(expectedValue, actual)
    }

    @Test
    fun `when fetchPaymentIntent success PaymentIntent stream should emits Success with domainEntity`() =
        runTest {
            // arrange
            val paymentId = "paymentId"
            val paymentIntentJson = "paymentIntentJson"
            val paymentIntentDomainEntity = PaymentIntentDomainEntity(
                id = "id",
                paymentToken = "clientSessionSecret",
                amount = AmountDomainEntity(
                    10L,
                    "0.10",
                    "GBP",
                ),
                supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
            )
            given(dataSource.fetchPaymentIntent(any(), any(), any()))
                .willAnswer {
                    val successCallback = it.arguments[1] as (String) -> Unit
                    successCallback.invoke(paymentIntentJson)
                }
            given(paymentIntentPayLoadMapper.mapToPaymentIntentPayLoad(any())).willReturn(
                PaymentIntentPayload(),
            )
            given(paymentIntentDomainEntityMapper.apply(any())).willReturn(paymentIntentDomainEntity)
            val expectedValue = PaymentIntentResult.Success(paymentIntentDomainEntity)
            // act
            sut.fetchPaymentIntent(paymentId)
            val actual = sut.observePaymentIntent().first()

            // assert
            assertEquals(expectedValue, actual)
        }

    @Test
    fun `when fetchSetUpIntent fails PaymentIntent stream should emits FetchFailure`() = runTest {
        // arrange
        val paymentId = "paymentId"
        given(dataSource.fetchSetUpIntent(any(), any(), any()))
            .willAnswer {
                val onFailure: () -> Unit = it.arguments[2] as () -> Unit
                onFailure.invoke()
            }
        val expectedValue = PaymentIntentResult.FetchFailure
        // act
        sut.fetchSetUpIntent(paymentId)
        val actual = sut.observePaymentIntent().first()
        // assert
        assertEquals(expectedValue, actual)
    }

    @Test
    fun `when fetchSetUpIntent success PaymentIntent stream should emits Success with domainEntity`() =
        runTest {
            // arrange
            val paymentId = "paymentId"
            val paymentIntentJson = "paymentIntentJson"
            val paymentIntentDomainEntity = PaymentIntentDomainEntity(
                id = "id",
                paymentToken = "clientSessionSecret",
                amount = AmountDomainEntity(
                    10L,
                    "0.10",
                    "GBP",
                ),
                supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD),
            )
            given(dataSource.fetchSetUpIntent(any(), any(), any()))
                .willAnswer {
                    val successCallback = it.arguments[1] as (String) -> Unit
                    successCallback.invoke(paymentIntentJson)
                }
            given(paymentIntentPayLoadMapper.mapToPaymentIntentPayLoad(any())).willReturn(
                PaymentIntentPayload(),
            )
            given(paymentIntentDomainEntityMapper.apply(any())).willReturn(paymentIntentDomainEntity)
            val expectedValue = PaymentIntentResult.Success(paymentIntentDomainEntity)
            // act
            sut.fetchSetUpIntent(paymentId)
            val actual = sut.observePaymentIntent().first()
            // assert
            assertEquals(expectedValue, actual)
        }

    @Test
    fun `when fetchPaymentIntent success PaymentIntent stream should emits FetchFailure if something wrong happened on mapping `() =
        runTest {
            // arrange
            val paymentId = "paymentId"
            val paymentIntentJson = "paymentIntentJson"
            given(dataSource.fetchPaymentIntent(any(), any(), any()))
                .willAnswer {
                    val successCallback = it.arguments[1] as (String) -> Unit
                    successCallback.invoke(paymentIntentJson)
                }
            given(paymentIntentPayLoadMapper.mapToPaymentIntentPayLoad(any())).willReturn(
                PaymentIntentPayload(),
            )
            given(paymentIntentDomainEntityMapper.apply(any())).willThrow(RuntimeException("A mock exception occurred!"))
            val expectedValue = PaymentIntentResult.FetchFailure
            // act

            sut.fetchPaymentIntent(paymentId)
            val actual = sut.observePaymentIntent().first()
            // assert
            assertEquals(expectedValue, actual)
        }
}
