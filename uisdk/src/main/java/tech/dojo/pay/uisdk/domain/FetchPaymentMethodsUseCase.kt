package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.paymentmethods.PaymentMethodsRepository

internal class FetchPaymentMethodsUseCase(
    private val repo: PaymentMethodsRepository
) {
    fun fetchPaymentMethods(customerId: String, customerSecret: String) {
        repo.fetchPaymentMethod(
            customerId,
            customerSecret
        )
    }
}
