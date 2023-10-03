package tech.dojo.pay.sdk.paymentmethods.data

import tech.dojo.pay.sdk.DojoDeletePaymentMethodsResult
import tech.dojo.pay.sdk.DojoFetchPaymentMethodsResult

internal class PaymentMethodsRepository(private val api: PaymentMethodsApi) {

    suspend fun fetchPaymentMethods(
        customerId: String,
        customerSecret: String,
    ): DojoFetchPaymentMethodsResult {
        val response = api.fetchSavedPayment(
            customerId = customerId,
            authorization = "Basic $customerSecret",
        )
        return if (response.isSuccessful) {
            DojoFetchPaymentMethodsResult.Success(response.body().toString())
        } else {
            DojoFetchPaymentMethodsResult.Failed
        }
    }

    suspend fun deletePaymentMethods(
        customerId: String,
        customerSecret: String,
        paymentMethodId: String,
    ): DojoDeletePaymentMethodsResult {
        val response = api.deleteSavedPayment(
            customerId = customerId,
            paymentMethodId = paymentMethodId,
            authorization = "Basic $customerSecret",
        )
        return if (response.isSuccessful) {
            DojoDeletePaymentMethodsResult.Success
        } else {
            DojoDeletePaymentMethodsResult.Failed
        }
    }
}
