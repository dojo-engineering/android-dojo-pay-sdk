package tech.dojo.pay.sdk.card.data

import android.util.Log
import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.data.mappers.CardPaymentRequestMapper
import tech.dojo.pay.sdk.card.data.remote.cardpayment.CardPaymentApi
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad

internal class DeviceDataRepository(
    private val api: CardPaymentApi,
    private val token: String,
    private val requestMapper: CardPaymentRequestMapper = CardPaymentRequestMapper()
) {
    companion object {
        private const val TAG = "DeviceDataRepository"
    }

    suspend fun collectDeviceData(payload: DojoCardPaymentPayLoad): DeviceData {
        val paymentDetails = requestMapper.mapToPaymentDetails(payload)
        val result = api.collectDeviceData(token, paymentDetails)
        Log.d(TAG, "collectDeviceData: Response received. eligible=${result.eligible}")
        return result
    }
}
