package tech.dojo.pay.sdk.card.data

import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.data.entities.DeviceDataRequest
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload

internal class CardPaymentRepository(private val api: CardPaymentApi) {

    suspend fun collectDeviceData(token: String, payload: DojoCardPaymentPayload): DeviceData =
        api.collectDeviceData(token, createDeviceDataRequest(payload))

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

