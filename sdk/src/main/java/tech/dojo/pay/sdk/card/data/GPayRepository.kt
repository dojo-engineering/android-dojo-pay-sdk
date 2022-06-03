package tech.dojo.pay.sdk.card.data

import tech.dojo.pay.sdk.card.data.entities.GPayDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams

internal class GPayRepository (
    private val api: CardPaymentApi,
    private val token: String,
    private val payload: String
) {

//    private val paymentDetails = payload.toPaymentDetails()

    suspend fun processPayment(gPayPayload: GPayDetails): PaymentResult {
        val response = api.processGPay(token, gPayPayload)
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

}