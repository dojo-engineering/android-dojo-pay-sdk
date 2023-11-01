package tech.dojo.pay.sdk.card.data.entities

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
class PaymentResponse(
    val statusCode: Int,
    val md: String? = null,
    val stepUpUrl: String? = null,
    @SerializedName("paReq")
    val jwt: String? = null,
)
