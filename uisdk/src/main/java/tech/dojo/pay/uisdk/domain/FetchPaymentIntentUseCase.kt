package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.paymentintent.PaymentIntentRepository
import tech.dojo.pay.uisdk.entities.DojoPaymentType

internal class FetchPaymentIntentUseCase(
    private val repo: PaymentIntentRepository,
) {
    fun fetchPaymentIntentWithPaymentType(paymentType: DojoPaymentType, paymentId: String) {
        when (paymentType) {
            DojoPaymentType.PAYMENT_CARD -> fetchPaymentIntent(paymentId)
            DojoPaymentType.VIRTUAL_TERMINAL -> fetchPaymentIntent(paymentId)
            DojoPaymentType.SETUP_INTENT -> fetchSetUpIntent(paymentId)
        }
    }
    private fun fetchPaymentIntent(paymentId: String) {
        repo.fetchPaymentIntent(paymentId)
    }

    private fun fetchSetUpIntent(paymentId: String) {
        repo.fetchSetUpIntent(paymentId)
    }
}
