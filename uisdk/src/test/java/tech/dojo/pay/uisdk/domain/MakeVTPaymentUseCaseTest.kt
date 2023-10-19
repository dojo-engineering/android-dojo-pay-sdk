package tech.dojo.pay.uisdk.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import tech.dojo.pay.uisdk.domain.entities.MakeVTPaymentParams
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult

class MakeVTPaymentUseCaseTest {
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase = mock()
    private val getRefreshedPaymentTokenFlow: GetRefreshedPaymentTokenFlow = mock()
    private val refreshPaymentIntentUseCase: RefreshPaymentIntentUseCase = mock()
    private lateinit var makeVTPaymentUseCase: MakeVTPaymentUseCase

    @Before
    fun setup() {
        makeVTPaymentUseCase = MakeVTPaymentUseCase(
            updatePaymentStateUseCase,
            getRefreshedPaymentTokenFlow,
            refreshPaymentIntentUseCase,
        )
    }

    @Test
    fun `when calling makeVTPayment with successful refresh token should start payment process and updatePaymentSate`() =
        runTest {
            // arrange
            val params = MakeVTPaymentParams(
                paymentId = "123",
                fullCardPaymentPayload = mock(),
                virtualTerminalHandler = mock(),
            )
            given(getRefreshedPaymentTokenFlow.getUpdatedPaymentTokenFlow()).willReturn(
                MutableStateFlow(
                    RefreshPaymentIntentResult.Success(token = "token"),
                ),
            )
            // act
            makeVTPaymentUseCase.makeVTPayment(params, {})
            // assert
            verify(updatePaymentStateUseCase).updatePaymentSate(true)
            verify(params.virtualTerminalHandler).executeVirtualTerminalPayment(any(), any())
        }

    @Test
    fun `when calling makePaymentWithUpdatedToken with RefreshFailure for  token should not start payment  and run onUpdateTokenError`() =
        runTest {
            // arrange
            val params = MakeVTPaymentParams(
                paymentId = "123",
                fullCardPaymentPayload = mock(),
                virtualTerminalHandler = mock(),
            )

            given(getRefreshedPaymentTokenFlow.getUpdatedPaymentTokenFlow()).willReturn(
                MutableStateFlow(
                    RefreshPaymentIntentResult.RefreshFailure,
                ),
            )
            val onUpdateTokenError = mock<() -> Unit>()
            // act
            makeVTPaymentUseCase.makeVTPayment(params, onUpdateTokenError)
            // assert
            verify(updatePaymentStateUseCase).updatePaymentSate(false)
            verify(onUpdateTokenError).invoke()
        }
}
