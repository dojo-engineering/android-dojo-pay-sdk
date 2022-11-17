package tech.dojo.pay.sdk

sealed class DojoFetchPaymentMethodsResult {
    data class Success(val paymentMethodsJson: String) : DojoFetchPaymentMethodsResult()
    object Failed : DojoFetchPaymentMethodsResult()
}

sealed class DojoDeletePaymentMethodsResult {
    object Success : DojoDeletePaymentMethodsResult()
    object Failed : DojoDeletePaymentMethodsResult()
}
