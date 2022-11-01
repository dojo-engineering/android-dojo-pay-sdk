package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state

import androidx.annotation.StringRes
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity

internal data class CardDetailsCheckoutState(
    val totalAmount: String,
    val amountCurrency: String,
    val allowedPaymentMethodsIcons: List<Int>,
    val cardHolderInputField: InputFieldState,
    val emailInputField: InputFieldState,
    val isEmailInputFieldRequired: Boolean,
    val cardNumberInputField: InputFieldState,
    val cardExpireDateInputField: InputFieldState,
    val cvvInputFieldState: InputFieldState,
    val isBillingCountryFieldRequired: Boolean,
    val supportedCountriesList: List<SupportedCountriesViewEntity>,
    val currentSelectedCountry: SupportedCountriesViewEntity,
    val isPostalCodeFieldRequired: Boolean,
    val postalCodeField: InputFieldState,
    val isLoading: Boolean,
    val isEnabled: Boolean
)

data class InputFieldState(
    val value: String,
    @StringRes val errorMessages: Int? = null,
    val isError: Boolean = false
)
