package tech.dojo.pay.sdk.card.presentation.card.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.CardPaymentRepository
import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.entities.PaymentResult

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class DojoCardPaymentViewModelTest {

//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    @Mock
//    lateinit var repository: CardPaymentRepository
//
//    @Mock
//    lateinit var configuredCardinalInstance: Cardinal
//
//    @Before
//    fun setup() {
//        Dispatchers.setMain(UnconfinedTestDispatcher())
//    }
//
//    @Test
//    fun `WHEN device data collection fails THEN sdk internal error is returned`() = runTest {
//        // arrange
//        whenever(repository.collectDeviceData()).thenThrow(IllegalArgumentException())
//        val expected = PaymentResult.Completed(DojoPaymentResult.FAILED)
//        // act
//        val viewModel = DojoCardPaymentViewModel(repository, configuredCardinalInstance)
//        // assert
//        assertEquals(expected, viewModel.paymentResult.value)
//    }
//
//    @Test
//    fun `WHEN device data collection completes THEN device data is returned`() = runTest {
//        // arrange
//        val deviceData = DeviceData("action", "token")
//        whenever(repository.collectDeviceData()).thenReturn(deviceData)
//        // act
//        val viewModel = DojoCardPaymentViewModel(repository, configuredCardinalInstance)
//        // assert
//        assertEquals(deviceData, viewModel.deviceData.value)
//    }
//
//
//    @Test
//    fun `when initCardinal called init from configuredCardinalInstance should be called`() =
//        runTest {
//            // arrange
//            val deviceData = DeviceData("action", "token")
//            whenever(repository.collectDeviceData()).thenReturn(deviceData)
//            val viewModel = DojoCardPaymentViewModel(repository, configuredCardinalInstance)
//            // act
//            viewModel.initCardinal()
//            // assert
//            verify(configuredCardinalInstance).init(any(), any())
//        }
//
//    @Test
//    fun `when onSetupCompleted called processPayment from repository should be called`() = runTest {
//        // arrange
//        val deviceData = DeviceData("action", "token")
//        whenever(repository.collectDeviceData()).thenReturn(deviceData)
//        val viewModel = DojoCardPaymentViewModel(repository, configuredCardinalInstance)
//        // act
//        viewModel.onSetupCompleted("")
//
//        // assert
//        verify(repository).processPayment()
//    }
//
//    @Test
//    fun `calling onValidated should THEN  FAILED payment result is returned `() = runTest {
//        // arrange
//        val result = PaymentResult.Completed(DojoPaymentResult.FAILED)
//        val viewModel = DojoCardPaymentViewModel(repository, configuredCardinalInstance)
//        // act
//        viewModel.onValidated(null, null)
//        // assert
//        assertEquals(result, viewModel.paymentResult.value)
//    }
//
//
//    @Test
//    fun `WHEN payment processing completes from onSetupCompleted THEN payment result is returned AND user can exit`() =
//        runTest {
//            // arrange
//            val deviceData = DeviceData("action", "token")
//            val result = PaymentResult.Completed(DojoPaymentResult.SUCCESSFUL)
//            whenever(repository.collectDeviceData()).thenReturn(deviceData)
//            whenever(repository.processPayment()).thenReturn(result)
//            val viewModel = DojoCardPaymentViewModel(repository, configuredCardinalInstance)
//            // act
//            viewModel.onSetupCompleted("")
//            // assert
//            assertEquals(result, viewModel.paymentResult.value)
//            assertTrue(viewModel.canExit)
//        }
//
//    @Test
//    fun `WHEN payment processing completes  with error from onSetupCompleted THEN  FAILED payment result is returned `() =
//        runTest {
//            // arrange
//            val deviceData = DeviceData("action", "token")
//            val result = PaymentResult.Completed(DojoPaymentResult.FAILED)
//            whenever(repository.collectDeviceData()).thenReturn(deviceData)
//            whenever(repository.processPayment()).thenThrow(IllegalArgumentException())
//            val viewModel = DojoCardPaymentViewModel(repository, configuredCardinalInstance)
//            // act
//            viewModel.onSetupCompleted("")
//            // assert
//            assertEquals(result, viewModel.paymentResult.value)
//        }
//
//    @Test
//    fun `when on3dsCompleted called processAuthorization should be called from repo `() = runTest {
//        // arrange
//        val viewModel = DojoCardPaymentViewModel(repository, configuredCardinalInstance)
//        // act
//        viewModel.on3dsCompleted(validateResponse = validateResponse)
//        // assert
//        verify(repository).processAuthorization(any(), any())
//    }
//
//    @Test
//    fun `WHEN processAuthorization completes from on3dsCompleted THEN payment result is returned AND user can exit`() =
//        runTest {
//            // arrange
//            val deviceData = DeviceData("action", "token")
//            val result = PaymentResult.Completed(DojoPaymentResult.SUCCESSFUL)
//            whenever(repository.collectDeviceData()).thenReturn(deviceData)
//            whenever(repository.processAuthorization(any(), any())).thenReturn(result)
//            val viewModel = DojoCardPaymentViewModel(repository, configuredCardinalInstance)
//            // act
//            viewModel.on3dsCompleted(validateResponse = validateResponse)
//            // assert
//            assertEquals(result, viewModel.paymentResult.value)
//            assertTrue(viewModel.canExit)
//        }
//
//    @Test
//    fun `WHEN processAuthorization completes from on3dsCompleted THEN FAILED payment result is returned `() =
//        runTest {
//            // arrange
//            val deviceData = DeviceData("action", "token")
//            val result = PaymentResult.Completed(DojoPaymentResult.FAILED)
//            whenever(repository.collectDeviceData()).thenReturn(deviceData)
//            whenever(repository.processAuthorization(any(), any())).thenThrow(IllegalArgumentException())
//            val viewModel = DojoCardPaymentViewModel(repository, configuredCardinalInstance)
//            // act
//            viewModel.on3dsCompleted(validateResponse = validateResponse)
//            // assert
//            assertEquals(result, viewModel.paymentResult.value)
//        }
}
