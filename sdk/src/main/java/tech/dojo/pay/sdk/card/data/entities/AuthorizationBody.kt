package tech.dojo.pay.sdk.card.data.entities

import com.google.gson.annotations.SerializedName

data class AuthorizationBody(
    @SerializedName("PaRes")
    val jwt: String
)