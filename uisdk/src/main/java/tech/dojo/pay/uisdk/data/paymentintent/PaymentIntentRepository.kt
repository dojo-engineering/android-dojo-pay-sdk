package tech.dojo.pay.uisdk.data.paymentintent

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tech.dojo.pay.uisdk.data.mapper.PaymentIntentPayLoadMapper
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.mapper.PaymentIntentDomainEntityMapper

@Suppress("TooGenericExceptionCaught", "SwallowedException")

internal class PaymentIntentRepository(
    private val dataSource: PaymentIntentDataSource = PaymentIntentDataSource(),
    private val paymentIntentDomainEntityMapper: PaymentIntentDomainEntityMapper = PaymentIntentDomainEntityMapper(),
    private val paymentIntentPayLoadMapper: PaymentIntentPayLoadMapper = PaymentIntentPayLoadMapper(),
) {
    private val paymentIntentResult: MutableStateFlow<PaymentIntentResult> =
        MutableStateFlow(PaymentIntentResult.None)

    fun fetchPaymentIntent(paymentId: String) {
        paymentIntentResult.tryEmit(PaymentIntentResult.Fetching)
        dataSource.fetchPaymentIntent(
            paymentId,
            { handleSuccessPaymentIntent(it) },
            { paymentIntentResult.tryEmit(PaymentIntentResult.FetchFailure) },
        )
    }

    fun fetchSetUpIntent(paymentId: String) {
        paymentIntentResult.tryEmit(PaymentIntentResult.Fetching)
        dataSource.fetchSetUpIntent(
            paymentId,
            { handleSuccessPaymentIntent(it) },
            { paymentIntentResult.tryEmit(PaymentIntentResult.FetchFailure) },
        )
    }

    private fun handleSuccessPaymentIntent(it: String) {
        try {
            val domainEntity = paymentIntentDomainEntityMapper.mapPayload(
                paymentIntentPayLoadMapper.mapToPaymentIntentPayLoad(it),
            )
            if (domainEntity != null) {
                paymentIntentResult.tryEmit(PaymentIntentResult.Success(domainEntity))
            } else {
                paymentIntentResult.tryEmit(PaymentIntentResult.FetchFailure)
            }
        } catch (e: Exception) {
            paymentIntentResult.tryEmit(PaymentIntentResult.FetchFailure)
        }
    }

    fun observePaymentIntent(): StateFlow<PaymentIntentResult> = paymentIntentResult.asStateFlow()
}
