package tech.dojo.pay.sdk.card.data.entities

import com.google.gson.annotations.SerializedName

internal data class AuthorizationBody(
    @SerializedName("paRes")
    val jwt: String,
    val transactionId: String,
    val cardinalValidateResponse: ValidateCardinalResponse?
)

internal data class ValidateCardinalResponse(
    val isValidated: Boolean?,
    val errorNumber: Int?,
    val errorDescription: String,
    val actionCode: String
)
