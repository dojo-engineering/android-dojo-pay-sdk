package tech.dojo.pay.uisdk.data.paymentintent

import kotlinx.coroutines.flow.MutableStateFlow
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.data.mapper.PaymentIntentPayLoadMapper
import tech.dojo.pay.uisdk.domain.mapper.PaymentIntentDomainEntityMapper

@Suppress("TooGenericExceptionCaught", "SwallowedException")

internal class PaymentIntentRepository(
    private val dataSource: PaymentIntentDataSource = PaymentIntentDataSource(),
    private val paymentIntentDomainEntityMapper: PaymentIntentDomainEntityMapper = PaymentIntentDomainEntityMapper(),
    private val paymentIntentPayLoadMapper: PaymentIntentPayLoadMapper = PaymentIntentPayLoadMapper(),
) {
    private lateinit var paymentIntentResult: MutableStateFlow<PaymentIntentResult?>

    fun fetchPaymentIntent(paymentId: String) {
        paymentIntentResult = MutableStateFlow(null)
        dataSource.fetchPaymentIntent(
            paymentId,
            { handleSuccessPaymentIntent(it) },
            { paymentIntentResult.tryEmit(PaymentIntentResult.FetchFailure) },
        )
    }

    fun fetchSetUpIntent(paymentId: String) {
        paymentIntentResult = MutableStateFlow(null)
        dataSource.fetchSetUpIntent(
            paymentId,
            { handleSuccessPaymentIntent(it) },
            { paymentIntentResult.tryEmit(PaymentIntentResult.FetchFailure) },
        )
    }

    private fun handleSuccessPaymentIntent(it: String) {
        try {
            val domainEntity = paymentIntentDomainEntityMapper.apply(
                paymentIntentPayLoadMapper.mapToPaymentIntentPayLoad(it),
            )
            paymentIntentResult.tryEmit(PaymentIntentResult.Success(domainEntity))
        } catch (e: Exception) {
            paymentIntentResult.tryEmit(PaymentIntentResult.FetchFailure)
        }
    }

    fun observePaymentIntent() = paymentIntentResult
}
