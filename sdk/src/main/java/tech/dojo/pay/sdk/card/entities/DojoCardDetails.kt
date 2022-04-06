package tech.dojo.pay.sdk.card.entities

data class DojoCardDetails(
    val cardNumber: String,
    val cardName: String? = null,
    val expiryDate: String? = null,
    val cv2: String? = null
)