package tech.dojo.pay.uisdk.domain

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import tech.dojo.pay.uisdk.data.PaymentStateRepository

class UpdatePaymentStateUseCaseTest {

    private lateinit var paymentStateRepository: PaymentStateRepository
    private lateinit var useCase: UpdatePaymentStateUseCase

    @Before
    fun setUp() {
        paymentStateRepository = mockk()
        useCase = UpdatePaymentStateUseCase(paymentStateRepository)
    }

    @Test
    fun `updatePaymentSate with isActive = true`() {
        every { paymentStateRepository.updatePayment(any()) } just Runs

        useCase.updatePaymentSate(true)

        verify(exactly = 1) { paymentStateRepository.updatePayment(true) }
    }

    @Test
    fun `updateGpayPaymentSate with isActive = false`() {
        every { paymentStateRepository.updateGpayPayment(any()) } just Runs

        useCase.updateGpayPaymentSate(false)

        verify(exactly = 1) { paymentStateRepository.updateGpayPayment(false) }
    }
}
