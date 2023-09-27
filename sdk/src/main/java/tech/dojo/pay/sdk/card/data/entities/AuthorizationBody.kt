package tech.dojo.pay.sdk.card.data.entities

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
internal data class AuthorizationBody(
    @SerializedName("paRes")
    val jwt: String,
    val transactionId: String,
    val cardinalValidateResponse: ValidateCardinalResponse?,
)

@Keep
internal data class ValidateCardinalResponse(
    val isValidated: Boolean?,
    val errorNumber: Int?,
    val errorDescription: String,
    val actionCode: String,
)
