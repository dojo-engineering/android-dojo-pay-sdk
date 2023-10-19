package tech.dojo.pay.uisdk.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import tech.dojo.pay.uisdk.domain.entities.MakeCardPaymentParams
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult

class MakeCardPaymentUseCaseTest {
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase = mock()
    private val getRefreshedPaymentTokenFlow: GetRefreshedPaymentTokenFlow = mock()
    private val refreshPaymentIntentUseCase: RefreshPaymentIntentUseCase = mock()
    private lateinit var makeCardPaymentUseCase: MakeCardPaymentUseCase

    @Before
    fun setup() {
        makeCardPaymentUseCase = MakeCardPaymentUseCase(
            updatePaymentStateUseCase,
            getRefreshedPaymentTokenFlow,
            refreshPaymentIntentUseCase,
        )
    }

    @Test
    fun `when calling makeCardPayment with successful refresh token should start payment process and updatePaymentSate`() =
        runTest {
            // arrange
            val params = MakeCardPaymentParams(
                paymentId = "123",
                fullCardPaymentPayload = mock(),
                dojoCardPaymentHandler = mock(),
            )
            given(getRefreshedPaymentTokenFlow.getUpdatedPaymentTokenFlow()).willReturn(
                MutableStateFlow(
                    RefreshPaymentIntentResult.Success(token = "token"),
                ),
            )
            // act
            makeCardPaymentUseCase.makeCardPayment(params, {})
            // assert
            verify(getRefreshedPaymentTokenFlow).getUpdatedPaymentTokenFlow()
            verify(updatePaymentStateUseCase).updatePaymentSate(true)
            verify(params.dojoCardPaymentHandler).executeCardPayment(any(), any())
            verifyNoMoreInteractions(updatePaymentStateUseCase)
            verifyNoMoreInteractions(getRefreshedPaymentTokenFlow)
        }

    @Test
    fun `when calling makeCardPayment with RefreshFailure for  token should not start payment  and run onUpdateTokenError`() =
        runTest {
            // arrange
            val params = MakeCardPaymentParams(
                paymentId = "123",
                fullCardPaymentPayload = mock(),
                dojoCardPaymentHandler = mock(),
            )

            given(getRefreshedPaymentTokenFlow.getUpdatedPaymentTokenFlow()).willReturn(
                MutableStateFlow(
                    RefreshPaymentIntentResult.RefreshFailure,
                ),
            )
            val onUpdateTokenError = mock<() -> Unit>()
            // act
            makeCardPaymentUseCase.makeCardPayment(params, onUpdateTokenError)
            // assert
            verify(updatePaymentStateUseCase).updatePaymentSate(false)
            verify(onUpdateTokenError).invoke()
        }
}
