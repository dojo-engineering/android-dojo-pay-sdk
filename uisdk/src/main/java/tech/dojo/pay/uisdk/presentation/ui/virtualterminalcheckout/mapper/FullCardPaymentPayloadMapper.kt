package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.mapper

import tech.dojo.pay.sdk.card.entities.DojoAddressDetails
import tech.dojo.pay.sdk.card.entities.DojoCardDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.sdk.card.entities.DojoShippingDetails
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.BillingAddressViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.CardDetailsViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.ShippingAddressViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.VirtualTerminalViewState

internal class FullCardPaymentPayloadMapper {

    fun apply(currentState: VirtualTerminalViewState): DojoCardPaymentPayLoad.FullCardPaymentPayload {
        val cardDetails = currentState.cardDetailsSection
        val shippingAddress = currentState.shippingAddressSection
        val billingAddress = currentState.billingAddressSection
        return DojoCardPaymentPayLoad.FullCardPaymentPayload(
            cardDetails = mapToDojoCardDetails(cardDetails),
            shippingDetails = mapToDojoShippingDetails(shippingAddress),
            billingAddress = mapToDojoAddressDetails(shippingAddress, billingAddress),
            userEmailAddress = cardDetails?.emailInputField?.value,
            metaData = getMetaData(shippingAddress)
        )
    }

    private fun mapToDojoAddressDetails(
        shippingAddress: ShippingAddressViewState?,
        billingAddress: BillingAddressViewState?
    ) =
        if (shippingAddress?.isVisible == true && shippingAddress.isShippingSameAsBillingCheckBox.isChecked) {
            DojoAddressDetails(
                address1 = shippingAddress.addressLine1.value,
                address2 = shippingAddress.addressLine2.value,
                city = shippingAddress.city.value,
                postcode = shippingAddress.postalCode.value,
                countryCode = shippingAddress.currentSelectedCountry.countryCode
            )
        } else {
            DojoAddressDetails(
                address1 = billingAddress?.addressLine1?.value ?: "",
                address2 = billingAddress?.addressLine2?.value ?: "",
                city = billingAddress?.city?.value ?: "",
                postcode = billingAddress?.postalCode?.value ?: "",
                countryCode = billingAddress?.currentSelectedCountry?.countryCode ?: ""
            )
        }

    private fun mapToDojoShippingDetails(shippingAddress: ShippingAddressViewState?) =
        if (shippingAddress?.isVisible == true) {
            DojoShippingDetails(
                name = shippingAddress.name.value,
                address = DojoAddressDetails(
                    address1 = shippingAddress.addressLine1.value,
                    address2 = shippingAddress.addressLine2.value,
                    city = shippingAddress.city.value,
                    postcode = shippingAddress.postalCode.value,
                    countryCode = shippingAddress.currentSelectedCountry.countryCode
                )
            )
        } else {
            null
        }

    private fun mapToDojoCardDetails(cardDetails: CardDetailsViewState?) =
        DojoCardDetails(
            cardNumber = cardDetails?.cardNumberInputField?.value ?: "",
            cardName = cardDetails?.cardHolderInputField?.value ?: "",
            expiryMonth = getExpiryMonth(cardDetails?.cardExpireDateInputField?.value),
            expiryYear = getExpiryYear(cardDetails?.cardExpireDateInputField?.value),
            cv2 = cardDetails?.cvvInputFieldState?.value ?: ""
        )
    private fun getExpiryMonth(expireDateValueValue: String?) =
        if (!expireDateValueValue.isNullOrBlank()) {
            expireDateValueValue.substring(0, 2)
        } else {
            ""
        }

    private fun getExpiryYear(expireDateValueValue: String?) =
        if (!expireDateValueValue.isNullOrBlank() && expireDateValueValue.length > 2) {
            expireDateValueValue.substring(2, 4)
        } else {
            ""
        }

    private fun getMetaData(shippingAddress: ShippingAddressViewState?): Map<String, String> {
        val deliveryNotes: String = shippingAddress?.deliveryNotes?.value ?: ""
        return mapOf("DeliveryNotes" to deliveryNotes)
    }
}
