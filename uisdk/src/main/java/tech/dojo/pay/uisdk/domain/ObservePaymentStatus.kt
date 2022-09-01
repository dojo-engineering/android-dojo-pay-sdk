package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.PaymentStateRepository

class ObservePaymentStatus(
    private val paymentStateRepository: PaymentStateRepository = PaymentStateRepository()

) {
    fun observePaymentStates() = paymentStateRepository.observePaymentIntent()
}