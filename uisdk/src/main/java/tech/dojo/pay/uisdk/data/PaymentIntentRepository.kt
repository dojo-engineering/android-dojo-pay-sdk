package tech.dojo.pay.uisdk.data

import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import tech.dojo.pay.uisdk.data.entities.PaymentIntentPayload
import tech.dojo.pay.uisdk.data.entities.PaymentIntentResult

class PaymentIntentRepository(
    private val dataSource: PaymentIntentDataSource = PaymentIntentDataSource(),
    private val gson: Gson = Gson()
) {
    private val paymentIntentResult: MutableStateFlow<PaymentIntentResult?> = MutableStateFlow(null)

    fun fetchPaymentIntent(paymentId: String) {
        dataSource.fetchPaymentIntent(
            paymentId,
            { paymentIntentResult.tryEmit(PaymentIntentResult.Success(mapToPaymentIntentPayLoad(it))) },
            { paymentIntentResult.tryEmit(PaymentIntentResult.Failure) }
        )
    }

    private fun mapToPaymentIntentPayLoad(paymentIntentPayloadJson: String) =
        gson.fromJson(paymentIntentPayloadJson, PaymentIntentPayload::class.java)

    fun observePaymentIntent() = paymentIntentResult
}
