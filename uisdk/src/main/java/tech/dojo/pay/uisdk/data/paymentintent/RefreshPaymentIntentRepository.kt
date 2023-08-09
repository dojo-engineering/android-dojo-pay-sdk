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
        dataSource
            .refreshPaymentIntent(
                paymentId,
                { handleRefreshSuccess(it) },
                { handleRefreshFailure() },
            )
    }

    fun refreshSetupIntent(
        paymentId: String,
    ) {
        dataSource
            .refreshSetupIntent(
                paymentId,
                { handleRefreshSuccess(it) },
                { handleRefreshFailure() },
            )
    }

    private fun handleRefreshSuccess(it: String) {
        try {
            paymentIntentResult.tryEmit(
                RefreshPaymentIntentResult.Success(
                    paymentIntentDomainEntityMapper.apply(
                        paymentIntentPayLoadMapper.mapToPaymentIntentPayLoad(it),
                    ).paymentToken,
                ),
            )
        } catch (e: Exception) {
            handleRefreshFailure()
        }
    }

    private fun handleRefreshFailure() {
        paymentIntentResult.tryEmit(RefreshPaymentIntentResult.RefreshFailure)
    }

    fun getRefreshedPaymentTokenFlow() = paymentIntentResult.asStateFlow()
}
