package tech.dojo.pay.uisdk.data.paymentintent

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import tech.dojo.pay.uisdk.data.mapper.PaymentIntentPayLoadMapper
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult
import tech.dojo.pay.uisdk.domain.mapper.PaymentIntentDomainEntityMapper

internal class RefreshPaymentIntentRepository(
    private val dataSource: PaymentIntentDataSource = PaymentIntentDataSource(),
    private val paymentIntentDomainEntityMapper: PaymentIntentDomainEntityMapper = PaymentIntentDomainEntityMapper(),
    private val paymentIntentPayLoadMapper: PaymentIntentPayLoadMapper = PaymentIntentPayLoadMapper(),
) {
    private var paymentIntentResult: MutableStateFlow<RefreshPaymentIntentResult?> =
        MutableStateFlow(null)

    fun refreshPaymentIntent(paymentId: String) {
        paymentIntentResult = MutableStateFlow(null)
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
        paymentIntentResult = MutableStateFlow(null)
        dataSource
            .refreshSetupIntent(
                paymentId,
                { paymentIntentPayloadJson -> handleRefreshSuccess(paymentIntentPayloadJson) },
                { handleRefreshFailure() },
            )
    }

    private fun handleRefreshSuccess(paymentIntentPayloadJson: String) {
        try {
            val domainEntity = paymentIntentDomainEntityMapper.apply(
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
