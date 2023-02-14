package tech.dojo.pay.sdk.card.data.entities

import com.google.gson.annotations.SerializedName

class PaymentResponse(
    val statusCode: Int,
    val md: String? = null,
    val stepUpUrl: String? = null,
    @SerializedName("paReq")
    val jwt: String? = null
)
