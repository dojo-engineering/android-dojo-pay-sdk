package tech.dojo.pay.sdk.payemntintent.data

import tech.dojo.pay.sdk.DojoPaymentIntentResult

internal class PaymentIntentRepository(
    private val api: PaymentIntentApi,
) {
    suspend fun getPaymentIntent(paymentId: String): DojoPaymentIntentResult {
        val response = api.fetchPaymentIntent(paymentId)
        return if (response.isSuccessful) {
            DojoPaymentIntentResult.Success(response.body().toString())
        } else {
            DojoPaymentIntentResult.Failed
        }
    }

    suspend fun getSetUpIntent(paymentId: String): DojoPaymentIntentResult {
        val response = api.fetchSetUpIntent(paymentId)
        return if (response.isSuccessful) {
            DojoPaymentIntentResult.Success(response.body().toString())
        } else {
            DojoPaymentIntentResult.Failed
        }
    }

    suspend fun refreshPaymentIntent(paymentId: String): DojoPaymentIntentResult {
        val response = api.refreshPaymentIntent(paymentId)
        return if (response.isSuccessful) {
            DojoPaymentIntentResult.Success(response.body().toString())
        } else {
            DojoPaymentIntentResult.Failed
        }
    }

    suspend fun refreshSetUpIntent(paymentId: String): DojoPaymentIntentResult {
        val response = api.refreshSetupIntent(paymentId)
        return if (response.isSuccessful) {
            DojoPaymentIntentResult.Success(response.body().toString())
        } else {
            DojoPaymentIntentResult.Failed
        }
    }
}
