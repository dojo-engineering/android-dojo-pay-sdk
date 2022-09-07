package tech.dojo.pay.uisdk.data.entities

import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity

sealed class PaymentIntentResult {
    data class Success(val result: PaymentIntentDomainEntity) : PaymentIntentResult()
    object Failure : PaymentIntentResult()
}
