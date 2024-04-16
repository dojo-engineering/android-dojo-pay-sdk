package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state

import tech.dojo.pay.sdk.card.entities.CardsSchemes

internal data class CardDetailsViewState(
    val isVisible: Boolean,
    var emailInputField: InputFieldState,
    var cardHolderInputField: InputFieldState,
    var cardNumberInputField: InputFieldState,
    var cardExpireDateInputField: InputFieldState,
    var cvvInputFieldState: InputFieldState,
    val allowedPaymentMethodsIcons: List<Int>,
    val allowedCardSchemes: List<CardsSchemes>,
) {
    fun updateIsVisible(newValue: Boolean) =
        copy(isVisible = newValue)
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

    fun updateAllowedCardSchemes(newValue: List<CardsSchemes>) =
        copy(allowedCardSchemes= newValue)
}
