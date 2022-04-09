package tech.dojo.pay.sdk.card.data

import tech.dojo.pay.sdk.card.data.entities.PaymentDetails
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams
import java.net.SocketTimeoutException

internal class CardPaymentRepository(private val api: CardPaymentApi) {

    suspend fun makePayment(token: String, payload: DojoCardPaymentPayload): PaymentResult {
        val paymentDetails = payload.toPaymentDetails()
        collectDeviceData(token, paymentDetails)
        return processPayment(token, paymentDetails)
    }

    private suspend fun collectDeviceData(token: String, paymentDetails: PaymentDetails) {
        val deviceData = api.collectDeviceData(token, paymentDetails)
        try {
            api.handleDataCollection(deviceData.formAction, deviceData.token)
        } catch (e: SocketTimeoutException) {
            // Ignore timeout exceptions
        }
    }

    suspend fun fetch3dsPage(url: String, token: String, md: String): String {
        return try {
            api.fetchSecurePage(url, token, md)
        } catch (e: Exception) {
            ""
        }
    }

    private suspend fun processPayment(token: String, paymentDetails: PaymentDetails): PaymentResult {
        val response = api.processPayment(token, paymentDetails)
        val paymentResult = requireNotNull(
            DojoCardPaymentResult.values().find { it.code == response.statusCode }
        )

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

    private fun DojoCardPaymentPayload.toPaymentDetails(): PaymentDetails =
        PaymentDetails(
            cV2 = cardDetails.cv2,
            cardName = cardDetails.cardName,
            cardNumber = cardDetails.cardNumber,
            expiryDate = cardDetails.expiryDate,
            userEmailAddress = userEmailAddress,
            userPhoneNumber = userPhoneNumber,
            billingAddress = billingAddress,
            shippingDetails = shippingDetails,
            metaData = metaData
        )
}

