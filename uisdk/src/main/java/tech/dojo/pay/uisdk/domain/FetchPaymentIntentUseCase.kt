package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.paymentintent.PaymentIntentRepository

internal class FetchPaymentIntentUseCase(
    private val repo: PaymentIntentRepository
) {
    fun fetchPaymentIntent(paymentId: String) {
        repo.fetchPaymentIntent(paymentId)
    }
}
