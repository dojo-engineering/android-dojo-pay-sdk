package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.paymentmethods.PaymentMethodsRepository
import tech.dojo.pay.uisdk.entities.DojoPaymentType

internal class FetchPaymentMethodsUseCase(
    private val repo: PaymentMethodsRepository,
) {

    fun fetchPaymentMethodsWithPaymentType(
        paymentType: DojoPaymentType,
        customerId: String,
        customerSecret: String,
    ) {
        if (paymentType != DojoPaymentType.CARD_ON_FILE) {
            fetchPaymentMethods(customerId, customerSecret)
        }
    }

    private fun fetchPaymentMethods(customerId: String, customerSecret: String) {
        repo.fetchPaymentMethod(
            customerId,
            customerSecret,
        )
    }
}
