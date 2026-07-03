package tech.dojo.pay.uisdk.data.paymentintent

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import tech.dojo.pay.uisdk.data.mapper.PaymentIntentPayLoadMapper
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult
import tech.dojo.pay.uisdk.domain.mapper.PaymentIntentDomainEntityMapper

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal class RefreshPaymentIntentRepository(
    private val dataSource: PaymentIntentDataSource = PaymentIntentDataSource(),
    private val paymentIntentDomainEntityMapper: PaymentIntentDomainEntityMapper = PaymentIntentDomainEntityMapper(),
    private val paymentIntentPayLoadMapper: PaymentIntentPayLoadMapper = PaymentIntentPayLoadMapper(),
) {
    companion object { private const val TAG = "RefreshPaymentIntentRepo" }

    private var paymentIntentResult: MutableStateFlow<RefreshPaymentIntentResult> = MutableStateFlow(RefreshPaymentIntentResult.None)

    fun refreshPaymentIntent(paymentId: String) {
        Log.d(TAG, "refreshPaymentIntent: Refreshing for paymentId='$paymentId'")
        paymentIntentResult.tryEmit(RefreshPaymentIntentResult.Fetching)
        dataSource
            .refreshPaymentIntent(
                paymentId,
                { paymentIntentPayloadJson -> handleRefreshSuccess(paymentIntentPayloadJson) },
                { handleRefreshFailure() },
            )
    }

    fun refreshSetupIntent(
        paymentId: String,
    ) {
        Log.d(TAG, "refreshSetupIntent: Refreshing setup intent for paymentId='$paymentId'")
        paymentIntentResult.tryEmit(RefreshPaymentIntentResult.Fetching)
        dataSource
            .refreshSetupIntent(
                paymentId,
                { paymentIntentPayloadJson -> handleRefreshSuccess(paymentIntentPayloadJson) },
                { handleRefreshFailure() },
            )
    }

    private fun handleRefreshSuccess(paymentIntentPayloadJson: String) {
        try {
            val domainEntity = paymentIntentDomainEntityMapper.mapPayload(
                paymentIntentPayLoadMapper.mapToPaymentIntentPayLoad(paymentIntentPayloadJson),
            )
            if (domainEntity != null) {
                paymentIntentResult.tryEmit(
                    RefreshPaymentIntentResult.Success(domainEntity.paymentToken),
                )
            } else {
                Log.w(TAG, "handleRefreshSuccess: domainEntity is null after mapping.")
                handleRefreshFailure()
            }
        } catch (e: Exception) {
            Log.e(TAG, "handleRefreshSuccess: Exception during mapping", e)
            handleRefreshFailure()
        }
    }

    private fun handleRefreshFailure() {
        Log.e(TAG, "handleRefreshFailure: Emitting RefreshFailure")
        paymentIntentResult.tryEmit(RefreshPaymentIntentResult.RefreshFailure)
    }

    fun getRefreshedPaymentTokenFlow() = paymentIntentResult.asStateFlow()
}
