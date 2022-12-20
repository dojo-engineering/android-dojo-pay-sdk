package tech.dojo.pay.uisdk.domain.mapper

import org.junit.Assert
import org.junit.Test
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.uisdk.data.entities.CardDetails
import tech.dojo.pay.uisdk.data.entities.PaymentMethodsRaw
import tech.dojo.pay.uisdk.data.entities.SavedPaymentMethods
import tech.dojo.pay.uisdk.data.entities.SupportedPaymentMethods
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntityItem

internal class SupportedPaymentMethodsDomainMapperTest {

    @Test
    fun `should map to the correct PaymentMethodsDomainEntity`() {
        val raw = PaymentMethodsRaw(
            customerId = "id",
            merchantId = "mid",
            savedPaymentMethods = listOf(
                SavedPaymentMethods(
                    id = "PaymentId",
                    cardDetails = CardDetails(
                        pan = "pan",
                        expiryDate = "expiryDate",
                        scheme = CardsSchemes.MASTERCARD
                    )
                )
            ),
            supportedPaymentMethods = SupportedPaymentMethods(
                cardSchemes = listOf(CardsSchemes.MASTERCARD)
            )
        )
        val expected = PaymentMethodsDomainEntity(
            listOf(
                PaymentMethodsDomainEntityItem(
                    id = "PaymentId",
                    pan = "pan",
                    expiryDate = "expiryDate",
                    scheme = CardsSchemes.MASTERCARD
                )
            )
        )

        val actual = SupportedPaymentMethodsDomainMapper().apply(raw)

        Assert.assertEquals(expected, actual)
    }
}
