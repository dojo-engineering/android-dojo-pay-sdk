package tech.dojo.pay.sdk.card.data

import tech.dojo.pay.sdk.card.data.entities.DeviceDataRequest
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import java.net.SocketTimeoutException

internal class CardPaymentRepository(private val api: CardPaymentApi) {

    suspend fun collectDeviceData(token: String, payload: DojoCardPaymentPayload) {
        val deviceData = api.collectDeviceData(token, createDeviceDataRequest(payload))
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

    private fun createDeviceDataRequest(payload: DojoCardPaymentPayload): DeviceDataRequest {
        val card = payload.cardDetails
        return DeviceDataRequest(
            cV2 = card.cv2,
            cardName = card.cardName,
            cardNumber = card.cardNumber,
            expiryDate = card.expiryDate,
            userEmailAddress = payload.userEmailAddress,
            userPhoneNumber = payload.userPhoneNumber,
            billingAddress = payload.billingAddress,
            shippingDetails = payload.shippingDetails,
            metaData = payload.metaData
        )
    }
}

