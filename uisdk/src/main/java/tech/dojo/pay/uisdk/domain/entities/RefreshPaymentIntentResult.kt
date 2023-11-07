package tech.dojo.pay.uisdk.domain.entities

internal sealed class RefreshPaymentIntentResult {
    data class Success(val token: String) : RefreshPaymentIntentResult()
    object RefreshFailure : RefreshPaymentIntentResult()
    object None : RefreshPaymentIntentResult()
    object Fetching : RefreshPaymentIntentResult()
}
