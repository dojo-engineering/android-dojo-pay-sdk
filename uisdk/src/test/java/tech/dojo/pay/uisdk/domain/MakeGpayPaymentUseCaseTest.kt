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
import tech.dojo.pay.sdk.card.entities.DojoTotalAmount
import tech.dojo.pay.uisdk.domain.entities.MakeGpayPaymentParams
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult

class MakeGpayPaymentUseCaseTest {
    private lateinit var updatePaymentStateUseCase: UpdatePaymentStateUseCase
    private lateinit var getRefreshedPaymentTokenFlow: GetRefreshedPaymentTokenFlow
    private lateinit var refreshPaymentIntentUseCase: RefreshPaymentIntentUseCase
    private lateinit var makeGpayPaymentUseCase: MakeGpayPaymentUseCase

    @Before
    fun setup() {
        updatePaymentStateUseCase = mock()
        getRefreshedPaymentTokenFlow = mock()
        refreshPaymentIntentUseCase = mock()
        makeGpayPaymentUseCase = MakeGpayPaymentUseCase(
            updatePaymentStateUseCase,
            getRefreshedPaymentTokenFlow,
            refreshPaymentIntentUseCase,
        )
    }

    @Test
    fun `calling makePaymentWithUpdatedToken with successful refresh token should start payment process and updateGpayPaymentSate`() =
        runTest {
            // arrange
            val params = MakeGpayPaymentParams(
                paymentId = "123",
                gpayPaymentHandler = mock(),
                dojoGPayConfig = mock(),
                dojoTotalAmount = DojoTotalAmount(
                    amount = 100L,
                    currencyCode = "",
                ),
            )
            given(getRefreshedPaymentTokenFlow.getUpdatedPaymentTokenFlow()).willReturn(
                MutableStateFlow(
                    RefreshPaymentIntentResult.Success(token = "token"),
                ),
            )
            // act
            makeGpayPaymentUseCase.makePaymentWithUpdatedToken(params, {})
            // assert
            verify(updatePaymentStateUseCase).updateGpayPaymentSate(true)
            verify(params.gpayPaymentHandler).executeGPay(any(), any())
        }

    @Test
    fun `calling makePaymentWithUpdatedToken with RefreshFailure for  token should not start payment  and run onUpdateTokenError`() =
        runTest {
            // arrange
            val params = MakeGpayPaymentParams(
                paymentId = "123",
                gpayPaymentHandler = mock(),
                dojoGPayConfig = mock(),
                dojoTotalAmount = DojoTotalAmount(
                    amount = 100L,
                    currencyCode = "",
                ),
            )

            given(getRefreshedPaymentTokenFlow.getUpdatedPaymentTokenFlow()).willReturn(
                MutableStateFlow(
                    RefreshPaymentIntentResult.RefreshFailure,
                ),
            )
            val onUpdateTokenError = mock<() -> Unit>()
            // act
            makeGpayPaymentUseCase.makePaymentWithUpdatedToken(params, onUpdateTokenError)
            // assert
            verify(updatePaymentStateUseCase, never()).updateGpayPaymentSate(true)
            verify(onUpdateTokenError).invoke()
        }
}
