package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.mapper

import tech.dojo.pay.sdk.card.presentation.gpay.util.centsToString
import tech.dojo.pay.uisdk.domain.entities.FetchPaymentMethodsResult
import tech.dojo.pay.uisdk.domain.entities.ItemLinesDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentResult
import tech.dojo.pay.uisdk.presentation.components.AmountBreakDownItem
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.InputFieldState
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PayWithCardButtonState
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PaymentMethodCheckoutState
import java.util.Currency

internal class PaymentMethodCheckoutViewEntityMapper {
    private var currentState = PaymentMethodCheckoutState(
        isGooglePayButtonVisible = false,
        isBottomSheetVisible = true,
        isBottomSheetLoading = true,
        paymentMethodItem = null,
        amountBreakDownList = listOf(),
        totalAmount = "",
        cvvFieldState = InputFieldState(value = ""),
        payWithCardButtonState = PayWithCardButtonState(
            isVisible = false,
            isPrimary = false,
            navigateToCardCheckout = false,
        ),
        payAmountButtonState = null,
    )

    fun mapToViewState(
        paymentMethods: FetchPaymentMethodsResult?,
        isWalletAvailable: Boolean,
        paymentIntentResult: PaymentIntentResult.Success,
    ): PaymentMethodCheckoutState {
        val isSavedPaymentMethodsNotEmpty =
            if (paymentMethods is FetchPaymentMethodsResult.Success) paymentMethods.result.items.isNotEmpty() else false
        currentState = currentState.copy(
            amountBreakDownList = getAmountBreakDownList(paymentIntentResult.result.itemLines)
                ?: emptyList(),
            totalAmount = Currency.getInstance(paymentIntentResult.result.amount.currencyCode).symbol + paymentIntentResult.result.amount.valueString,
            isBottomSheetVisible = true,
            isBottomSheetLoading = false,
            payAmountButtonState = null,
        )
        when (isSavedPaymentMethodsNotEmpty) {
            true -> buildStateForAvailableSavedCard(isWalletAvailable)
            false -> buildStateForUnavailableSavedCard(isWalletAvailable)
        }
        return currentState
    }

    private fun buildStateForAvailableSavedCard(isGooglePayEnabled: Boolean) {
        if (isGooglePayEnabled) {
            currentState = currentState.copy(
                isGooglePayButtonVisible = true,
                paymentMethodItem = PaymentMethodItemViewEntityItem.WalletItemItem,
                payWithCardButtonState = PayWithCardButtonState(
                    isVisible = false,
                    isPrimary = false,
                    navigateToCardCheckout = true,
                ),
            )
        } else {
            currentState = currentState.copy(
                isGooglePayButtonVisible = false,
                payWithCardButtonState = PayWithCardButtonState(
                    isVisible = true,
                    isPrimary = true,
                    navigateToCardCheckout = false,
                ),
            )
        }
    }

    private fun buildStateForUnavailableSavedCard(isGooglePayEnabled: Boolean) {
        if (isGooglePayEnabled) {
            currentState = currentState.copy(
                isGooglePayButtonVisible = true,
                payWithCardButtonState = PayWithCardButtonState(
                    isVisible = true,
                    isPrimary = false,
                    navigateToCardCheckout = true,
                ),
            )
        } else {
            currentState = currentState.copy(
                isGooglePayButtonVisible = false,
                payWithCardButtonState = PayWithCardButtonState(
                    isVisible = true,
                    isPrimary = true,
                    navigateToCardCheckout = true,
                ),
            )
        }
    }

    private fun getAmountBreakDownList(itemLines: List<ItemLinesDomainEntity>?): List<AmountBreakDownItem> {
        return itemLines?.map {
            AmountBreakDownItem(
                caption = it.caption,
                amount = Currency.getInstance(it.amount.currencyCode).symbol +
                    it.amount.value.centsToString(),
            )
        } ?: emptyList()
    }
}
