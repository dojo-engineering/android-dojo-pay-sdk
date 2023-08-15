package tech.dojo.pay.uisdk.domain

import kotlinx.coroutines.flow.StateFlow
import tech.dojo.pay.uisdk.data.paymentintent.RefreshPaymentIntentRepository
import tech.dojo.pay.uisdk.domain.entities.RefreshPaymentIntentResult

internal class GetRefreshedPaymentTokenFlow(
    private val repo: RefreshPaymentIntentRepository,
) {
    fun getUpdatedPaymentTokenFlow(): StateFlow<RefreshPaymentIntentResult?> {
        return repo.getRefreshedPaymentTokenFlow()
    }
}
