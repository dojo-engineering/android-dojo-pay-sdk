package tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.mapper

import org.junit.Assert
import org.junit.Test
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.domain.entities.FetchPaymentMethodsResult
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntityItem
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntity
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem


internal class PaymentMethodItemViewEntityMapperTest {

    @Test
    fun `should map to wallet item in case of enabled wallet`() {
        // arrange
        val isDarkModeEnabled = false
        val result = FetchPaymentMethodsResult.Failure
        val expected =
            PaymentMethodItemViewEntity(items = listOf(PaymentMethodItemViewEntityItem.WalletItemItem))
        // act
        val actual = PaymentMethodItemViewEntityMapper(isDarkModeEnabled).apply(result, true)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should map to the card item in case of Success  from FetchPaymentMethodsResult`() {
        // arrange
        val isDarkModeEnabled = false

        val paymentMethodsDomainEntity = PaymentMethodsDomainEntity(
            items = listOf(
                PaymentMethodsDomainEntityItem(
                    id = "id",
                    pan = "****9560",
                    expiryDate = "expiryDate",
                    scheme = CardsSchemes.VISA
                )
            )
        )
        val result = FetchPaymentMethodsResult.Success(result = paymentMethodsDomainEntity)
        val expected = PaymentMethodItemViewEntity(
            items = listOf(
                PaymentMethodItemViewEntityItem.WalletItemItem,
                PaymentMethodItemViewEntityItem.CardItemItem(
                    id = "id",
                    icon = R.drawable.ic_visa,
                    scheme = "VISA",
                    pan = "****9560"
                )
            )
        )
        // act
        val actual = PaymentMethodItemViewEntityMapper(isDarkModeEnabled).apply(result, true)
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should map to visa icon`() {
        // arrange
        val isDarkModeEnabled = false
        val visaItem = PaymentMethodsDomainEntity(
            items = listOf(
                PaymentMethodsDomainEntityItem(
                    id = "id",
                    pan = "****9560",
                    expiryDate = "expiryDate",
                    scheme = CardsSchemes.VISA
                )
            )
        )
        val result = FetchPaymentMethodsResult.Success(result = visaItem)
        val expected = PaymentMethodItemViewEntity(
            items = listOf(
                PaymentMethodItemViewEntityItem.CardItemItem(
                    id = "id",
                    icon = R.drawable.ic_visa,
                    scheme = "VISA",
                    pan = "****9560"
                )
            )
        )
        // act
        val actual = PaymentMethodItemViewEntityMapper(isDarkModeEnabled).apply(result, false)
        // assert
        Assert.assertEquals(
            (expected.items[0] as PaymentMethodItemViewEntityItem.CardItemItem).icon,
            (actual.items[0] as PaymentMethodItemViewEntityItem.CardItemItem).icon
        )
    }

    @Test
    fun `should map to visa icon with darck mode `() {
        // arrange
        val isDarkModeEnabled = true
        val visaItem = PaymentMethodsDomainEntity(
            items = listOf(
                PaymentMethodsDomainEntityItem(
                    id = "id",
                    pan = "****9560",
                    expiryDate = "expiryDate",
                    scheme = CardsSchemes.VISA
                )
            )
        )
        val result = FetchPaymentMethodsResult.Success(result = visaItem)
        val expected = PaymentMethodItemViewEntity(
            items = listOf(
                PaymentMethodItemViewEntityItem.CardItemItem(
                    id = "id",
                    icon = R.drawable.ic_visa_dark,
                    scheme = "VISA",
                    pan = "****9560"
                )
            )
        )
        // act
        val actual = PaymentMethodItemViewEntityMapper(isDarkModeEnabled).apply(result, false)
        // assert
        Assert.assertEquals(
            (expected.items[0] as PaymentMethodItemViewEntityItem.CardItemItem).icon,
            (actual.items[0] as PaymentMethodItemViewEntityItem.CardItemItem).icon
        )
    }

    @Test
    fun `should map to mastercard icon`() {
        // arrange
        val isDarkModeEnabled = false
        val visaItem = PaymentMethodsDomainEntity(
            items = listOf(
                PaymentMethodsDomainEntityItem(
                    id = "id",
                    pan = "****9560",
                    expiryDate = "expiryDate",
                    scheme = CardsSchemes.MASTERCARD
                )
            )
        )
        val result = FetchPaymentMethodsResult.Success(result = visaItem)
        val expected = PaymentMethodItemViewEntity(
            items = listOf(
                PaymentMethodItemViewEntityItem.CardItemItem(
                    id = "id",
                    icon = R.drawable.ic_mastercard,
                    scheme = "VISA",
                    pan = "****9560"
                )
            )
        )
        // act
        val actual = PaymentMethodItemViewEntityMapper(isDarkModeEnabled).apply(result, false)
        // assert
        Assert.assertEquals(
            (expected.items[0] as PaymentMethodItemViewEntityItem.CardItemItem).icon,
            (actual.items[0] as PaymentMethodItemViewEntityItem.CardItemItem).icon
        )
    }

    @Test
    fun `should map to maestro icon`() {
        // arrange
        val isDarkModeEnabled = false

        val visaItem = PaymentMethodsDomainEntity(
            items = listOf(
                PaymentMethodsDomainEntityItem(
                    id = "id",
                    pan = "****9560",
                    expiryDate = "expiryDate",
                    scheme = CardsSchemes.MAESTRO
                )
            )
        )
        val result = FetchPaymentMethodsResult.Success(result = visaItem)
        val expected = PaymentMethodItemViewEntity(
            items = listOf(
                PaymentMethodItemViewEntityItem.CardItemItem(
                    id = "id",
                    icon = R.drawable.ic_maestro,
                    scheme = "VISA",
                    pan = "****9560"
                )
            )
        )
        // act
        val actual = PaymentMethodItemViewEntityMapper(isDarkModeEnabled).apply(result, false)
        // assert
        Assert.assertEquals(
            (expected.items[0] as PaymentMethodItemViewEntityItem.CardItemItem).icon,
            (actual.items[0] as PaymentMethodItemViewEntityItem.CardItemItem).icon
        )
    }

    @Test
    fun `should map to amex icon`() {
        // arrange
        val isDarkModeEnabled = false

        val visaItem = PaymentMethodsDomainEntity(
            items = listOf(
                PaymentMethodsDomainEntityItem(
                    id = "id",
                    pan = "****9560",
                    expiryDate = "expiryDate",
                    scheme = CardsSchemes.AMEX
                )
            )
        )
        val result = FetchPaymentMethodsResult.Success(result = visaItem)
        val expected = PaymentMethodItemViewEntity(
            items = listOf(
                PaymentMethodItemViewEntityItem.CardItemItem(
                    id = "id",
                    icon = R.drawable.ic_amex,
                    scheme = "VISA",
                    pan = "****9560"
                )
            )
        )
        // act
        val actual = PaymentMethodItemViewEntityMapper(isDarkModeEnabled).apply(result, false)
        // assert
        Assert.assertEquals(
            (expected.items[0] as PaymentMethodItemViewEntityItem.CardItemItem).icon,
            (actual.items[0] as PaymentMethodItemViewEntityItem.CardItemItem).icon
        )
    }

    @Test
    fun `should map to amex icon with dark mode  `() {
        // arrange
        val isDarkModeEnabled = true

        val visaItem = PaymentMethodsDomainEntity(
            items = listOf(
                PaymentMethodsDomainEntityItem(
                    id = "id",
                    pan = "****9560",
                    expiryDate = "expiryDate",
                    scheme = CardsSchemes.AMEX
                )
            )
        )
        val result = FetchPaymentMethodsResult.Success(result = visaItem)
        val expected = PaymentMethodItemViewEntity(
            items = listOf(
                PaymentMethodItemViewEntityItem.CardItemItem(
                    id = "id",
                    icon = R.drawable.ic_amex_dark,
                    scheme = "VISA",
                    pan = "****9560"
                )
            )
        )
        // act
        val actual = PaymentMethodItemViewEntityMapper(isDarkModeEnabled).apply(result, false)
        // assert
        Assert.assertEquals(
            (expected.items[0] as PaymentMethodItemViewEntityItem.CardItemItem).icon,
            (actual.items[0] as PaymentMethodItemViewEntityItem.CardItemItem).icon
        )
    }
}