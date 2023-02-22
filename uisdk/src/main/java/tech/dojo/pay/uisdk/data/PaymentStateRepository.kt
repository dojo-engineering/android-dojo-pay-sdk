package tech.dojo.pay.uisdk.data

import kotlinx.coroutines.flow.MutableStateFlow

class PaymentStateRepository {

    private var isPaymentInProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var isGpayPaymentInProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun updatePayment(isActive: Boolean) {
        isPaymentInProgress.tryEmit(isActive)
    }

    fun updateGpayPayment(isActive: Boolean) {
        isGpayPaymentInProgress.tryEmit(isActive)
    }
    fun observePaymentIntent() = isPaymentInProgress
    fun observeGpayPaymentIntent() = isGpayPaymentInProgress
}
