package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state

import androidx.annotation.StringRes
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity

internal data class CardDetailsCheckoutState(
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
    var saveCardCheckBox: CheckBoxItem = CheckBoxItem(
        isVisible = false,
        isChecked = true,
        messageText = R.string.dojo_ui_sdk_card_details_checkout_save_card,
    ),
    var actionButtonState: ActionButtonState = ActionButtonState(),
)

internal data class InputFieldState(
    val value: String,
    @StringRes val errorMessages: Int? = null,
    val isError: Boolean = false,
)

internal data class CheckBoxItem(
    @StringRes val messageText: Int,
    val isChecked: Boolean,
    val isVisible: Boolean,
)

internal data class ActionButtonState(
    val isLoading: Boolean = false,
    val isEnabled: Boolean = false,
    val text: String = "",
)
