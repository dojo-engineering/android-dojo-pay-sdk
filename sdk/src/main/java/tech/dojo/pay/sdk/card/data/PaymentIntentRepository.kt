package tech.dojo.pay.sdk.card.data

import tech.dojo.pay.sdk.DojoPaymentIntentResult
import tech.dojo.pay.sdk.card.data.remote.paymentintent.PaymentIntentApi

class PaymentIntentRepository(
    private val api: PaymentIntentApi
) {
    suspend fun getPaymentIntent(paymentId: String): DojoPaymentIntentResult {
        val response = api.fetchPaymentIntent(paymentId)
        return if (response.isSuccessful) {
            DojoPaymentIntentResult.Success(response.body().toString())
        } else {
            DojoPaymentIntentResult.Failed
        }
    }
}