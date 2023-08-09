import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tech.dojo.pay.uisdk.data.paymentintent.RefreshPaymentIntentRepository
import tech.dojo.pay.uisdk.domain.GetRefreshedPaymentTokenFlow
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult

class GetRefreshedPaymentTokenFlowTest {

    @Test
    fun `getUpdatedPaymentTokenFlow should return StateFlow from repo`() {
        // arrange
        val repo = mock<RefreshPaymentIntentRepository>()
        val expectedFlow: StateFlow<RefreshPaymentIntentResult?> = MutableStateFlow(null)
        whenever(repo.getRefreshedPaymentTokenFlow()).thenReturn(expectedFlow)
        val useCase = GetRefreshedPaymentTokenFlow(repo)

        // act
        val actualFlow = useCase.getUpdatedPaymentTokenFlow()

        // assert
        assertEquals(expectedFlow, actualFlow)
    }
}
