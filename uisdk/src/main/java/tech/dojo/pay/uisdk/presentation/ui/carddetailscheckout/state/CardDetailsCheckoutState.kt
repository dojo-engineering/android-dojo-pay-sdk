package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state

import androidx.annotation.StringRes

data class CardDetailsCheckoutState(
    val totalAmount: String,
    val amountCurrency: String,
    val cardHolderInputField: InputFieldState,
    val isLoading: Boolean
)

data class InputFieldState(
    @StringRes val labelStringId: Int,
    val value:String,
)