package tech.dojo.pay.sdk.card.entities

enum class CardsSchemes(val cardsSchemes: String) {
    VISA("VISA"),
    MASTERCARD("MASTERCARD"),
    MAESTRO("MAESTRO"),
    AMEX("AMEX"),
    NOT_SUPPORTED("");

    companion object {
        fun fromCardsSchemes(cardsSchemes: String): CardsSchemes =
            values()
                .find { it.cardsSchemes == cardsSchemes } ?: NOT_SUPPORTED
    }
}
