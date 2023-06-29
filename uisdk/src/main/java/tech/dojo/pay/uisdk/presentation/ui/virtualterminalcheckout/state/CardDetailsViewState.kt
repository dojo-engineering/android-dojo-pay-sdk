package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state

internal data class CardDetailsViewState(
    val isVisible: Boolean,
    // this will be the start point to scroll to the input field when it got focus
    var itemPoissonOffset: Int,
    var emailInputField: InputFieldState,
    var cardHolderInputField: InputFieldState,
    var cardNumberInputField: InputFieldState,
    var cardExpireDateInputField: InputFieldState,
    var cvvInputFieldState: InputFieldState,
    val allowedPaymentMethodsIcons: List<Int>
) {
    fun updateIsVisible(newValue: Boolean) =
        copy(isVisible = newValue)
    fun updateItemPoissonOffset(newValue: Int) =
        copy(itemPoissonOffset = newValue)
    fun updateEmailInputField(newValue: InputFieldState) =
        copy(emailInputField = newValue)

    fun updateCardHolderInputField(newValue: InputFieldState) =
        copy(cardHolderInputField = newValue)

    fun updateCardNumberInputField(newValue: InputFieldState) =
        copy(cardNumberInputField = newValue)

    fun updateCardExpireDateInputField(newValue: InputFieldState) =
        copy(cardExpireDateInputField = newValue)

    fun updateCvvInputFieldState(newValue: InputFieldState) =
        copy(cvvInputFieldState = newValue)

    fun updateAllowedPaymentMethodsIcons(newValue: List<Int>) =
        copy(allowedPaymentMethodsIcons = newValue)
}
