package tech.dojo.pay.sdk.card.data

import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.entities.GPayDetails
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams

internal class GPayRepository(
    private val api: CardPaymentApi,
    private val token: String,
    private val paymentIntent: DojoPaymentIntent
) {

    suspend fun processPayment(gPayPayload: GPayDetails): PaymentResult {
        val response = api.processGPay(token, gPayPayload)
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

}