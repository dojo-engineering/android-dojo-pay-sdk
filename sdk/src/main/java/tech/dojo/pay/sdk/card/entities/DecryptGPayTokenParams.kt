package tech.dojo.pay.sdk.card.entities

internal data class DecryptGPayTokenParams(
    val authMethod: AuthMethod,
    val pan: String,
    val expirationMonth: String,
    val expirationYear: String
)
internal enum class AuthMethod(val authMethod: String) {
    PAN_ONLY("PAN_ONLY"),
    CRYPTOGRAM_3DS("CRYPTOGRAM_3DS"),
    NOT_SUPPORTED("");

    companion object {
        fun fromAuthMethod(cardsSchemes: String): AuthMethod =
            values()
                .find { it.authMethod == cardsSchemes } ?: NOT_SUPPORTED
    }
}
