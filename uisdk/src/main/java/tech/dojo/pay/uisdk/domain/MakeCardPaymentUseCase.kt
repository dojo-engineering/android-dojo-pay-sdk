package tech.dojo.pay.uisdk.domain

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import tech.dojo.pay.uisdk.domain.entities.MakeCardPaymentParams
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult

internal class MakeCardPaymentUseCase(
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase,
    private val getRefreshedPaymentTokenFlow: GetRefreshedPaymentTokenFlow,
    private val refreshPaymentIntentUseCase: RefreshPaymentIntentUseCase,
) {
    suspend fun makeCardPayment(
        params: MakeCardPaymentParams,
        onUpdateTokenError: () -> Unit,
    ) {
        updatePaymentStateUseCase.updatePaymentSate(isActive = true)
        refreshPaymentIntentUseCase.refreshPaymentIntent(params.paymentId)
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
        params: MakeCardPaymentParams,
        result: RefreshPaymentIntentResult.Success,
    ) {
        params.dojoCardPaymentHandler.executeCardPayment(
            token = result.token,
            payload = params.fullCardPaymentPayload,
        )
    }
}
