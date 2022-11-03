package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.paymentintent.PaymentIntentRepository

internal class ObservePaymentIntent(
    private val repo: PaymentIntentRepository
) {
    fun observePaymentIntent() = repo.observePaymentIntent()
}
