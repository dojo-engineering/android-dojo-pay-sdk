package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state

import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity

internal data class CardDetailsCheckoutState(
    var toolbarTitle: String = "",
    var orderId: String? = null,
    var merchantName: String? = null,
    var isLoading: Boolean = false,
    var totalAmount: String = "",
    var amountCurrency: String = "",
    var allowedPaymentMethodsIcons: List<Int> = listOf(),
    var cardHolderInputField: InputFieldState = InputFieldState(value = ""),
    var emailInputField: InputFieldState = InputFieldState(value = ""),
    var isEmailInputFieldRequired: Boolean = false,
    var cardNumberInputField: InputFieldState = InputFieldState(value = ""),
    var cardExpireDateInputField: InputFieldState = InputFieldState(value = ""),
    var cvvInputFieldState: InputFieldState = InputFieldState(value = ""),
    var isBillingCountryFieldRequired: Boolean = false,
    var supportedCountriesList: List<SupportedCountriesViewEntity> = listOf(),
    var currentSelectedCountry: SupportedCountriesViewEntity = SupportedCountriesViewEntity(
        "",
        "",
        false,
    ),
    var isPostalCodeFieldRequired: Boolean = false,
    var postalCodeField: InputFieldState = InputFieldState(value = ""),
    var checkBoxItem: CheckBoxItem = CheckBoxItem(
        isVisible = false,
        isChecked = true,
        messageText = "",
    ),
    var actionButtonState: ActionButtonState = ActionButtonState(),
)

internal data class InputFieldState(
    val value: String,
    val errorMessages: String? = null,
    val isError: Boolean = false,
)

internal data class CheckBoxItem(
    val messageText: String,
    val isChecked: Boolean,
    val isVisible: Boolean,
)

internal data class ActionButtonState(
    val isLoading: Boolean = false,
    val isEnabled: Boolean = false,
    val text: String = "",
) {
    fun updateIsEnabled(newValue: Boolean) =
        copy(isEnabled = newValue)

    fun updateIsLoading(newValue: Boolean) =
        copy(isLoading = newValue)

    fun updateText(newValue: String) =
        copy(text = newValue)
}
