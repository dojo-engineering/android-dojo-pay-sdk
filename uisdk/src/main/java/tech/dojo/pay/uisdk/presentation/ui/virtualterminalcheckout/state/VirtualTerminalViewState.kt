package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state

import androidx.annotation.StringRes
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity

internal data class VirtualTerminalViewState(
    val isLoading: Boolean,
    val paymentDetailsSection: PaymentDetailsViewState? = null,
    val shippingAddressSection: ShippingAddressViewState? = null,
    val billingAddressSection: BillingAddressViewState? = null,
    val cardDetailsSection: CardDetailsViewState? = null,
    val payButtonSection: PayButtonViewState? = null
)

internal data class PaymentDetailsViewState(
    val merchantName: String,
    val totalAmount: String,
    val amountCurrency: String,
)

internal data class ShippingAddressViewState(
    val isVisible: Boolean,
    var name: InputFieldState = InputFieldState(value = ""),
    var addressLine1: InputFieldState = InputFieldState(value = ""),
    var addressLine2: InputFieldState = InputFieldState(value = ""),
    var city: InputFieldState = InputFieldState(value = ""),
    var postalCode: InputFieldState = InputFieldState(value = ""),
    val supportedCountriesList: List<SupportedCountriesViewEntity>,
    var currentSelectedCountry: SupportedCountriesViewEntity,
    var isShippingSameAsBillingCheckBox: CheckBoxItem = CheckBoxItem(
        R.string.dojo_ui_sdk_card_details_checkout_save_card, isChecked = true, isVisible = true
    )
)

internal data class BillingAddressViewState(
    var isVisible: Boolean,
    var addressLine1: InputFieldState = InputFieldState(value = ""),
    var addressLine2: InputFieldState = InputFieldState(value = ""),
    var city: InputFieldState = InputFieldState(value = ""),
    var postalCode: InputFieldState = InputFieldState(value = ""),
    val supportedCountriesList: List<SupportedCountriesViewEntity>,
    var currentSelectedCountry: SupportedCountriesViewEntity
)

internal data class CardDetailsViewState(
    val isVisible: Boolean,
    var emailInputField: InputFieldState,
    var cardHolderInputField: InputFieldState,
    var cardNumberInputField: InputFieldState,
    var cardExpireDateInputField: InputFieldState,
    var cvvInputFieldState: InputFieldState,
    val allowedPaymentMethodsIcons: List<Int>
)

internal data class PayButtonViewState(
    val isEnabled: Boolean = false,
    val isLoading: Boolean = false
)

internal data class InputFieldState(
    val value: String,
    @StringRes val errorMessages: Int? = null,
    val isError: Boolean = false,
    val isVisible: Boolean = true
)

internal data class CheckBoxItem(
    @StringRes val messageText: Int,
    val isChecked: Boolean,
    val isVisible: Boolean
)

internal enum class InputFieldType {
    NAME,
    ADDRESS1,
    CITY,
    POSTAL_CODE,
    Email,
    CARD_HOLDER_NAME,
    CARD_NUMBER,
    CVV,
    EXPIRE_DATA
}
