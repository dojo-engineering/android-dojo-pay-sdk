package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state

import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity

internal data class BillingAddressViewState(
    var isVisible: Boolean,
    var addressLine1: InputFieldState = InputFieldState(value = ""),
    var addressLine2: InputFieldState = InputFieldState(value = ""),
    var city: InputFieldState = InputFieldState(value = ""),
    var postalCode: InputFieldState = InputFieldState(value = ""),
    var supportedCountriesList: List<SupportedCountriesViewEntity>,
    var currentSelectedCountry: SupportedCountriesViewEntity,
) {
    fun updateIsVisible(newValue: Boolean) =
        copy(isVisible = newValue)
    fun updateAddressLine1(newValue: InputFieldState) =
        copy(addressLine1 = newValue)
    fun updateAddressLine2(newValue: InputFieldState) =
        copy(addressLine2 = newValue)
    fun updateCity(newValue: InputFieldState) =
        copy(city = newValue)
    fun updatePostalCode(newValue: InputFieldState) =
        copy(postalCode = newValue)
    fun updateSupportedCountriesList(newValue: List<SupportedCountriesViewEntity>) =
        copy(supportedCountriesList = newValue)
    fun updateCurrentSelectedCountry(newValue: SupportedCountriesViewEntity) =
        copy(currentSelectedCountry = newValue)
}
