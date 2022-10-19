package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state

import tech.dojo.pay.uisdk.domain.entities.SupportedCountriesDomainEntity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity

internal data class CardDetailsCheckoutState(
    val totalAmount: String,
    val amountCurrency: String,
    val cardHolderInputField: InputFieldState,
    val emailInputField: InputFieldState,
    val isEmailInputFieldRequired: Boolean,
    val cardDetailsInPutField: CardDetailsInputFieldState,
    val isBillingCountryFieldRequired: Boolean,
    val supportedCountriesList: List<SupportedCountriesViewEntity>,
    val currentSelectedCountry: SupportedCountriesViewEntity,
    val isPostalCodeFieldRequired: Boolean,
    val postalCodeField: InputFieldState,
    val isLoading: Boolean,
    val isEnabled: Boolean
)

data class InputFieldState(val value: String)

data class CardDetailsInputFieldState(
    val cardNumberValue: String,
    val cvvValue: String,
    val expireDateValueValue: String,
)
