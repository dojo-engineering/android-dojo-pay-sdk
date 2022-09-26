package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state

data class CardDetailsCheckoutState(
    val totalAmount: String,
    val amountCurrency: String,
    val cardHolderInputField: InputFieldState,
    val cardDetailsInPutField: CardDetailsInputFieldState,
    val isLoading: Boolean,
    val isEnabled: Boolean
)

data class InputFieldState(val value: String)

data class CardDetailsInputFieldState(
    val cardNumberValue: String,
    val cvvValue: String,
    val expireDateValueValue: String,
)
