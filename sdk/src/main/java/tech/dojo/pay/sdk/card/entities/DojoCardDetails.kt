package tech.dojo.pay.sdk.card.entities

import java.io.Serializable

data class DojoCardDetails(
    val cardNumber: String,
    val cardName: String? = null,
    val expiryMonth: String? = null,
    val expiryYear: String? = null,
    val cv2: String? = null
) : Serializable