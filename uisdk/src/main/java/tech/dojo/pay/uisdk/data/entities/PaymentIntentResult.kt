package tech.dojo.pay.uisdk.data.entities

sealed class PaymentIntentResult {
    data class Success(val result: PaymentIntentPayload) : PaymentIntentResult()
    object Failure : PaymentIntentResult()
}
