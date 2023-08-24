package tech.dojo.pay.uisdk.domain

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import tech.dojo.pay.uisdk.data.paymentintent.RefreshPaymentIntentRepository
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult

class GetRefreshedPaymentTokenFlowTest {

    @Test
    fun `when getUpdatedPaymentTokenFlow should return StateFlow from repo`() {
        // arrange
        val repo = mock<RefreshPaymentIntentRepository>()
        val expectedFlow: StateFlow<RefreshPaymentIntentResult?> = MutableStateFlow(null)
        given(repo.getRefreshedPaymentTokenFlow()).willReturn(expectedFlow)
        val useCase = GetRefreshedPaymentTokenFlow(repo)

        // act
        val actualFlow = useCase.getUpdatedPaymentTokenFlow()

        // assert
        assertEquals(expectedFlow, actualFlow)
    }
}
