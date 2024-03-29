package tech.dojo.pay.uisdk.data.paymentintent

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
    private var paymentIntentResult: MutableStateFlow<RefreshPaymentIntentResult> = MutableStateFlow(RefreshPaymentIntentResult.None)

    fun refreshPaymentIntent(paymentId: String) {
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
                handleRefreshFailure()
            }
        } catch (e: Exception) {
            handleRefreshFailure()
        }
    }

    private fun handleRefreshFailure() {
        paymentIntentResult.tryEmit(RefreshPaymentIntentResult.RefreshFailure)
    }

    fun getRefreshedPaymentTokenFlow() = paymentIntentResult.asStateFlow()
}
