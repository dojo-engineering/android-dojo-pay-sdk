package tech.dojo.pay.uisdk.data

import tech.dojo.pay.sdk.DojoSdk

class PaymentIntentDataSource {
    fun fetchPaymentIntent(
        paymentId: String,
        onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit,
        onPaymentIntentFailed: () -> Unit
    ) {
        DojoSdk.fetchPaymentIntent(paymentId, onPaymentIntentSuccess, onPaymentIntentFailed)
    }
}
