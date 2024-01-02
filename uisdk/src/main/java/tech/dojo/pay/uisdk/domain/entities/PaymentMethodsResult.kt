package tech.dojo.pay.uisdk.domain.entities

internal sealed class FetchPaymentMethodsResult {

    data class Success(val result: PaymentMethodsDomainEntity) : FetchPaymentMethodsResult()
    object None : FetchPaymentMethodsResult()
    object Fetching : FetchPaymentMethodsResult()
    object Failure : FetchPaymentMethodsResult()
}

internal sealed class DeletePaymentMethodsResult {
    object Success : DeletePaymentMethodsResult()
    object Failure : DeletePaymentMethodsResult()
}
