package tech.dojo.pay.sdk.card.data

import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.data.entities.PaymentDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams
import java.util.Locale

internal class CardPaymentRepository(
    private val api: CardPaymentApi,
    private val token: String,
    private val payload: DojoCardPaymentPayLoad
) {

    private val paymentDetails = when (payload) {
        is DojoCardPaymentPayLoad.NormalCardPaymentPayload -> payload.toPaymentDetails()
        is DojoCardPaymentPayLoad.SavedCardPaymentPayLoad -> payload.toPaymentDetails()
    }

    suspend fun collectDeviceData(): DeviceData =
        api.collectDeviceData(token, paymentDetails)

    suspend fun processPayment(): PaymentResult {
        val response = processCardPaymentCall()
        val paymentResult = DojoPaymentResult.fromCode(response.statusCode)

        return if (paymentResult == DojoPaymentResult.AUTHORIZING) {
            PaymentResult.ThreeDSRequired(
                ThreeDSParams(
                    stepUpUrl = requireNotNull(response.stepUpUrl),
                    jwt = requireNotNull(response.jwt),
                    md = requireNotNull(response.md)
                )
            )
        } else {
            PaymentResult.Completed(paymentResult)
        }
    }

    private suspend fun processCardPaymentCall() =
        when (payload) {
            is DojoCardPaymentPayLoad.NormalCardPaymentPayload -> api.processPayment(token, paymentDetails)
            is DojoCardPaymentPayLoad.SavedCardPaymentPayLoad -> api.processPaymentForSaverCard(
                token,
                paymentDetails
            )
        }

    private fun DojoCardPaymentPayLoad.NormalCardPaymentPayload.toPaymentDetails(): PaymentDetails =
        PaymentDetails(
            cV2 = cardDetails.cv2,
            cardName = cardDetails.cardName,
            cardNumber = cardDetails.cardNumber,
            expiryDate = formatExpiryDate(cardDetails.expiryMonth, cardDetails.expiryYear),
            userEmailAddress = userEmailAddress,
            userPhoneNumber = userPhoneNumber,
            billingAddress = billingAddress,
            shippingDetails = shippingDetails,
            metaData = metaData
        )

    private fun DojoCardPaymentPayLoad.SavedCardPaymentPayLoad.toPaymentDetails(): PaymentDetails =
        PaymentDetails(
            cV2 = cv2,
            paymentMethodId = paymentMethodId
        )

    /**
     * API requires the expiry to be in the format mm / yy, with the spaces.
     * To ensure the correct format is being adhered to, formatting is done before any calls
     * are made
     */
    private fun formatExpiryDate(month: String?, year: String?): String? {
        if (month == null || year == null) return null
        return String.format(Locale.getDefault(), "%s / %s", month, year)
    }
}
