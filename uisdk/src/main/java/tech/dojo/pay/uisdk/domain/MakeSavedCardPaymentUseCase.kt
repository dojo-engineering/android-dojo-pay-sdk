package tech.dojo.pay.uisdk.domain

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.uisdk.domain.entities.MakeSavedCardPaymentParams
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult

internal class MakeSavedCardPaymentUseCase(
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase,
    private val getRefreshedPaymentTokenFlow: GetRefreshedPaymentTokenFlow,
    private val refreshPaymentIntentUseCase: RefreshPaymentIntentUseCase,
) {

    suspend fun makePaymentWithUpdatedToken(
        params: MakeSavedCardPaymentParams,
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
                    onSuccessResult(params, result,onError)
                } else if (result is RefreshPaymentIntentResult.RefreshFailure) {
                    onPaymentError(onError)
                }
            }
    }

    private fun onSuccessResult(
        params: MakeSavedCardPaymentParams,
        successResult: RefreshPaymentIntentResult.Success,
        onError: () -> Unit,
    ) {
        try {
            startSavedCardPayment(params, successResult)
        } catch (e: Exception) {
            onPaymentError(onError)
        }
    }

    private fun startSavedCardPayment(
        params: MakeSavedCardPaymentParams,
        successResult: RefreshPaymentIntentResult.Success
    ) {
        params.savedCardPaymentHandler.executeSavedCardPayment(
            token = successResult.token,
            DojoCardPaymentPayLoad.SavedCardPaymentPayLoad(
                cv2 = params.cv2,
                paymentMethodId = params.paymentMethodId,
            ),
        )
    }

    private fun onPaymentError(onError: () -> Unit) {
        updatePaymentStateUseCase.updatePaymentSate(isActive = false)
        onError()
    }
}
