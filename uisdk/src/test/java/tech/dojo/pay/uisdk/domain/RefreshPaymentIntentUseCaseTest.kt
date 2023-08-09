package tech.dojo.pay.uisdk.domain

import io.mockk.mockk
import org.junit.Test
import tech.dojo.pay.uisdk.data.paymentintent.PaymentIntentRepository

class RefreshPaymentIntentUseCaseTest {

    private val mockPaymentIntentRepository = mockk<PaymentIntentRepository>()
    private val refreshPaymentIntentUseCase = RefreshPaymentIntentUseCase(mockPaymentIntentRepository)

    @Test
    fun `should refresh payment intent`() {
        // given
        val paymentId = "payment_123"
//        every { mockPaymentIntentRepository.refreshPaymentIntent(paymentId) } returns Unit

        // when
        refreshPaymentIntentUseCase.refreshPaymentIntent(paymentId)

        // then
//        verify { mockPaymentIntentRepository.refreshPaymentIntent(paymentId) }
    }
}
