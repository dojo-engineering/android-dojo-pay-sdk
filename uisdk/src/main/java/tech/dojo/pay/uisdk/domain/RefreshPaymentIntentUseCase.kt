package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.paymentintent.PaymentIntentRepository

internal class RefreshPaymentIntentUseCase(
    private val repo: PaymentIntentRepository,
) {
    fun refreshPaymentIntent(paymentId: String) {
//        repo.refreshPaymentIntent(paymentId)
    }
}
