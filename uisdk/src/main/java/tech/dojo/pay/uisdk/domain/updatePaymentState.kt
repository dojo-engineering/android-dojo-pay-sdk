package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.PaymentStateRepository

class UpdatePaymentStateUseCase(
    private val paymentStateRepository: PaymentStateRepository = PaymentStateRepository()
) {
    fun updatePaymentSate(isActive: Boolean) {
        paymentStateRepository.updatePayment(isActive)
    }
}