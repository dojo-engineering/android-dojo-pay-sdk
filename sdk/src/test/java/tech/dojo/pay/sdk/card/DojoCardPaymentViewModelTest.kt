package tech.dojo.pay.sdk.card

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import tech.dojo.pay.sdk.card.data.CardPaymentRepository
import tech.dojo.pay.sdk.card.data.entities.DeviceData
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

    @Test
    fun `WHEN device data collection completes THEN device data is returned`() = runTest {
        val deviceData = DeviceData("action", "token")
        whenever(repository.collectDeviceData()).thenReturn(deviceData)
        val viewModel = DojoCardPaymentViewModel(repository)
        assertEquals(deviceData, viewModel.deviceData.value)
    }

    @Test
    fun `WHEN device fingerprint is captured THEN payment processing starts`() = runTest {
        val deviceData = DeviceData("action", "token")
        whenever(repository.collectDeviceData()).thenReturn(deviceData)
        val viewModel = DojoCardPaymentViewModel(repository)
        viewModel.onFingerprintCaptured()
        verify(repository).processPayment()
    }

    @Test
    fun `WHEN device fingerprint is not captured AND timeout completes THEN payment processing starts`() = runTest {
        val deviceData = DeviceData("action", "token")
        whenever(repository.collectDeviceData()).thenReturn(deviceData)
        DojoCardPaymentViewModel(repository)
        advanceTimeBy(DojoCardPaymentViewModel.FINGERPRINT_TIMEOUT_MILLIS + 1)
        verify(repository).processPayment()
    }

    @Test
    fun `WHEN payment processing completes THEN payment result is returned AND user can exit`() = runTest {
        val deviceData = DeviceData("action", "token")
        val result = PaymentResult.Completed(DojoCardPaymentResult.SUCCESSFUL)
        whenever(repository.collectDeviceData()).thenReturn(deviceData)
        whenever(repository.processPayment()).thenReturn(result)
        val viewModel = DojoCardPaymentViewModel(repository)
        viewModel.onFingerprintCaptured()
        assertEquals(result, viewModel.paymentResult.value)
        assertTrue(viewModel.canExit)
    }
}