package tech.dojo.pay.sdk.card.entities

data class CardApiConfigurations(
    val cardApiEnvironment: CardApiEnvironment = CardApiEnvironment.SAND_BOX,
    val walletApiEnvironment: WalletApiEnvironment = WalletApiEnvironment.SAND_BOX
)

enum class CardApiEnvironment {
    PROD, SAND_BOX, STAGING
}

enum class WalletApiEnvironment {
    PROD, SAND_BOX
}
