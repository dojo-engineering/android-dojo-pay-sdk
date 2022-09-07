package tech.dojo.pay.uisdk.data

import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import tech.dojo.pay.uisdk.data.entities.PaymentIntentPayload
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.mapper.PaymentIntentDomainEntityMapper

class PaymentIntentRepository(
    private val dataSource: PaymentIntentDataSource = PaymentIntentDataSource(),
    private val gson: Gson = Gson(),
    private val mapper: PaymentIntentDomainEntityMapper = PaymentIntentDomainEntityMapper()
) {
    private lateinit var paymentIntentResult: MutableStateFlow<PaymentIntentResult?>

    fun fetchPaymentIntent(paymentId: String) {
        paymentIntentResult = MutableStateFlow(null)
        dataSource.fetchPaymentIntent(
            paymentId,
            { handleSuccessPaymentIntent(it) },
            { paymentIntentResult.tryEmit(PaymentIntentResult.Failure) }
        )
    }

    private fun handleSuccessPaymentIntent(it: String) {
        try {
            val domainEntity = mapper.apply(mapToPaymentIntentPayLoad(it))
            paymentIntentResult.tryEmit(PaymentIntentResult.Success(domainEntity))
        } catch (e: Exception) {
            paymentIntentResult.tryEmit(PaymentIntentResult.Failure)
        }
    }

    private fun mapToPaymentIntentPayLoad(paymentIntentPayloadJson: String) =
        gson.fromJson(paymentIntentPayloadJson, PaymentIntentPayload::class.java)

    fun observePaymentIntent() = paymentIntentResult
}
