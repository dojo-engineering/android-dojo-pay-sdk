package tech.dojo.pay.uisdk.domain

import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import tech.dojo.pay.uisdk.data.paymentintent.PaymentIntentRepository

class FetchPaymentIntentUseCaseTest {

    private lateinit var repository: PaymentIntentRepository
    private lateinit var useCase: FetchPaymentIntentUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = FetchPaymentIntentUseCase(repository)
    }

    @Test
    fun `fetchPaymentIntent should call repository fetchPaymentIntent`() {
        // given
        val paymentId = "paymentId"

        // when
        useCase.fetchPaymentIntent(paymentId)

        // then
        verify {
            repository.fetchPaymentIntent(paymentId)
        }
    }
}
