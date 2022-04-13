package tech.dojo.pay.sdk.card.data

import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.data.entities.PaymentDetails
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams
import java.net.SocketTimeoutException

internal class CardPaymentRepository(
    private val api: CardPaymentApi,
    private val token: String,
    payload: DojoCardPaymentPayload
) {

    private val paymentDetails = payload.toPaymentDetails()

    suspend fun collectDeviceData(): DeviceData =
        api.collectDeviceData(token, paymentDetails)

    suspend fun processPayment(): PaymentResult {
        val response = api.processPayment(token, paymentDetails)
        val paymentResult = DojoCardPaymentResult.fromCode(response.statusCode)

        return if (paymentResult == DojoCardPaymentResult.AUTHORIZING) {
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

    suspend fun fetch3dsPage(params: ThreeDSParams): String {
        return api.fetchSecurePage(params.stepUpUrl, params.jwt, params.md)
    }

    private fun DojoCardPaymentPayload.toPaymentDetails(): PaymentDetails =
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

    /**
     * API requires the expiry to be in the format mm / yy, with the spaces.
     * To ensure the correct format is being adhered to, formatting is done before any calls
     * are made
     */
    private fun formatExpiryDate(month: String?, year: String?): String? {
        if (month == null || year == null) return null
        return String.format("%s / %s", month, year)
    }

}

