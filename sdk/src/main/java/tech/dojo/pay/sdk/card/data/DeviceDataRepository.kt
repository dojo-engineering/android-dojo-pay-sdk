package tech.dojo.pay.sdk.card.data

import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.data.mappers.CardPaymentRequestMapper
import tech.dojo.pay.sdk.card.data.remote.cardpayment.CardPaymentApi
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad

internal class DeviceDataRepository(
    private val api: CardPaymentApi,
    private val token: String,
    private val requestMapper: CardPaymentRequestMapper = CardPaymentRequestMapper()
) {
    suspend fun collectDeviceData(payload: DojoCardPaymentPayLoad): DeviceData {
        val paymentDetails = requestMapper.mapToPaymentDetails(payload)
        return api.collectDeviceData(token, paymentDetails)
    }
}