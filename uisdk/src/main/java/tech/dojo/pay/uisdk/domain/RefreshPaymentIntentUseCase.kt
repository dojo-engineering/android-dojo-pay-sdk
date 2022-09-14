package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.PaymentIntentRepository

class RefreshPaymentIntentUseCase(
    private val repo: PaymentIntentRepository
) {
    fun refreshPaymentIntent(paymentId: String) {
        repo.refreshPaymentIntent(paymentId)
    }
}
