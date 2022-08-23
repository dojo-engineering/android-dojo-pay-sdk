package tech.dojo.pay.sdk.payemntintent.data

import tech.dojo.pay.sdk.DojoPaymentIntentResult

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