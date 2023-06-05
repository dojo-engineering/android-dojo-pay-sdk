package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state

import androidx.annotation.StringRes
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.BillingAddressViewState as BillingAddressViewState1

internal data class VirtualTerminalViewState(
    val isLoading: Boolean,
    val paymentDetailsSection: PaymentDetailsViewState? = null,
    val shippingAddressSection: ShippingAddressViewState? = null,
    val billingAddressSection: BillingAddressViewState1? = null,
    val cardDetailsSection: CardDetailsViewState? = null,
    val payButtonSection: PayButtonViewState? = null
)

internal data class PaymentDetailsViewState(
    val merchantName: String,
    val totalAmount: String,
    val amountCurrency: String,
    val orderId: String
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
