package tech.dojo.pay.uisdk.domain

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent
import tech.dojo.pay.uisdk.domain.entities.MakeGpayPaymentParams
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult

internal class MakeGpayPaymentUseCase(
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase,
    private val getRefreshedPaymentTokenFlow: GetRefreshedPaymentTokenFlow,
    private val refreshPaymentIntentUseCase: RefreshPaymentIntentUseCase,
) {

    suspend fun makePaymentWithUpdatedToken(
        params: MakeGpayPaymentParams,
        onUpdateTokenError: () -> Unit,
    ) {
        refreshPaymentIntentUseCase.refreshPaymentIntent(params.paymentId)
        getRefreshedPaymentTokenFlow
            .getUpdatedPaymentTokenFlow()
            .filter { it is RefreshPaymentIntentResult.Success || it is RefreshPaymentIntentResult.RefreshFailure }
            .firstOrNull()
            ?.let { result ->
                if (result is RefreshPaymentIntentResult.Success) {
                    onSuccessResult(params, result)
                } else if (result is RefreshPaymentIntentResult.RefreshFailure) {
                    onUpdateTokenError()
                }
            }
    }

    private fun onSuccessResult(
        params: MakeGpayPaymentParams,
        successResult: RefreshPaymentIntentResult.Success,
    ) {
        updatePaymentStateUseCase.updateGpayPaymentSate(isActive = true)
        startGpayPayment(params, successResult)
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
}
