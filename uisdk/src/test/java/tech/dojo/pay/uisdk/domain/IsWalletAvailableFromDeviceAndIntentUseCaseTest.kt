package tech.dojo.pay.uisdk.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import tech.dojo.pay.sdk.card.entities.WalletSchemes
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentResult

class IsWalletAvailableFromDeviceAndIntentUseCaseTest {
    private lateinit var observePaymentIntent: ObservePaymentIntent
    private lateinit var observeDeviceWalletState: ObserveDeviceWalletState
    private lateinit var isWalletAvailableUseCase: IsWalletAvailableFromDeviceAndIntentUseCase

    @Before
    fun setup() {
        observePaymentIntent = mock()
        observeDeviceWalletState = mock()
        isWalletAvailableUseCase = IsWalletAvailableFromDeviceAndIntentUseCase(
            observePaymentIntent,
            observeDeviceWalletState,
        )
    }

    @Test
    fun `when calling isAvailable from isWalletAvailableUseCase with with wallet  available from device and API should return true`() =
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
                MutableStateFlow(true),
            )
            // act
            val isAvailable = isWalletAvailableUseCase.isAvailable()
            // assert
            assert(isAvailable)
        }

    @Test
    fun `when calling isAvailable from isWalletAvailableUseCase with with wallet not available from device and but available from API should return false`() =
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
                MutableStateFlow(false),
            )
            // act
            val isAvailable = isWalletAvailableUseCase.isAvailable()
            // assert
            assert(!isAvailable)
        }

    @Test
    fun `when calling isAvailable from isWalletAvailableUseCase with with wallet  available from device and supported wallet schemes do not contain Google Pay should return false`() =
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
                MutableStateFlow(true),
            )
            // act
            val isAvailable = isWalletAvailableUseCase.isAvailable()
            // assert
            assert(!isAvailable)
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
                MutableStateFlow(true),
            )
            // act
            val isAvailable = isWalletAvailableUseCase.isAvailable()
            // assert
            assert(!isAvailable)
        }
}