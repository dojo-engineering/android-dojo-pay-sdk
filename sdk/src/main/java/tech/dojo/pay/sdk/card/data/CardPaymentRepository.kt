package tech.dojo.pay.sdk.card.data

import android.util.Log
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.mappers.CardPaymentRequestMapper
import tech.dojo.pay.sdk.card.data.remote.cardpayment.CardPaymentApi
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams

internal class CardPaymentRepository(
    private val api: CardPaymentApi,
    private val token: String,
    private val payload: DojoCardPaymentPayLoad,
    private val requestMapper: CardPaymentRequestMapper = CardPaymentRequestMapper()
) {

    companion object {
        private const val TAG = "CardPaymentRepository"
    }

    private val paymentDetails = requestMapper.mapToPaymentDetails(payload)

    suspend fun processPayment(): PaymentResult {
        val response = processCardPaymentCall()
        val paymentResult = DojoPaymentResult.fromCode(response.statusCode)
        Log.d(TAG, "processPayment: statusCode=${response.statusCode}, paymentResult=$paymentResult")

        return if (paymentResult == DojoPaymentResult.AUTHORIZING) {
            Log.d(TAG, "processPayment: 3DS required.")
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
            is DojoCardPaymentPayLoad.FullCardPaymentPayload -> api.processPaymentForFullCard(token, paymentDetails)
            is DojoCardPaymentPayLoad.SavedCardPaymentPayLoad -> api.processPaymentForSaverCard(token, paymentDetails)
        }
}
