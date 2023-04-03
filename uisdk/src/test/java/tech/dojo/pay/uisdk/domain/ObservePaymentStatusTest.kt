package tech.dojo.pay.uisdk.domain

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test
import tech.dojo.pay.uisdk.data.PaymentStateRepository

class ObservePaymentStatusTest {

    @Test
    fun `observePaymentStates should return payment states stream`() {
        // given
        val expectedPaymentStates: MutableStateFlow<Boolean> = MutableStateFlow(false)
        val repository = mockk<PaymentStateRepository>()
        every { repository.observePaymentIntent() } returns (expectedPaymentStates)
        val useCase = ObservePaymentStatus(repository)

        // when
        val result = useCase.observePaymentStates()

        // then
        assert(result == expectedPaymentStates)
    }

    @Test
    fun `observeGpayPaymentStates should return payment states stream`() {
        // given
        val expectedPaymentStates: MutableStateFlow<Boolean> = MutableStateFlow(false)
        val repository = mockk<PaymentStateRepository>()
        every { repository.observeGpayPaymentIntent() } returns (expectedPaymentStates)
        val useCase = ObservePaymentStatus(repository)

        // when
        val result = useCase.observeGpayPaymentStates()

        // then
        assert(result == expectedPaymentStates)
    }
}
