package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper

import org.junit.Assert
import org.junit.Test
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.uisdk.R



internal class AllowedPaymentMethodsViewEntityMapperTest{

    @Test
    fun `calling apply should map to correct icons`(){
        val supportedCards= listOf(
            CardsSchemes.AMEX,
            CardsSchemes.NOT_SUPPORTED,
            CardsSchemes.MAESTRO,
            CardsSchemes.MASTERCARD,
            CardsSchemes.VISA
        )

        val expected= listOf(
            R.drawable.ic_amex,
            R.drawable.ic_maestro,
            R.drawable.ic_mastercard,
            R.drawable.ic_visa
        )

        val actual= AllowedPaymentMethodsViewEntityMapper().apply(supportedCards)

        Assert.assertEquals(expected,actual)
    }

}