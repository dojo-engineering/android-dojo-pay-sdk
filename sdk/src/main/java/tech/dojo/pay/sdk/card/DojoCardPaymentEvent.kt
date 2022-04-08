package tech.dojo.pay.sdk.card

import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult

internal sealed class DojoCardPaymentEvent {

    class ReturnResult(val result: DojoCardPaymentResult) : DojoCardPaymentEvent()

    object Navigate3DS : DojoCardPaymentEvent()

    data class Show3dsScreen(val pageContent: String) : DojoCardPaymentEvent()
}