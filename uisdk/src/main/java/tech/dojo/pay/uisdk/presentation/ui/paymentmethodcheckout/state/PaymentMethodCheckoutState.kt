package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state

import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.uisdk.presentation.components.AmountBreakDownItem
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.InputFieldState
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem

internal data class PaymentMethodCheckoutState(
    val gPayConfig: DojoGPayConfig?,
    val paymentMethodItem: PaymentMethodItemViewEntityItem?,
    val amountBreakDownList: List<AmountBreakDownItem>,
    val payWithCarButtonState: PayWithCarButtonState,
    val totalAmount: String,
    val cvvFieldState: InputFieldState,
    val isGooglePayButtonVisible: Boolean,
    val isBottomSheetVisible: Boolean,
    val isBottomSheetLoading: Boolean,
    val payAmountButtonState: PayAmountButtonVState?
)

internal data class PayWithCarButtonState(
    val isVisible: Boolean,
    val isPrimary: Boolean,
    val navigateToCardCheckout: Boolean
)

internal data class PayAmountButtonVState(val isEnabled: Boolean, val isLoading: Boolean = false)
