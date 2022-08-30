package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.PaymentIntentRepository

class ObservePaymentIntent(
    private val repo: PaymentIntentRepository
) {
    fun observePaymentIntent() = repo.observePaymentIntent()
}
