package tech.dojo.pay.uisdk.domain
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.data.paymentintent.PaymentIntentRepository

class ObservePaymentIntentTest {

    @Test
    fun `observePaymentIntent should return  flow of payment intent`() {
        // given
        val expectedPaymentIntent: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)
        val repository = mockk<PaymentIntentRepository>()
        every { repository.observePaymentIntent() } returns (expectedPaymentIntent)
        val useCase = ObservePaymentIntent(repository)

        // when
        val result = useCase.observePaymentIntent()

        // then
        assert(result == expectedPaymentIntent)
    }
}
