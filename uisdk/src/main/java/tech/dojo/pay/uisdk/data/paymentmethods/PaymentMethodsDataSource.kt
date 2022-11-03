package tech.dojo.pay.uisdk.data.paymentmethods

import tech.dojo.pay.sdk.DojoSdk

class PaymentMethodsDataSource {

    fun fetchPaymentMethods(
        customerId: String,
        customerSecret: String,
        onFetchPaymentMethodsSuccess: (paymentMethodsJson: String) -> Unit,
        onFetchPaymentMethodsFailed: () -> Unit
    ) {
        DojoSdk.fetchPaymentMethods(
            customerId,
            customerSecret,
            onFetchPaymentMethodsSuccess,
            onFetchPaymentMethodsFailed
        )
    }

    fun deletePaymentMethods(
        customerId: String,
        customerSecret: String,
        paymentMethodId: String,
        onDeletePaymentMethodsSuccess: () -> Unit,
        onDeletePaymentMethodsFailed: () -> Unit
    ) {
        DojoSdk.deletePaymentMethods(
            customerId,
            customerSecret,
            paymentMethodId,
            onDeletePaymentMethodsSuccess,
            onDeletePaymentMethodsFailed
        )
    }
}