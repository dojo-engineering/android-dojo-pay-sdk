package tech.dojo.pay.sdk.card.entities

enum class CardsSchemes(val cardsSchemes: String) {
    VISA("VISA"),
    MASTER_CARD("MASTERCARD"),
    MAESTRO("MAESTRO"),
    AMEX("AMEX"),
    NOT_SUPPORTED("");

    companion object {
        fun fromCountryCode(countryCode: String): CardsSchemes =
            values()
                .find { it.cardsSchemes == countryCode } ?: NOT_SUPPORTED
    }
}


