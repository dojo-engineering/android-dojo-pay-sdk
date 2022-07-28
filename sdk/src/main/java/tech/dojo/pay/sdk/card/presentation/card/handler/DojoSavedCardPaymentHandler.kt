package tech.dojo.pay.sdk.card.presentation.card.handler

import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad

interface DojoSavedCardPaymentHandler {
    fun executeSavedCardPayment(
        token: String,
        payload: DojoCardPaymentPayLoad.SavedCardPaymentPayLoad
    )
}
