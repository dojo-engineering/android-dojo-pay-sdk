package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.paymentmethods.PaymentMethodsRepository
import tech.dojo.pay.uisdk.domain.entities.DeletePaymentMethodsResult

internal class DeletePaymentMethodsUseCase(
    private val customerId: String,
    private val customerSecrete: String,
    private val repo: PaymentMethodsRepository
) {
    fun deletePaymentMethods(
        paymentMethodId: String,
        onDeletePaymentMethodsSuccess: (DeletePaymentMethodsResult) -> Unit,
        onDeletePaymentMethodsFailed: (DeletePaymentMethodsResult) -> Unit
    ) {
        repo.deletePaymentMethods(
            customerId,
            customerSecrete,
            paymentMethodId,
            onDeletePaymentMethodsSuccess,
            onDeletePaymentMethodsFailed
        )
    }
}
