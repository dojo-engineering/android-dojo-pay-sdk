package tech.dojo.pay.uisdk.domain

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import tech.dojo.pay.uisdk.domain.entities.MakeVTPaymentParams
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal class MakeVTPaymentUseCase(
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase,
    private val getRefreshedPaymentTokenFlow: GetRefreshedPaymentTokenFlow,
    private val refreshPaymentIntentUseCase: RefreshPaymentIntentUseCase,
) {

    suspend fun makeVTPayment(
        params: MakeVTPaymentParams,
        onError: () -> Unit,
    ) {
        refreshPaymentIntentUseCase.refreshPaymentIntent(params.paymentId)
        updatePaymentStateUseCase.updatePaymentSate(isActive = true)
        getRefreshedPaymentTokenFlow
            .getUpdatedPaymentTokenFlow()
            .filter { it is RefreshPaymentIntentResult.Success || it is RefreshPaymentIntentResult.RefreshFailure }
            .firstOrNull()
            ?.let { result ->
                if (result is RefreshPaymentIntentResult.Success) {
                    onSuccessResult(params, result)
                } else if (result is RefreshPaymentIntentResult.RefreshFailure) {
                    onPaymentError(onError)
                }
            }
    }

    private fun onSuccessResult(
        params: MakeVTPaymentParams,
        result: RefreshPaymentIntentResult.Success,
    ) {
        try {
            startVTPayment(params, result)
        } catch (e: Exception) {
            onPaymentError { }
        }
    }

    private fun startVTPayment(
        params: MakeVTPaymentParams,
        result: RefreshPaymentIntentResult.Success
    ) {
        params.virtualTerminalHandler.executeVirtualTerminalPayment(
            token = result.token,
            payload = params.fullCardPaymentPayload,
        )
    }

    private fun onPaymentError(onError: () -> Unit) {
        updatePaymentStateUseCase.updatePaymentSate(isActive = false)
        onError()
    }
}
