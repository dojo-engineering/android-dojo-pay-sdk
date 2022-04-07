package tech.dojo.pay.sdk.card.entities

import java.io.Serializable

data class DojoCardDetails(
    val cardNumber: String,
    val cardName: String? = null,
    val expiryDate: String? = null,
    val cv2: String? = null
) : Serializable