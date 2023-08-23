package tech.dojo.pay.uisdk.data.paymentintent

import tech.dojo.pay.sdk.DojoSdk

class PaymentIntentDataSource {
    fun fetchPaymentIntent(
        paymentId: String,
        onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit,
        onPaymentIntentFailed: () -> Unit,
    ) {
        DojoSdk.fetchPaymentIntent(paymentId, onPaymentIntentSuccess, onPaymentIntentFailed)
    }

    fun fetchSetUpIntent(
        paymentId: String,
        onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit,
        onPaymentIntentFailed: () -> Unit,
    ) {
        DojoSdk.fetchSetUpIntent(paymentId, onPaymentIntentSuccess, onPaymentIntentFailed)
    }

    fun refreshPaymentIntent(
        paymentId: String,
        onPaymentIntentSuccess: (paymentIntentJson: String) -> Unit,
        onPaymentIntentFailed: () -> Unit,
    ) {
        DojoSdk.refreshPaymentIntent(paymentId, onPaymentIntentSuccess, onPaymentIntentFailed)
    }

    fun refreshSetupIntent(
        paymentId: String,
        onRefreshSetUpIntentSuccess: (paymentIntentJson: String) -> Unit,
        onRefreshSetUpIntentFailed: () -> Unit,
    ) {
        DojoSdk.refreshSetupIntent(paymentId, onRefreshSetUpIntentSuccess, onRefreshSetUpIntentFailed)
    }
}
