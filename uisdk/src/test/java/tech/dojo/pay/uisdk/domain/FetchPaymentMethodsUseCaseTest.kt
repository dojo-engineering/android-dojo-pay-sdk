package tech.dojo.pay.uisdk.domain

import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import tech.dojo.pay.uisdk.data.paymentmethods.PaymentMethodsRepository

class FetchPaymentMethodsUseCaseTest {

    private lateinit var repository: PaymentMethodsRepository
    private lateinit var useCase: FetchPaymentMethodsUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = FetchPaymentMethodsUseCase(repository)
    }

    @Test
    fun `fetchPaymentMethods should call repository fetchPaymentMethods`() {
        // given
        val customerId = "customerId"
        val customerSecret = "customerSecret"

        // when
        useCase.fetchPaymentMethods(customerId, customerSecret)

        // then
        verify {
            repository.fetchPaymentMethod(customerId, customerSecret)
        }
    }
}
