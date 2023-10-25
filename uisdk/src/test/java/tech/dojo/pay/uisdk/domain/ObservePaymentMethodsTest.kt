package tech.dojo.pay.uisdk.domain

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test
import tech.dojo.pay.uisdk.data.paymentmethods.PaymentMethodsRepository
import tech.dojo.pay.uisdk.domain.entities.FetchPaymentMethodsResult

class ObservePaymentMethodsTest {

    @Test
    fun `observe should return payment methods steam`() {
        // given
        val expectedPaymentMethods: MutableStateFlow<FetchPaymentMethodsResult> = MutableStateFlow(FetchPaymentMethodsResult.Fetching)
        val repository = mockk<PaymentMethodsRepository>()
        every { repository.observePaymentMethods() } returns (expectedPaymentMethods)
        val useCase = ObservePaymentMethods(repository)

        // when
        val result = useCase.observe()

        // then
        assert(result == expectedPaymentMethods)
    }
}
