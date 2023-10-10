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
        params: MakeSavedCardPaymentParams,
        successResult: RefreshPaymentIntentResult.Success,
    ) {
        updatePaymentStateUseCase.updatePaymentSate(isActive = true)
        params.savedCardPaymentHandler.executeSavedCardPayment(
            token = successResult.token,
            DojoCardPaymentPayLoad.SavedCardPaymentPayLoad(
                cv2 = params.cv2,
                paymentMethodId = params.paymentMethodId,
            ),
        )
    }
}
