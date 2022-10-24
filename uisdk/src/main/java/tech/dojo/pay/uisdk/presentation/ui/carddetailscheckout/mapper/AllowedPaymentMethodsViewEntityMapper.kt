package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper

import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.uisdk.R

internal class AllowedPaymentMethodsViewEntityMapper {
    fun apply(supportedCardsSchemes: List<CardsSchemes>): List<Int> {
        val result = mutableListOf<Int>()
        supportedCardsSchemes
            .filter { it != CardsSchemes.NOT_SUPPORTED }
            .forEach {
                if (it == CardsSchemes.VISA) {
                    result.add(R.drawable.ic_visa)
                }
                if (it == CardsSchemes.MASTERCARD) {
                    result.add(R.drawable.ic_mastercard)
                }
                if (it == CardsSchemes.MAESTRO) {
                    result.add(R.drawable.ic_maestro)
                }
                if (it == CardsSchemes.AMEX) {
                    result.add(R.drawable.ic_amex)
                }
            }
        return result
    }
}
