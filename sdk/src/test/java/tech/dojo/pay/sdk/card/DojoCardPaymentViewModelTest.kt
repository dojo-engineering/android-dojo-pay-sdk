package tech.dojo.pay.sdk.card

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
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import tech.dojo.pay.sdk.card.data.CardPaymentRepository
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult
import tech.dojo.pay.sdk.card.entities.PaymentResult
import java.lang.IllegalArgumentException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class DojoCardPaymentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: CardPaymentRepository

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `WHEN device data collection fails THEN sdk internal error is returned`() = runTest {
        whenever(repository.collectDeviceData()).thenThrow(IllegalArgumentException())
        val viewModel = DojoCardPaymentViewModel(repository)
        val expected = PaymentResult.Completed(DojoCardPaymentResult.SDK_INTERNAL_ERROR)
        assertEquals(expected, viewModel.paymentResult.value)
    }
}