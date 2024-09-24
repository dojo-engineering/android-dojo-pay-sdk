package tech.dojo.pay.uisdk.domain.entities

enum class MarketId(val id: String) {

    Ireland("IE"),
    UnitedKingdom("GB"),
    Spain("ES"),
    Italy("IT"),
    ;
    companion object {

        private val DEFAULT = UnitedKingdom

        fun fromMarketId(id: String): MarketId {
            return entries.firstOrNull { it.id == id } ?: DEFAULT
        }
    }
}
