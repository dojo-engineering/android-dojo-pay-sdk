package tech.dojo.pay.uisdk.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import tech.dojo.pay.sdk.card.entities.WalletSchemes
import tech.dojo.pay.uisdk.domain.entities.DeviceWalletStateResult
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentResult

@OptIn(ExperimentalCoroutinesApi::class)
class IsWalletAvailableFromDeviceAndIntentUseCaseTest {
    private val observePaymentIntent: ObservePaymentIntent = mock()
    private val observeDeviceWalletState: ObserveDeviceWalletState = mock()
    private lateinit var isWalletAvailableUseCase: IsWalletAvailableFromDeviceAndIntentUseCase

    @Before
    fun setup() {
        isWalletAvailableUseCase = IsWalletAvailableFromDeviceAndIntentUseCase(
            observePaymentIntent,
            observeDeviceWalletState,
        )
    }

    @Test
    fun `when calling isAvailable from isWalletAvailableUseCase with wallet available from device and API should return true`() =
        runTest {
            // arrange
            val mockPaymentIntentDomainEntity: PaymentIntentDomainEntity = mock()
            given(mockPaymentIntentDomainEntity.supportedWalletSchemes).willReturn(
                listOf(WalletSchemes.GOOGLE_PAY),
            )
            given(observePaymentIntent.observePaymentIntent()).willReturn(
                MutableStateFlow(
                    PaymentIntentResult.Success(mockPaymentIntentDomainEntity),
                ),
            )
            given(observeDeviceWalletState.observe()).willReturn(
                MutableStateFlow(DeviceWalletStateResult.Enabled),
            )

            // act
            val isAvailable = isWalletAvailableUseCase.isAvailable()

            // assert
            Assert.assertTrue(isAvailable)
            verify(observePaymentIntent).observePaymentIntent()
            verify(observeDeviceWalletState).observe()
        }

    @Test
    fun `when calling isAvailable from isWalletAvailableUseCase with wallet not available from device and but available from API should return false`() =
        runTest {
            // arrange
            val mockPaymentIntentDomainEntity: PaymentIntentDomainEntity = mock()
            given(mockPaymentIntentDomainEntity.supportedWalletSchemes).willReturn(
                listOf(WalletSchemes.GOOGLE_PAY),
            )
            given(observePaymentIntent.observePaymentIntent()).willReturn(
                MutableStateFlow(
                    PaymentIntentResult.Success(mockPaymentIntentDomainEntity),
                ),
            )
            given(observeDeviceWalletState.observe()).willReturn(
                MutableStateFlow(DeviceWalletStateResult.Disabled),
            )
            // act
            val isAvailable = isWalletAvailableUseCase.isAvailable()
            // assert
            Assert.assertTrue(!isAvailable)
            verify(observePaymentIntent).observePaymentIntent()
            verify(observeDeviceWalletState).observe()
        }

    @Test
    fun `when calling isAvailable from isWalletAvailableUseCase with wallet available from device and supported wallet schemes do not contain Google Pay should return false`() =
        runTest {
            // arrange
            val mockPaymentIntentDomainEntity: PaymentIntentDomainEntity = mock()
            given(mockPaymentIntentDomainEntity.supportedWalletSchemes).willReturn(
                listOf(WalletSchemes.APPLE_PAY),
            )
            given(observePaymentIntent.observePaymentIntent()).willReturn(
                MutableStateFlow(
                    PaymentIntentResult.Success(mockPaymentIntentDomainEntity),
                ),
            )
            given(observeDeviceWalletState.observe()).willReturn(
                MutableStateFlow(DeviceWalletStateResult.Enabled),
            )
            // act
            val isAvailable = isWalletAvailableUseCase.isAvailable()
            // assert
            Assert.assertTrue(!isAvailable)
            verify(observePaymentIntent).observePaymentIntent()
            verify(observeDeviceWalletState).observe()
        }

    @Test
    fun `when calling isAvailable from isWalletAvailableUseCase with with wallet  available from device and but not available from API should return false`() =
        runTest {
            // arrange
            given(observePaymentIntent.observePaymentIntent()).willReturn(
                MutableStateFlow(
                    PaymentIntentResult.FetchFailure,
                ),
            )
            given(observeDeviceWalletState.observe()).willReturn(
                MutableStateFlow(DeviceWalletStateResult.Enabled),
            )
            // act
            val isAvailable = isWalletAvailableUseCase.isAvailable()
            // assert
            Assert.assertTrue(!isAvailable)
            verify(observePaymentIntent).observePaymentIntent()
            verify(observeDeviceWalletState).observe()
        }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(observePaymentIntent)
        verifyNoMoreInteractions(observeDeviceWalletState)
    }
}
