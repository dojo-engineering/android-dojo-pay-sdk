package tech.dojo.pay.uisdk.data.mapper

import com.google.gson.Gson
import tech.dojo.pay.uisdk.data.entities.PaymentIntentPayload

internal class PaymentIntentPayLoadMapper(
    private val gson: Gson = Gson(),
) {
    fun mapToPaymentIntentPayLoad(paymentIntentPayloadJson: String): PaymentIntentPayload =
        gson.fromJson(paymentIntentPayloadJson, PaymentIntentPayload::class.java)
}
