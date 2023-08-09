package tech.dojo.pay.uisdk.domain

import tech.dojo.pay.uisdk.data.paymentintent.RefreshPaymentIntentRepository
import tech.dojo.pay.uisdk.entities.DojoPaymentType

internal class RefreshPaymentIntentUseCase(
    private val repo: RefreshPaymentIntentRepository,
    private val paymentType: DojoPaymentType,
) {
    fun refreshPaymentIntent(paymentId: String) {
        when (paymentType) {
            DojoPaymentType.PAYMENT_CARD -> repo.refreshPaymentIntent(paymentId)
            DojoPaymentType.SETUP_INTENT -> repo.refreshSetupIntent(paymentId)
            DojoPaymentType.VIRTUAL_TERMINAL -> repo.refreshPaymentIntent(paymentId)
        }
    }
}
