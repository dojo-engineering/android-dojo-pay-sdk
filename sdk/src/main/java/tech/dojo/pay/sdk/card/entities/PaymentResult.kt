package tech.dojo.pay.sdk.card.entities

internal sealed class PaymentResult {

    data class Completed(val value: DojoCardPaymentResult) : PaymentResult()

    data class ThreeDSRequired(val params: ThreeDSParams) : PaymentResult()
}