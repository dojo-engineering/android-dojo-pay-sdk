package tech.dojo.pay.sdk.card.data.mappers

import tech.dojo.pay.sdk.card.data.entities.PaymentDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import java.util.Locale

internal class CardPaymentRequestMapper {
    fun mapToPaymentDetails(payload: DojoCardPaymentPayLoad): PaymentDetails {
        return when (payload) {
            is DojoCardPaymentPayLoad.FullCardPaymentPayload -> payload.toPaymentDetails()
            is DojoCardPaymentPayLoad.SavedCardPaymentPayLoad -> payload.toPaymentDetails()
        }
    }

    private fun DojoCardPaymentPayLoad.FullCardPaymentPayload.toPaymentDetails(): PaymentDetails =
        PaymentDetails(
            cV2 = cardDetails.cv2,
            cardName = cardDetails.cardName,
            cardNumber = cardDetails.cardNumber,
            expiryDate = formatExpiryDate(cardDetails.expiryMonth, cardDetails.expiryYear),
            userEmailAddress = userEmailAddress,
            savePaymentMethod = savePaymentMethod,
            mitConsentGiven = cardDetails.mitConsentGiven,
            userPhoneNumber = userPhoneNumber,
            billingAddress = billingAddress,
            shippingDetails = shippingDetails,
            metaData = metaData,
        )

    private fun DojoCardPaymentPayLoad.SavedCardPaymentPayLoad.toPaymentDetails(): PaymentDetails =
        PaymentDetails(
            cV2 = cv2,
            paymentMethodId = paymentMethodId,
            userEmailAddress = userEmailAddress,
            userPhoneNumber = userPhoneNumber,
            shippingDetails = shippingDetails,
            metaData = metaData,
        )

    /**
     * API requires the expiry to be in the format mm / yy, with the spaces.
     * To ensure the correct format is being adhered to, formatting is done before any calls
     * are made
     */
    private fun formatExpiryDate(month: String?, year: String?): String? {
        if (month == null || year == null) return null
        val expireYear = if (year.length == 4) { year.substring(2, 4) } else year
        return String.format(Locale.getDefault(), "%s / %s", month, expireYear)
    }
}
