package tech.dojo.pay.sdk.card.data

import tech.dojo.pay.sdk.card.data.entities.PaymentDetails
import tech.dojo.pay.sdk.card.data.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import java.net.SocketTimeoutException

internal class CardPaymentRepository(private val api: CardPaymentApi) {

    suspend fun processPayment(token: String, payload: DojoCardPaymentPayload): PaymentResult {
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

    private suspend fun processPayment(token: String, paymentDetails: PaymentDetails): PaymentResult =
        api.processPayment(token, paymentDetails)

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

