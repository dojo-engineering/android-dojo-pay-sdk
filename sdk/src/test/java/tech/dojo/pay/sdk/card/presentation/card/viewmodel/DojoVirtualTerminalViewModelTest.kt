package tech.dojo.pay.sdk.card.presentation.card.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.CardPaymentRepository
import tech.dojo.pay.sdk.card.entities.PaymentResult

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class DojoVirtualTerminalViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val cardPaymentRepository: CardPaymentRepository = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `when payment is successful, post PaymentResult to UI`() = runTest {
        // given
        val result = PaymentResult.Completed(DojoPaymentResult.SUCCESSFUL)
        whenever(cardPaymentRepository.processPayment()).thenReturn(result)
        // when
        val viewModel = DojoVirtualTerminalViewModel(cardPaymentRepository)
        // then
        assertEquals(viewModel.paymentResult.value, result)
    }

    @Test
    fun `when payment fails, post PaymentResult to UI`() = runTest {
        // given
        val result = PaymentResult.Completed(DojoPaymentResult.FAILED)
        whenever(cardPaymentRepository.processPayment()).thenReturn(result)
        // when
        val viewModel = DojoVirtualTerminalViewModel(cardPaymentRepository)
        // then
        assertEquals(viewModel.paymentResult.value, result)
    }
}
