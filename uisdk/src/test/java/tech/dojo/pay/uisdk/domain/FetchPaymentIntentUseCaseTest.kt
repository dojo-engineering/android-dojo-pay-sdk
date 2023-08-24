package tech.dojo.pay.uisdk.domain

import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import tech.dojo.pay.uisdk.data.paymentintent.PaymentIntentRepository
import tech.dojo.pay.uisdk.entities.DojoPaymentType

class FetchPaymentIntentUseCaseTest {

    private lateinit var repository: PaymentIntentRepository
    private lateinit var useCase: FetchPaymentIntentUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = FetchPaymentIntentUseCase(repository)
    }

    @Test
    fun `given calling fetchPaymentIntentWithPaymentType with payment type as PAYMENT_CARD should call repository fetchPaymentIntent`() {
        // given
        val paymentId = "paymentId"

        // when
        useCase.fetchPaymentIntentWithPaymentType(DojoPaymentType.PAYMENT_CARD, paymentId)

        // then
        verify {
            repository.fetchPaymentIntent(paymentId)
        }
    }

    @Test
    fun `given calling fetchPaymentIntentWithPaymentType with payment type as VIRTUAL_TERMINAL should call repository fetchPaymentIntent`() {
        // given
        val paymentId = "paymentId"

        // when
        useCase.fetchPaymentIntentWithPaymentType(DojoPaymentType.VIRTUAL_TERMINAL, paymentId)

        // then
        verify {
            repository.fetchPaymentIntent(paymentId)
        }
    }

    @Test
    fun `given calling fetchPaymentIntentWithPaymentType with payment type as CARD_ON_FILE should call repository fetchSetUpIntent`() {
        // given
        val paymentId = "paymentId"

        // when
        useCase.fetchPaymentIntentWithPaymentType(DojoPaymentType.SETUP_INTENT, paymentId)

        // then
        verify {
            repository.fetchSetUpIntent(paymentId)
        }
    }
}
