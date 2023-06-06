package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state

import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity

internal data class ShippingAddressViewState(
    val isVisible: Boolean,
    var itemPoissonOffset: Int,
    var name: InputFieldState = InputFieldState(value = ""),
    var addressLine1: InputFieldState = InputFieldState(value = ""),
    var addressLine2: InputFieldState = InputFieldState(value = ""),
    var city: InputFieldState = InputFieldState(value = ""),
    var postalCode: InputFieldState = InputFieldState(value = ""),
    val supportedCountriesList: List<SupportedCountriesViewEntity>,
    var currentSelectedCountry: SupportedCountriesViewEntity,
    var deliveryNotes: InputFieldState = InputFieldState(value = ""),
    var isShippingSameAsBillingCheckBox: CheckBoxItem = CheckBoxItem(
        R.string.dojo_ui_sdk_card_details_checkout_billing_same_as_shipping, isChecked = true, isVisible = true
    )
) {
    fun updateIsVisible(newValue: Boolean) =
        copy(isVisible = newValue)
    fun updateItemPoissonOffset(newValue: Int) =
        copy(itemPoissonOffset = newValue)
    fun updateAddressName(newValue: InputFieldState) =
        copy(name = newValue)
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

    fun updateDeliveryNotes(newValue: InputFieldState) =
        copy(deliveryNotes = newValue)
    fun updateIsShippingSameAsBillingCheckBox(newValue: CheckBoxItem) =
        copy(isShippingSameAsBillingCheckBox = newValue)
}
