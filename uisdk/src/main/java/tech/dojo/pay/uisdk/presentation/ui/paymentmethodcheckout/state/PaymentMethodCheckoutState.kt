package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state

import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.uisdk.presentation.components.AmountBreakDownItem

data class PaymentMethodCheckoutState(
    val gPayConfig: DojoGPayConfig?,
    val isGooglePayVisible: Boolean,
    val isBottomSheetVisible: Boolean,
    val isLoading: Boolean,
    val isGpayItemVisible: Boolean,
    val amountBreakDownList: List<AmountBreakDownItem>,
    val totalAmount: String,
    val payWithCarButtonState: PayWithCarButtonState
)
data class PayWithCarButtonState(
    val isVisibleL: Boolean,
    val isPrimary: Boolean,

)
