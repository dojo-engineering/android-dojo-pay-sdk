package tech.dojo.pay.uisdk.domain.entities

import tech.dojo.pay.sdk.card.presentation.card.handler.DojoSavedCardPaymentHandler

internal data class MakeSavedCardPaymentParams(
    val savedCardPaymentHandler: DojoSavedCardPaymentHandler,
    val cv2: String,
    val paymentId: String,
    val paymentMethodId: String,
)
