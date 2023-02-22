package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.PaymentStateRepository

class ObservePaymentStatus(
    private val paymentStateRepository: PaymentStateRepository
) {
    fun observePaymentStates() = paymentStateRepository.observePaymentIntent()

    fun observeGpayPaymentStates() = paymentStateRepository.observeGpayPaymentIntent()

}
