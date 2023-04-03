package tech.dojo.pay.uisdk.data.paymentmethods

import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.uisdk.domain.entities.DeletePaymentMethodsResult
import tech.dojo.pay.uisdk.domain.entities.FetchPaymentMethodsResult
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntityItem
import tech.dojo.pay.uisdk.domain.mapper.SupportedPaymentMethodsDomainMapper

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class PaymentMethodsRepositoryTest {
    private val dataSource: PaymentMethodsDataSource = mockk()
    private val gson: Gson = mock()
    private val domainMapper: SupportedPaymentMethodsDomainMapper = mockk()

    @Test
    fun `when deletePaymentMethods fails onDeletePaymentMethodsFailed should be called`() =
        runTest {
            val customerId = "customerId"
            val customerSecret = "customerSecret"
            val paymentMethodId = "paymentMethodId"
            val onDeletePaymentMethodsSuccess: (DeletePaymentMethodsResult) -> Unit = {}
            var actualValue: DeletePaymentMethodsResult? = null
            val onDeletePaymentMethodsFailed: (DeletePaymentMethodsResult) -> Unit =
                { actualValue = it }
            every {
                dataSource.deletePaymentMethods(
                    any(), any(), any(), any(), any()
                )
            }.answers { lastArg<() -> Unit>().invoke() }
            // act
            val sut = PaymentMethodsRepository(dataSource, gson, domainMapper)
            sut.deletePaymentMethods(
                customerId,
                customerSecret,
                paymentMethodId,
                onDeletePaymentMethodsSuccess,
                onDeletePaymentMethodsFailed
            )
            // assert
            Assert.assertTrue(actualValue is DeletePaymentMethodsResult.Failure)
        }

    @Test
    fun `when deletePaymentMethods success onDeletePaymentMethodsSuccess should be called`() {
        val customerId = "customerId"
        val customerSecret = "customerSecret"
        val paymentMethodId = "paymentMethodId"
        var actualValue: DeletePaymentMethodsResult? = null
        val onDeletePaymentMethodsSuccess: (DeletePaymentMethodsResult) -> Unit =
            { actualValue = it }
        val onDeletePaymentMethodsFailed: (DeletePaymentMethodsResult) -> Unit = { }
        every {
            dataSource.deletePaymentMethods(
                any(), any(), any(), any(), any()
            )
        }.answers { arg<() -> Unit>(3).invoke() }
        // act
        val sut = PaymentMethodsRepository(dataSource, gson, domainMapper)
        sut.deletePaymentMethods(
            customerId,
            customerSecret,
            paymentMethodId,
            onDeletePaymentMethodsSuccess,
            onDeletePaymentMethodsFailed
        )
        // assert
        Assert.assertTrue(actualValue is DeletePaymentMethodsResult.Success)
    }

    @Test
    fun `when fetchPaymentMethod success PaymentMethod stream should emits Success with domainEntity`() =
        runTest {
            // arrange
            val customerId = "customerId"
            val customerSecret = "customerSecret"
            val paymentMethodsRawJson = "paymentMethodsRaw"
            val domainEntity = PaymentMethodsDomainEntity(
                listOf(
                    PaymentMethodsDomainEntityItem(
                        id = "PaymentId",
                        pan = "pan",
                        expiryDate = "expiryDate",
                        scheme = CardsSchemes.MASTERCARD
                    )
                )
            )
            every { domainMapper.apply(any()) } returns domainEntity
            coEvery { dataSource.fetchPaymentMethods(any(), any(), any(), any()) } answers {
                val onSuccess = thirdArg<(String) -> Unit>()
                onSuccess(paymentMethodsRawJson)
            }
            val expectedValue = FetchPaymentMethodsResult.Success(domainEntity)
            // act
            val sut = PaymentMethodsRepository(dataSource, gson, domainMapper)
            sut.fetchPaymentMethod(customerId, customerSecret)
            val flow = sut.observePaymentMethods()
            // assert
            val job = launch { flow.collectLatest { Assert.assertEquals(expectedValue, it) } }
            job.cancel()
        }

    @Test
    fun `when fetchPaymentMethod fails PaymentMethod stream should emits Failure`() = runTest {
        // arrange
        val customerId = "customerId"
        val customerSecret = "customerSecret"
        coEvery { dataSource.fetchPaymentMethods(any(), any(), any(), any()) } answers {
            val onFetchPaymentMethodsFailed = lastArg<() -> Unit>()
            onFetchPaymentMethodsFailed()
        }
        val expectedValue = FetchPaymentMethodsResult.Failure
        // act
        val sut = PaymentMethodsRepository(dataSource, gson, domainMapper)
        sut.fetchPaymentMethod(customerId, customerSecret)
        // assert
        val flow = sut.observePaymentMethods()
        val job = launch { flow.collectLatest { Assert.assertEquals(expectedValue, it) } }
        job.cancel()
    }

    @Test
    fun `when fetchPaymentMethod success PaymentMethod stream should emits Failure if something wrong happened on mapping`() = runTest {
        // arrange
        val customerId = "customerId"
        val customerSecret = "customerSecret"
        val paymentMethodsRawJson = "paymentMethodsRaw"
        every { domainMapper.apply(any()) } throws RuntimeException("A mock exception occurred!")
        coEvery { dataSource.fetchPaymentMethods(any(), any(), any(), any()) } answers {
            val onSuccess = thirdArg<(String) -> Unit>()
            onSuccess(paymentMethodsRawJson)
        }
        val expectedValue = FetchPaymentMethodsResult.Failure
        // act
        val sut = PaymentMethodsRepository(dataSource, gson, domainMapper)
        sut.fetchPaymentMethod(customerId, customerSecret)
        val flow = sut.observePaymentMethods()
        // assert
        val job = launch { flow.collectLatest { Assert.assertEquals(expectedValue, it) } }
        job.cancel()
    }
}
