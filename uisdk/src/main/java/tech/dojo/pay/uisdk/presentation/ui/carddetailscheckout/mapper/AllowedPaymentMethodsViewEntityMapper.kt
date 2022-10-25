package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper

import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.uisdk.R

internal class AllowedPaymentMethodsViewEntityMapper {
    fun apply(supportedCardsSchemes: List<CardsSchemes>): List<Int> {
        return supportedCardsSchemes
            .filter { it != CardsSchemes.NOT_SUPPORTED }
            .mapNotNull {
                when (it) {
                    CardsSchemes.VISA -> R.drawable.ic_visa
                    CardsSchemes.MASTERCARD -> R.drawable.ic_mastercard
                    CardsSchemes.MAESTRO -> R.drawable.ic_maestro
                    CardsSchemes.AMEX -> R.drawable.ic_amex
                    else -> null
                }
            }
    }
}
