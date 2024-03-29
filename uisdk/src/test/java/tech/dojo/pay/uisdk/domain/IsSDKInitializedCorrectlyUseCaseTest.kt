package tech.dojo.pay.uisdk.domain

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.entities.DojoPaymentType

class IsSDKInitializedCorrectlyUseCaseTest {
    private lateinit var useCase: IsSDKInitializedCorrectlyUseCase

    @Before
    fun setUp() {
        useCase = IsSDKInitializedCorrectlyUseCase()
    }

    @Test
    fun `when calling isSDKInitiatedCorrectly with Virtual Terminal PaymentIntent, isSDKInitiatedCorrectly then should return true`() {
        // Given
        val paymentIntent: PaymentIntentDomainEntity = mock()
        val paymentType = DojoPaymentType.VIRTUAL_TERMINAL
        given(paymentIntent.isVirtualTerminalPayment).willReturn(true)

        // when
        val result = useCase.isSDKInitiatedCorrectly(paymentIntent, paymentType)

        // then
        assertEquals(true, result)
    }

    @Test
    fun `when calling isSDKInitiatedCorrectly with  Setup Intent PaymentIntent, isSDKInitiatedCorrectly then should return true`() {
        // Given
        val paymentIntent: PaymentIntentDomainEntity = mock()
        val paymentType = DojoPaymentType.SETUP_INTENT
        given(paymentIntent.isSetUpIntentPayment).willReturn(true)

        // when
        val result = useCase.isSDKInitiatedCorrectly(paymentIntent, paymentType)

        // then
        assertEquals(true, result)
    }

    @Test
    fun `when calling isSDKInitiatedCorrectly with  Payment Card PaymentIntent, isSDKInitiatedCorrectly then should return true`() {
        // Given
        val paymentIntent: PaymentIntentDomainEntity = mock()
        val paymentType = DojoPaymentType.PAYMENT_CARD

        // when
        val result = useCase.isSDKInitiatedCorrectly(paymentIntent, paymentType)

        // then
        assertEquals(true, result)
    }

    @Test
    fun `when calling isSDKInitiatedCorrectly  non-matching PaymentIntent and PaymentType, isSDKInitiatedCorrectly then  should return false`() {
        // Given
        val paymentIntent: PaymentIntentDomainEntity = mock()
        val paymentType = DojoPaymentType.SETUP_INTENT

        // when
        val result = useCase.isSDKInitiatedCorrectly(paymentIntent, paymentType)

        // then
        assertEquals(false, result)
    }
}
