package tech.dojo.pay.sdk.card.entities

internal sealed class PaymentResult {

    class Completed(val value: DojoCardPaymentResult) : PaymentResult()

    class ThreeDSRequired(val params: ThreeDSParams) : PaymentResult()
}