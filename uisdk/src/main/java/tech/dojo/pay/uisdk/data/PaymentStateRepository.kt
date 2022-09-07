package tech.dojo.pay.uisdk.data

import kotlinx.coroutines.flow.MutableStateFlow

class PaymentStateRepository {

    private var isPaymentInProgress: MutableStateFlow<Boolean> = MutableStateFlow(true)

    fun updatePayment(isActive: Boolean) {
        isPaymentInProgress.tryEmit(isActive)
    }

    fun observePaymentIntent() = isPaymentInProgress
}
