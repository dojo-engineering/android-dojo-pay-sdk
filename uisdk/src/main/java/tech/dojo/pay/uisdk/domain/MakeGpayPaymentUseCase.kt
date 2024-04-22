package tech.dojo.pay.uisdk.domain

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent
import tech.dojo.pay.uisdk.domain.entities.MakeGpayPaymentParams
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal class MakeGpayPaymentUseCase(
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase,
    private val getRefreshedPaymentTokenFlow: GetRefreshedPaymentTokenFlow,
    private val refreshPaymentIntentUseCase: RefreshPaymentIntentUseCase,
) {

    suspend fun makePaymentWithUpdatedToken(
        params: MakeGpayPaymentParams,
        onError: () -> Unit,
    ) {
        updatePaymentStateUseCase.updateGpayPaymentSate(isActive = true)
        refreshPaymentIntentUseCase.refreshPaymentIntent(params.paymentId)
        getRefreshedPaymentTokenFlow
            .getUpdatedPaymentTokenFlow()
            .filter { it is RefreshPaymentIntentResult.Success || it is RefreshPaymentIntentResult.RefreshFailure }
            .firstOrNull()
            ?.let { result ->
                if (result is RefreshPaymentIntentResult.Success) {
                    onSuccessResult(params, result, onError)
                } else if (result is RefreshPaymentIntentResult.RefreshFailure) {
                    onPaymentFlowError(onError)
                }
            }
    }


    private fun onSuccessResult(
        params: MakeGpayPaymentParams,
        successResult: RefreshPaymentIntentResult.Success,
        onError: () -> Unit,
    ) {
        try {
            startGpayPayment(params, successResult)
        } catch (e: Exception) {
            onPaymentFlowError(onError)
        }
    }

    private fun startGpayPayment(
        params: MakeGpayPaymentParams,
        successResult: RefreshPaymentIntentResult.Success,
    ) {
        params.gpayPaymentHandler.executeGPay(
            GPayPayload = DojoGPayPayload(dojoGPayConfig = params.dojoGPayConfig),
            paymentIntent = DojoPaymentIntent(
                token = successResult.token,
                totalAmount = params.dojoTotalAmount,
            ),
        )
    }

    private fun onPaymentFlowError(onError: () -> Unit) {
        updatePaymentStateUseCase.updateGpayPaymentSate(isActive = false)
        onError()
    }
}
