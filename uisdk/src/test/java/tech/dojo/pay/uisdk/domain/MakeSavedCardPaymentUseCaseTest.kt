package tech.dojo.pay.uisdk.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import tech.dojo.pay.uisdk.domain.entities.MakeSavedCardPaymentParams
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult

class MakeSavedCardPaymentUseCaseTest {
    private lateinit var updatePaymentStateUseCase: UpdatePaymentStateUseCase
    private lateinit var getRefreshedPaymentTokenFlow: GetRefreshedPaymentTokenFlow
    private lateinit var refreshPaymentIntentUseCase: RefreshPaymentIntentUseCase
    private lateinit var makeSavedCardPaymentUseCase: MakeSavedCardPaymentUseCase

    @Before
    fun setup() {
        updatePaymentStateUseCase = mock()
        getRefreshedPaymentTokenFlow = mock()
        refreshPaymentIntentUseCase = mock()
        makeSavedCardPaymentUseCase = MakeSavedCardPaymentUseCase(
            updatePaymentStateUseCase,
            getRefreshedPaymentTokenFlow,
            refreshPaymentIntentUseCase,
        )
    }

    @Test
    fun `calling makePaymentWithUpdatedToken with successful refresh token should start payment process and updateGpayPaymentSate`() = runTest {
        // arrange
        val params = MakeSavedCardPaymentParams(
            paymentId = "123",
            savedCardPaymentHandler = mock(),
            cv2 = "123",
            paymentMethodId = "456",
        )

        given(getRefreshedPaymentTokenFlow.getUpdatedPaymentTokenFlow()).willReturn(
            MutableStateFlow(
                RefreshPaymentIntentResult.Success(token = "token"),
            ),
        )
        // act
        makeSavedCardPaymentUseCase.makePaymentWithUpdatedToken(params, {})
        // assert
        verify(updatePaymentStateUseCase).updatePaymentSate(true)
        verify(params.savedCardPaymentHandler).executeSavedCardPayment(any(), any())
    }

    @Test
    fun `calling makePaymentWithUpdatedToken with RefreshFailure for token should not start payment and run onUpdateTokenError`() = runTest {
        // arrange
        val params = MakeSavedCardPaymentParams(
            paymentId = "123",
            savedCardPaymentHandler = mock(),
            cv2 = "123",
            paymentMethodId = "456",
        )

        given(getRefreshedPaymentTokenFlow.getUpdatedPaymentTokenFlow()).willReturn(
            MutableStateFlow(
                RefreshPaymentIntentResult.RefreshFailure,
            ),
        )
        // act
        val onUpdateTokenError = mock<() -> Unit>()
        makeSavedCardPaymentUseCase.makePaymentWithUpdatedToken(params, onUpdateTokenError)
        // assert
        verify(updatePaymentStateUseCase, never()).updatePaymentSate(true)
        verify(onUpdateTokenError).invoke()
    }
}
