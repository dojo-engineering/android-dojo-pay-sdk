package tech.dojo.pay.sdk

sealed class DojoPaymentIntentResult {
    data class Success(val paymentIntentJson: String) : DojoPaymentIntentResult()
    object Failed : DojoPaymentIntentResult()
}
