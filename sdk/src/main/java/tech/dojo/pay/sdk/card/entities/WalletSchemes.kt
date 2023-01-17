package tech.dojo.pay.sdk.card.entities

enum class WalletSchemes(val walletSchemes: String) {
    GOOGLE_PAY("GOOGLE_PAY"),
    APPLE_PAY("APPLE_PAY"),
    NOT_SUPPORTED("");

    companion object {
        fun fromWalletSchemes(countryCode: String): WalletSchemes =
            values()
                .find { it.walletSchemes == countryCode } ?: NOT_SUPPORTED
    }
}
