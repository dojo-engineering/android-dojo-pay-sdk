package tech.dojo.pay.uisdk.domain.entities

internal sealed class RefreshPaymentIntentResult {
    data class Success(val result: String) : RefreshPaymentIntentResult()
    object RefreshFailure : RefreshPaymentIntentResult()
}
