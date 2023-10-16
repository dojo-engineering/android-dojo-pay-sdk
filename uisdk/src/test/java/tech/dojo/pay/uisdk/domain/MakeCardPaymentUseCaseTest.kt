package tech.dojo.pay.uisdk.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import tech.dojo.pay.uisdk.domain.entities.MakeCardPaymentParams
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult

class MakeCardPaymentUseCaseTest {
    private lateinit var updatePaymentStateUseCase: UpdatePaymentStateUseCase
    private lateinit var getRefreshedPaymentTokenFlow: GetRefreshedPaymentTokenFlow
    private lateinit var refreshPaymentIntentUseCase: RefreshPaymentIntentUseCase
    private lateinit var makeCardPaymentUseCase: MakeCardPaymentUseCase

    @Before
    fun setup() {
        updatePaymentStateUseCase = mock()
        getRefreshedPaymentTokenFlow = mock()
        refreshPaymentIntentUseCase = mock()
        makeCardPaymentUseCase = MakeCardPaymentUseCase(
            updatePaymentStateUseCase,
            getRefreshedPaymentTokenFlow,
            refreshPaymentIntentUseCase,
        )
    }

    @Test
    fun `calling makeCardPayment with successful refresh token should start payment process and updatePaymentSate`() =
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
            verify(updatePaymentStateUseCase).updatePaymentSate(true)
            verify(params.dojoCardPaymentHandler).executeCardPayment(any(), any())
        }

    @Test
    fun `calling makeCardPayment with RefreshFailure for  token should not start payment  and run onUpdateTokenError`() =
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
