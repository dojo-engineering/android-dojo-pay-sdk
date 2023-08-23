package tech.dojo.pay.uisdk.domain

import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import tech.dojo.pay.uisdk.data.paymentmethods.PaymentMethodsRepository
import tech.dojo.pay.uisdk.entities.DojoPaymentType

class FetchPaymentMethodsUseCaseTest {

    private lateinit var repository: PaymentMethodsRepository
    private lateinit var useCase: FetchPaymentMethodsUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = FetchPaymentMethodsUseCase(repository)
    }

    @Test
    fun `when fetchPaymentMethodsWithPaymentType with payment intent as CARD_ON_FILE should not call repository fetchPaymentMethods`() {
        // given
        val customerId = "customerId"
        val customerSecret = "customerSecret"

        // when
        useCase.fetchPaymentMethodsWithPaymentType(
            DojoPaymentType.SETUP_INTENT,
            customerId,
            customerSecret,
        )

        // then
        verify(exactly = 0) { repository.fetchPaymentMethod(customerId, customerSecret) }
    }

    @Test
    fun `when fetchPaymentMethodsWithPaymentType with payment intent as PAYMENT_CARD should call repository fetchPaymentMethods`() {
        // given
        val customerId = "customerId"
        val customerSecret = "customerSecret"

        // when
        useCase.fetchPaymentMethodsWithPaymentType(
            DojoPaymentType.PAYMENT_CARD,
            customerId,
            customerSecret,
        )

        // then
        verify {
            repository.fetchPaymentMethod(customerId, customerSecret)
        }
    }

    @Test
    fun `when fetchPaymentMethodsWithPaymentType with payment intent as VIRTUAL_TERMINAL should call repository fetchPaymentMethods`() {
        // given
        val customerId = "customerId"
        val customerSecret = "customerSecret"

        // when
        useCase.fetchPaymentMethodsWithPaymentType(
            DojoPaymentType.VIRTUAL_TERMINAL,
            customerId,
            customerSecret,
        )

        // then
        verify {
            repository.fetchPaymentMethod(customerId, customerSecret)
        }
    }
}
