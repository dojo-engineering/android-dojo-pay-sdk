package tech.dojo.pay.sdk.payemntintent

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import tech.dojo.pay.sdk.DojoPaymentIntentResult
import tech.dojo.pay.sdk.payemntintent.data.PaymentIntentRepository

@OptIn(ExperimentalCoroutinesApi::class)
internal class PaymentIntentProviderTest {

    private val paymentIntentRepository: PaymentIntentRepository = mockk()
    private val repositoryScope: CoroutineScope = CoroutineScope(UnconfinedTestDispatcher())

    private val paymentIntentProvider = PaymentIntentProvider(
        paymentIntentRepository,
        repositoryScope,
    )

    @Test
    fun `when fetchPaymentIntent success onPaymentIntentSuccess should be invoked`() = runTest {
        // arrange
        val paymentId = "paymentId"
        val paymentIntent = "paymentIntent"
        var actualPaymentIntentResult = ""

        coEvery {
            paymentIntentRepository.getPaymentIntent(any())
        }.answers { DojoPaymentIntentResult.Success(paymentIntent) }
        val onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit = {
            actualPaymentIntentResult = paymentIntent
        }
        val onPaymentIntentFailed: () -> Unit = {}
        // act
        paymentIntentProvider.fetchPaymentIntent(
            paymentId,
            onPaymentIntentSuccess,
            onPaymentIntentFailed,
        )

        // assert
        assertEquals(actualPaymentIntentResult, paymentIntent)
    }

    @Test
    fun `when fetchPaymentIntent fails onPaymentIntentFailed should be invoked`() = runTest {
        // arrange
        val paymentId = "paymentId"
        var actualPaymentIntentResult = ""

        coEvery {
            paymentIntentRepository.getPaymentIntent(any())
        }.answers { DojoPaymentIntentResult.Failed }
        val onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit = {}
        val onPaymentIntentFailed: () -> Unit = {
            actualPaymentIntentResult = ""
        }
        // act
        paymentIntentProvider.fetchPaymentIntent(
            paymentId,
            onPaymentIntentSuccess,
            onPaymentIntentFailed,
        )

        // assert
        assertTrue(actualPaymentIntentResult.isBlank())
    }

    @Test
    fun `when fetchPaymentIntent Throws onPaymentIntentFailed should be invoked`() = runTest {
        // arrange
        val paymentId = "paymentId"
        var actualPaymentIntentResult = ""
        val exception = Exception()

        coEvery {
            paymentIntentRepository.getPaymentIntent(any())
        }.throws(exception)
        val onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit = {}
        val onPaymentIntentFailed: () -> Unit = {
            actualPaymentIntentResult = ""
        }
        // act
        paymentIntentProvider.fetchPaymentIntent(
            paymentId,
            onPaymentIntentSuccess,
            onPaymentIntentFailed,
        )

        // assert
        assertTrue(actualPaymentIntentResult.isBlank())
    }

    @Test
    fun `when fetchSetUpIntent success onSetUpIntentSuccess should be invoked`() = runTest {
        // arrange
        val paymentId = "paymentId"
        val setUpIntent = "setUpIntent"
        var actualSetUpIntentResult = ""

        coEvery {
            paymentIntentRepository.getSetUpIntent(any())
        }.answers { DojoPaymentIntentResult.Success(setUpIntent) }
        val onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit = {
            actualSetUpIntentResult = setUpIntent
        }
        val onPaymentIntentFailed: () -> Unit = {}
        // act
        paymentIntentProvider.fetchSetUpIntent(
            paymentId,
            onPaymentIntentSuccess,
            onPaymentIntentFailed,
        )

        // assert
        assertEquals(actualSetUpIntentResult, setUpIntent)
    }

    @Test
    fun `when fetchSetUpIntent fails onSetUpIntentFailed should be invoked`() = runTest {
        // arrange
        val paymentId = "paymentId"
        var actualSetUpIntentResult = ""

        coEvery {
            paymentIntentRepository.getSetUpIntent(any())
        }.answers { DojoPaymentIntentResult.Failed }
        val onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit = {}
        val onPaymentIntentFailed: () -> Unit = {
            actualSetUpIntentResult = ""
        }
        // act
        paymentIntentProvider.fetchSetUpIntent(
            paymentId,
            onPaymentIntentSuccess,
            onPaymentIntentFailed,
        )

        // assert
        assertTrue(actualSetUpIntentResult.isBlank())
    }

    @Test
    fun `when fetchSetUpIntent Throws onSetUpIntentFailed should be invoked`() = runTest {
        // arrange
        val paymentId = "paymentId"
        var actualSetUpIntentResult = ""
        val exception = Exception()
        coEvery {
            paymentIntentRepository.getSetUpIntent(any())
        }.throws(exception)
        val onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit = {}
        val onPaymentIntentFailed: () -> Unit = {
            actualSetUpIntentResult = ""
        }
        // act
        paymentIntentProvider.fetchSetUpIntent(
            paymentId,
            onPaymentIntentSuccess,
            onPaymentIntentFailed,
        )

        // assert
        assertTrue(actualSetUpIntentResult.isBlank())
    }

    @Test
    fun `when refreshPaymentIntent success onPaymentIntentSuccess should be invoked`() = runTest {
        // arrange
        val paymentId = "paymentId"
        val paymentIntent = "paymentIntent"
        var actualPaymentIntentResult = ""

        coEvery {
            paymentIntentRepository.refreshPaymentIntent(any())
        }.answers { DojoPaymentIntentResult.Success(paymentIntent) }
        val onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit = {
            actualPaymentIntentResult = paymentIntent
        }
        val onPaymentIntentFailed: () -> Unit = {}
        // act
        paymentIntentProvider.refreshPaymentIntent(
            paymentId,
            onPaymentIntentSuccess,
            onPaymentIntentFailed,
        )

        // assert
        assertEquals(actualPaymentIntentResult, paymentIntent)
    }

    @Test
    fun `when refreshPaymentIntent fails onPaymentIntentFailed should be invoked`() = runTest {
        // arrange
        val paymentId = "paymentId"
        var actualPaymentIntentResult = ""

        coEvery {
            paymentIntentRepository.refreshPaymentIntent(any())
        }.answers { DojoPaymentIntentResult.Failed }
        val onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit = {}
        val onPaymentIntentFailed: () -> Unit = {
            actualPaymentIntentResult = ""
        }
        // act
        paymentIntentProvider.refreshPaymentIntent(
            paymentId,
            onPaymentIntentSuccess,
            onPaymentIntentFailed,
        )

        // assert
        assertTrue(actualPaymentIntentResult.isBlank())
    }

    @Test
    fun `when refreshPaymentIntent Throws onPaymentIntentFailed should be invoked`() = runTest {
        // arrange
        val paymentId = "paymentId"
        var actualPaymentIntentResult = ""
        val exception = Exception()

        coEvery {
            paymentIntentRepository.refreshPaymentIntent(any())
        }.throws(exception)
        val onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit = {}
        val onPaymentIntentFailed: () -> Unit = {
            actualPaymentIntentResult = ""
        }
        // act
        paymentIntentProvider.refreshPaymentIntent(
            paymentId,
            onPaymentIntentSuccess,
            onPaymentIntentFailed,
        )

        // assert
        assertTrue(actualPaymentIntentResult.isBlank())
    }
}
