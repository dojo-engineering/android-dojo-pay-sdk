package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.mapper

import org.junit.Assert
import org.junit.Test
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.FetchPaymentMethodsResult
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntityItem
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.InputFieldState
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PayWithCardButtonState
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PaymentMethodCheckoutState

class PaymentMethodCheckoutViewEntityMapperTest {
    private val visaItem = PaymentMethodsDomainEntity(
        items = listOf(
            PaymentMethodsDomainEntityItem(
                id = "id",
                pan = "****9560",
                expiryDate = "expiryDate",
                scheme = CardsSchemes.VISA,
            ),
        ),
    )
    private val fetchPaymentMethodsResult = FetchPaymentMethodsResult.Success(result = visaItem)

    private val paymentIntentResult = PaymentIntentResult.Success(
        result = PaymentIntentDomainEntity(
            "id",
            "token",
            AmountDomainEntity(
                10L,
                "100",
                "GBP",
            ),
            supportedCardsSchemes = listOf(CardsSchemes.AMEX),
            collectionBillingAddressRequired = true,
        ),
    )

    @Test
    fun `when calling mapToViewState with nonEmpty paymentMethods and Available wallet should map to correct state`() {
        // arrange
        val expected = PaymentMethodCheckoutState(
            paymentMethodItem = PaymentMethodItemViewEntityItem.WalletItemItem,
            amountBreakDownList = emptyList(),
            payWithCardButtonState = PayWithCardButtonState(
                isVisible = false,
                isPrimary = false,
                navigateToCardCheckout = true,
            ),
            totalAmount = "£100",
            cvvFieldState = InputFieldState(
                value = "",
                errorMessages = null,
                isError = false,
            ),
            isGooglePayButtonVisible = true,
            isBottomSheetVisible = true,
            isBottomSheetLoading = false,
            payAmountButtonState = null,
        )
        // act
        val actual = PaymentMethodCheckoutViewEntityMapper().mapToViewState(
            paymentMethods = fetchPaymentMethodsResult,
            isWalletAvailable = true,
            paymentIntentResult = paymentIntentResult,
        )
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when calling mapToViewState with nonEmpty paymentMethods and unAvailable wallet should map to correct state`() {
        // arrange
        val expected = PaymentMethodCheckoutState(
            paymentMethodItem = null,
            amountBreakDownList = emptyList(),
            payWithCardButtonState = PayWithCardButtonState(
                isVisible = true,
                isPrimary = true,
                navigateToCardCheckout = false,
            ),
            totalAmount = "£100",
            cvvFieldState = InputFieldState(
                value = "",
                errorMessages = null,
                isError = false,
            ),
            isGooglePayButtonVisible = false,
            isBottomSheetVisible = true,
            isBottomSheetLoading = false,
            payAmountButtonState = null,
        )
        // act
        val actual = PaymentMethodCheckoutViewEntityMapper().mapToViewState(
            paymentMethods = fetchPaymentMethodsResult,
            isWalletAvailable = false,
            paymentIntentResult = paymentIntentResult,
        )
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when calling mapToViewState with Empty paymentMethods and Available wallet should map to correct state`() {
        // arrange
        val expected = PaymentMethodCheckoutState(
            paymentMethodItem = null,
            amountBreakDownList = emptyList(),
            payWithCardButtonState = PayWithCardButtonState(
                isVisible = true,
                isPrimary = false,
                navigateToCardCheckout = true,
            ),
            totalAmount = "£100",
            cvvFieldState = InputFieldState(
                value = "",
                errorMessages = null,
                isError = false,
            ),
            isGooglePayButtonVisible = true,
            isBottomSheetVisible = true,
            isBottomSheetLoading = false,
            payAmountButtonState = null,
        )
        // act
        val actual = PaymentMethodCheckoutViewEntityMapper().mapToViewState(
            paymentMethods = fetchPaymentMethodsResult.copy(
                result = PaymentMethodsDomainEntity(
                    emptyList(),
                ),
            ),
            isWalletAvailable = true,
            paymentIntentResult = paymentIntentResult,
        )
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when calling mapToViewState with Empty paymentMethods and unAvailable wallet should map to correct state`() {
        // arrange
        val expected = PaymentMethodCheckoutState(
            paymentMethodItem = null,
            amountBreakDownList = emptyList(),
            payWithCardButtonState = PayWithCardButtonState(
                isVisible = true,
                isPrimary = true,
                navigateToCardCheckout = true,
            ),
            totalAmount = "£100",
            cvvFieldState = InputFieldState(
                value = "",
                errorMessages = null,
                isError = false,
            ),
            isGooglePayButtonVisible = false,
            isBottomSheetVisible = true,
            isBottomSheetLoading = false,
            payAmountButtonState = null,
        )
        // act
        val actual = PaymentMethodCheckoutViewEntityMapper().mapToViewState(
            paymentMethods = fetchPaymentMethodsResult.copy(
                result = PaymentMethodsDomainEntity(
                    emptyList(),
                ),
            ),
            isWalletAvailable = false,
            paymentIntentResult = paymentIntentResult,
        )
        // assert
        Assert.assertEquals(expected, actual)
    }
}
