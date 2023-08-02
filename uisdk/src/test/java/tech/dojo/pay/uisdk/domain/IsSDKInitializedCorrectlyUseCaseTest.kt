package tech.dojo.pay.uisdk.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.entities.DojoPaymentType

class IsSDKInitializedCorrectlyUseCaseTest {

    @Test
    fun `given Virtual Terminal PaymentIntent, isSDKInitiatedCorrectly should return true`() {
        // Arrange
        val paymentIntent = mock(PaymentIntentDomainEntity::class.java)
        val paymentType = DojoPaymentType.VIRTUAL_TERMINAL
        whenever(paymentIntent.isVirtualTerminalPayment).thenReturn(true)
        val useCase = IsSDKInitializedCorrectlyUseCase()

        // Act
        val result = useCase.isSDKInitiatedCorrectly(paymentIntent, paymentType)

        // Assert
        assertEquals(true, result)
    }

    @Test
    fun `given Setup Intent PaymentIntent, isSDKInitiatedCorrectly should return true`() {
        // Arrange
        val paymentIntent = mock(PaymentIntentDomainEntity::class.java)
        val paymentType = DojoPaymentType.SETUP_INTENT
        whenever(paymentIntent.isSetUpIntentPayment).thenReturn(true)
        val useCase = IsSDKInitializedCorrectlyUseCase()

        // Act
        val result = useCase.isSDKInitiatedCorrectly(paymentIntent, paymentType)

        // Assert
        assertEquals(true, result)
    }

    @Test
    fun `given Payment Card PaymentIntent, isSDKInitiatedCorrectly should return true`() {
        // Arrange
        val paymentIntent = mock(PaymentIntentDomainEntity::class.java)
        val paymentType = DojoPaymentType.PAYMENT_CARD
        val useCase = IsSDKInitializedCorrectlyUseCase()

        // Act
        val result = useCase.isSDKInitiatedCorrectly(paymentIntent, paymentType)

        // Assert
        assertEquals(true, result)
    }

    @Test
    fun `given non-matching PaymentIntent and PaymentType, isSDKInitiatedCorrectly should return false`() {
        // Arrange
        val paymentIntent = mock(PaymentIntentDomainEntity::class.java)
        val paymentType = DojoPaymentType.SETUP_INTENT
        val useCase = IsSDKInitializedCorrectlyUseCase()

        // Act
        val result = useCase.isSDKInitiatedCorrectly(paymentIntent, paymentType)

        // Assert
        assertEquals(false, result)
    }
}
