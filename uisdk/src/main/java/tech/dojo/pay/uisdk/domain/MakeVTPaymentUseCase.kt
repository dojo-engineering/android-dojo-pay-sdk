package tech.dojo.pay.uisdk.domain

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import tech.dojo.pay.uisdk.domain.entities.MakeVTPaymentParams
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult

internal class MakeVTPaymentUseCase(
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase,
    private val getRefreshedPaymentTokenFlow: GetRefreshedPaymentTokenFlow,
    private val refreshPaymentIntentUseCase: RefreshPaymentIntentUseCase,
) {

    suspend fun makeVTPayment(
        params: MakeVTPaymentParams,
        onUpdateTokenError: () -> Unit,
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
                    updatePaymentStateUseCase.updatePaymentSate(isActive = false)
                    onUpdateTokenError()
                }
            }
    }

    private fun onSuccessResult(
        params: MakeVTPaymentParams,
        result: RefreshPaymentIntentResult.Success,
    ) {
        params.virtualTerminalHandler.executeVirtualTerminalPayment(
            token = result.token,
            payload = params.fullCardPaymentPayload,
        )
    }
}
