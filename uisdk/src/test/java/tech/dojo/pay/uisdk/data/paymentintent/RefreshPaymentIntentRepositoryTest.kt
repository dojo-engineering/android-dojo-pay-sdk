package tech.dojo.pay.uisdk.data.paymentintent

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.uisdk.data.entities.PaymentIntentPayload
import tech.dojo.pay.uisdk.data.mapper.PaymentIntentPayLoadMapper
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult
import tech.dojo.pay.uisdk.domain.mapper.PaymentIntentDomainEntityMapper

class RefreshPaymentIntentRepositoryTest {
    private val dataSource: PaymentIntentDataSource = mock()
    private val paymentIntentDomainEntityMapper: PaymentIntentDomainEntityMapper = mock()
    private val paymentIntentPayLoadMapper: PaymentIntentPayLoadMapper = mock()
    private lateinit var sut: RefreshPaymentIntentRepository

    @Before
    fun setUp() {
        sut = RefreshPaymentIntentRepository(dataSource, paymentIntentDomainEntityMapper, paymentIntentPayLoadMapper)
    }

    @Test
    fun `when refreshPaymentIntent fails refresh PaymentIntent stream should emit RefreshFailure`() = runTest {
        // arrange
        val paymentId = "paymentId"
        doAnswer {
            val onFailure: () -> Unit = it.arguments[2] as () -> Unit
            onFailure.invoke()
        }.whenever(dataSource).refreshPaymentIntent(any(), any(), any())

        val expectedValue = RefreshPaymentIntentResult.RefreshFailure

        // act
        sut.refreshPaymentIntent(paymentId)
        val actual = sut.getRefreshedPaymentTokenFlow().first()

        // assert
        assertEquals(expectedValue, actual)
    }

    @Test
    fun `when refreshPaymentIntent success PaymentIntent stream should emits Success with domainEntity`() =
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
            doAnswer { invocation ->
                val successCallback = invocation.arguments[1] as (String) -> Unit
                successCallback.invoke(paymentIntentJson)
            }.whenever(dataSource).refreshPaymentIntent(any(), any(), any())
            given(paymentIntentPayLoadMapper.mapToPaymentIntentPayLoad(any())).willReturn(
                PaymentIntentPayload(),
            )
            given(paymentIntentDomainEntityMapper.apply(any())).willReturn(paymentIntentDomainEntity)
            val expectedValue = RefreshPaymentIntentResult.Success(paymentIntentDomainEntity.paymentToken)
            // act
            sut.refreshPaymentIntent(paymentId)
            val actual = sut.getRefreshedPaymentTokenFlow().first()

            // assert
            assertEquals(expectedValue, actual)
        }

    @Test
    fun `when refreshPaymentIntent success PaymentIntent stream should emits RefreshFailure if something wrong happened on mapping `() =
        runTest {
            // arrange
            val paymentId = "paymentId"
            val paymentIntentJson = "paymentIntentJson"
            doAnswer { invocation ->
                val successCallback = invocation.arguments[1] as (String) -> Unit
                successCallback.invoke(paymentIntentJson)
            }.whenever(dataSource).refreshPaymentIntent(any(), any(), any())
            given(paymentIntentPayLoadMapper.mapToPaymentIntentPayLoad(any())).willReturn(
                PaymentIntentPayload(),
            )
            given(paymentIntentDomainEntityMapper.apply(any())).willThrow(RuntimeException("A mock exception occurred!"))
            val expectedValue = RefreshPaymentIntentResult.RefreshFailure
            // act
            sut.refreshPaymentIntent(paymentId)
            val actual = sut.getRefreshedPaymentTokenFlow().first()

            // assert
            assertEquals(expectedValue, actual)
        }
}
