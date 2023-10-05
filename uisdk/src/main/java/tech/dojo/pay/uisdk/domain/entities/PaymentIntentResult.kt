package tech.dojo.pay.uisdk.domain.entities

internal sealed class PaymentIntentResult {
    data class Success(val result: PaymentIntentDomainEntity) : PaymentIntentResult()
    object FetchFailure : PaymentIntentResult()
    object None : PaymentIntentResult()
    object Fetching : PaymentIntentResult()
}
