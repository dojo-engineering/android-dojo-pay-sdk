package tech.dojo.pay.uisdk.domain

import android.util.Log
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import tech.dojo.pay.uisdk.domain.entities.MakeCardPaymentParams
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal class MakeCardPaymentUseCase(
    private val updatePaymentStateUseCase: UpdatePaymentStateUseCase,
    private val getRefreshedPaymentTokenFlow: GetRefreshedPaymentTokenFlow,
    private val refreshPaymentIntentUseCase: RefreshPaymentIntentUseCase,
) {
    companion object { private const val TAG = "MakeCardPaymentUseCase" }

    suspend fun makeCardPayment(params: MakeCardPaymentParams, onError: () -> Unit) {
        updatePaymentStateUseCase.updatePaymentSate(isActive = true)
        refreshPaymentIntentUseCase.refreshPaymentIntent(params.paymentId)
        getRefreshedPaymentTokenFlow
            .getUpdatedPaymentTokenFlow()
            .filter { it is RefreshPaymentIntentResult.Success || it is RefreshPaymentIntentResult.RefreshFailure }
            .firstOrNull()
            ?.let { result ->
                if (result is RefreshPaymentIntentResult.Success) {
                    onSuccessResult(params, result, onError)
                } else if (result is RefreshPaymentIntentResult.RefreshFailure) {
                    Log.e(TAG, "makeCardPayment: Token refresh failed.")
                    onMakingPaymentError(onError)
                }
            }
    }

    private fun onSuccessResult(params: MakeCardPaymentParams, result: RefreshPaymentIntentResult.Success, onError: () -> Unit) {
        try {
            startCardPayment(params, result)
        } catch (e: Exception) {
            Log.e(TAG, "onSuccessResult: startCardPayment threw exception", e)
            onMakingPaymentError(onError)
        }
    }

    private fun startCardPayment(params: MakeCardPaymentParams, result: RefreshPaymentIntentResult.Success) {
        params.dojoCardPaymentHandler.executeCardPayment(
            token = result.token,
            payload = params.fullCardPaymentPayload,
        )
    }

    private fun onMakingPaymentError(onError: () -> Unit) {
        updatePaymentStateUseCase.updatePaymentSate(isActive = false)
        onError()
    }
}
