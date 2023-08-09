import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import tech.dojo.pay.uisdk.data.paymentintent.RefreshPaymentIntentRepository
import tech.dojo.pay.uisdk.domain.RefreshPaymentIntentUseCase
import tech.dojo.pay.uisdk.entities.DojoPaymentType

class RefreshPaymentIntentUseCaseTest {

    private lateinit var repo: RefreshPaymentIntentRepository
    private lateinit var useCase: RefreshPaymentIntentUseCase

    @Before
    fun setUp() {
        repo = mock()
    }

    @Test
    fun `when paymentType is PAYMENT_CARD, refreshPaymentIntent should call repo refreshPaymentIntent`() {
        // arrange
        val paymentId = "paymentId"
        useCase = RefreshPaymentIntentUseCase(repo, DojoPaymentType.PAYMENT_CARD)

        // act
        useCase.refreshPaymentIntent(paymentId)

        // assert
        verify(repo).refreshPaymentIntent(paymentId)
        verify(repo, never()).refreshSetupIntent(any())
    }

    @Test
    fun `when paymentType is SETUP_INTENT, refreshPaymentIntent should call repo refreshSetupIntent`() {
        // arrange
        val paymentId = "paymentId"
        useCase = RefreshPaymentIntentUseCase(repo, DojoPaymentType.SETUP_INTENT)

        // act
        useCase.refreshPaymentIntent(paymentId)

        // assert
        verify(repo, never()).refreshPaymentIntent(any())
        verify(repo).refreshSetupIntent(paymentId)
    }

    @Test
    fun `when paymentType is VIRTUAL_TERMINAL, refreshPaymentIntent should call repo refreshPaymentIntent`() {
        // arrange
        val paymentId = "paymentId"
        useCase = RefreshPaymentIntentUseCase(repo, DojoPaymentType.VIRTUAL_TERMINAL)

        // act
        useCase.refreshPaymentIntent(paymentId)

        // assert
        verify(repo).refreshPaymentIntent(paymentId)
        verify(repo, never()).refreshSetupIntent(any())
    }
}
