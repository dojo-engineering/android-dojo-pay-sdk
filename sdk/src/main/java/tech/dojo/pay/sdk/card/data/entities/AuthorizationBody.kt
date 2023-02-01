package tech.dojo.pay.sdk.card.data.entities

import com.google.gson.annotations.SerializedName

data class AuthorizationBody(
    @SerializedName("paRes")
    val jwt:String,
    val transactionId:String
)
