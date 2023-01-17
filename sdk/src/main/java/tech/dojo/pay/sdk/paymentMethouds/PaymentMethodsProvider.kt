package tech.dojo.pay.sdk.paymentMethouds

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoDeletePaymentMethodsResult
import tech.dojo.pay.sdk.DojoFetchPaymentMethodsResult
import tech.dojo.pay.sdk.paymentMethouds.data.PaymentMethodsApiBuilder
import tech.dojo.pay.sdk.paymentMethouds.data.PaymentMethodsRepository

internal class PaymentMethodsProvider(
    private val paymentMethodsRepository: PaymentMethodsRepository = PaymentMethodsRepository(
        api = PaymentMethodsApiBuilder().create()
    )
) {
    fun fetchPaymentMethods(
        customerId: String,
        customerSecret: String,
        onFetchPaymentMethodsSuccess: (paymentMethodsJson: String) -> Unit,
        onFetchPaymentMethodsFailed: () -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                when (
                    val result =
                        paymentMethodsRepository.fetchPaymentMethods(customerId, customerSecret)
                ) {
                    is DojoFetchPaymentMethodsResult.Success -> onFetchPaymentMethodsSuccess(result.paymentMethodsJson)
                    is DojoFetchPaymentMethodsResult.Failed -> onFetchPaymentMethodsFailed()
                }
            } catch (throwable: Throwable) {
                onFetchPaymentMethodsFailed()
            }
        }
    }

    fun deletePaymentMethod(
        customerId: String,
        customerSecret: String,
        paymentMethodId: String,
        onDeletePaymentMethodsSuccess: () -> Unit,
        onDeletePaymentMethodsFailed: () -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                when (
                    paymentMethodsRepository.deletePaymentMethods(
                        customerId,
                        customerSecret,
                        paymentMethodId
                    )
                ) {
                    is DojoDeletePaymentMethodsResult.Success -> onDeletePaymentMethodsSuccess()
                    is DojoDeletePaymentMethodsResult.Failed -> onDeletePaymentMethodsFailed()
                }
            } catch (throwable: Throwable) {
                onDeletePaymentMethodsFailed()
            }
        }
    }
}
