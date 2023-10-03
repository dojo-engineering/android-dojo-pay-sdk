package tech.dojo.pay.uisdk.data.entities

import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity

internal sealed class PaymentIntentResult {
    data class Success(val result: PaymentIntentDomainEntity) : PaymentIntentResult()
    object FetchFailure : PaymentIntentResult()
    object None : PaymentIntentResult()
    object Fetching : PaymentIntentResult()
}
