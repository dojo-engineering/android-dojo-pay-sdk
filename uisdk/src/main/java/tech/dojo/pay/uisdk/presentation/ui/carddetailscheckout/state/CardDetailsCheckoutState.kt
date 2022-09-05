package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state

import androidx.annotation.StringRes
import tech.dojo.pay.uisdk.R

data class CardDetailsCheckoutState(
    val totalAmount: String,
    val amountCurrency: String,
    val cardHolderInputField: InputFieldState,
    val cardDetailsInPutField: CardDetailsInputFieldState,
    val isLoading: Boolean
)

data class InputFieldState(
    @StringRes val labelStringId: Int,
    val value: String,
)

data class CardDetailsInputFieldState(
    @StringRes val inputFieldLabel: Int,
    @StringRes val cardNumberLabel: Int,
    val cardNumberValue: String,
    @StringRes val cvvLabel: Int,
    val cvvValue: String,
    @StringRes val expireDateLabel: Int,
    val expireDateValueValue: String,
)