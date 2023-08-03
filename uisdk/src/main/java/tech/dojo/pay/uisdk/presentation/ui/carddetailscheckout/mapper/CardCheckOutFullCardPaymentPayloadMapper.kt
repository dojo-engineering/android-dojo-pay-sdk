package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.mapper

import tech.dojo.pay.sdk.card.entities.DojoAddressDetails
import tech.dojo.pay.sdk.card.entities.DojoCardDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsCheckoutState

internal class CardCheckOutFullCardPaymentPayloadMapper {

    fun getPaymentPayLoad(
        currentState: CardDetailsCheckoutState,
        isStartDestination: Boolean,
    ): DojoCardPaymentPayLoad.FullCardPaymentPayload =
        DojoCardPaymentPayLoad.FullCardPaymentPayload(
            userEmailAddress = if (currentState.isEmailInputFieldRequired) currentState.emailInputField.value else null,
            billingAddress = DojoAddressDetails(
                countryCode = if (currentState.isBillingCountryFieldRequired) currentState.currentSelectedCountry.countryCode else null,
                postcode = if (currentState.isPostalCodeFieldRequired) currentState.postalCodeField.value else null,
            ),
            savePaymentMethod = if (!isStartDestination) currentState.checkBoxItem.isChecked else null,
            cardDetails = DojoCardDetails(
                cardNumber = currentState.cardNumberInputField.value,
                cardName = currentState.cardHolderInputField.value,
                expiryMonth = getExpiryMonth(currentState),
                expiryYear = getExpiryYear(currentState),
                cv2 = currentState.cvvInputFieldState.value,
                mitConsentGiven = if (isStartDestination) currentState.checkBoxItem.isChecked else null,
            ),
        )

    private fun getExpiryMonth(currentState: CardDetailsCheckoutState) =
        if (currentState.cardExpireDateInputField.value.isNotBlank()) {
            currentState.cardExpireDateInputField.value.substring(0, 2)
        } else {
            ""
        }

    private fun getExpiryYear(currentState: CardDetailsCheckoutState) =
        if (currentState.cardExpireDateInputField.value.isNotBlank() && currentState.cardExpireDateInputField.value.length > 2) {
            currentState.cardExpireDateInputField.value.substring(2, 4)
        } else {
            ""
        }
}
