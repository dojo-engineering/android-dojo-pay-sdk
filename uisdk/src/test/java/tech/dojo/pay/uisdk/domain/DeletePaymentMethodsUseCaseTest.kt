package tech.dojo.pay.uisdk.domain

import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import tech.dojo.pay.uisdk.data.paymentmethods.PaymentMethodsRepository
import tech.dojo.pay.uisdk.domain.entities.DeletePaymentMethodsResult

class DeletePaymentMethodsUseCaseTest {

    private lateinit var repository: PaymentMethodsRepository
    private lateinit var useCase: DeletePaymentMethodsUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = DeletePaymentMethodsUseCase(
            "customerId",
            "customerSecret",
            repository
        )
    }

    @Test
    fun `deletePaymentMethods should call repository deletePaymentMethods`() {
        // given
        val paymentMethodId = "paymentMethodId"
        val onSuccess = mockk<(DeletePaymentMethodsResult) -> Unit>()
        val onFailure = mockk<(DeletePaymentMethodsResult) -> Unit>()

        // when
        useCase.deletePaymentMethods(paymentMethodId, onSuccess, onFailure)

        // then
        verify {
            repository.deletePaymentMethods(
                "customerId",
                "customerSecret",
                paymentMethodId,
                onSuccess,
                onFailure
            )
        }
    }
}
