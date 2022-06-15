package tech.dojo.pay.sdk.card.entities

import tech.dojo.pay.sdk.DojoPaymentResult

internal sealed class PaymentResult {

    data class Completed(val value: DojoPaymentResult) : PaymentResult()

    data class ThreeDSRequired(val params: ThreeDSParams) : PaymentResult()
}
