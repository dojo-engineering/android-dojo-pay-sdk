package tech.dojo.pay.uisdk.data.paymentintent

import tech.dojo.pay.sdk.DojoSdk

class PaymentIntentDataSource {
    fun fetchPaymentIntent(
        paymentId: String,
        onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit,
        onPaymentIntentFailed: () -> Unit
    ) {
        DojoSdk.fetchPaymentIntent(paymentId, onPaymentIntentSuccess, onPaymentIntentFailed)
    }
    fun refreshPaymentIntent(
        paymentId: String,
        onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit,
        onPaymentIntentFailed: () -> Unit
    ) {
        DojoSdk.refreshPaymentIntent(paymentId, onPaymentIntentSuccess, onPaymentIntentFailed)
    }
}
