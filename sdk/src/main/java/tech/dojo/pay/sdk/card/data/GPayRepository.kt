package tech.dojo.pay.sdk.card.data

import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.entities.DecryptGPayTokenBody
import tech.dojo.pay.sdk.card.data.entities.GPayDetails
import tech.dojo.pay.sdk.card.data.remote.cardpayment.CardPaymentApi
import tech.dojo.pay.sdk.card.entities.AuthMethod.Companion.fromAuthMethod
import tech.dojo.pay.sdk.card.entities.DecryptGPayTokenParams
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams

internal class GPayRepository(
    private val api: CardPaymentApi,
    private val token: String
) {

    suspend fun decryptGPayToken(decryptGPayTokenBody: DecryptGPayTokenBody): DecryptGPayTokenParams {
        val response = api.decryptGPayToken(token, decryptGPayTokenBody)
        return DecryptGPayTokenParams(
            authMethod = fromAuthMethod(response.paymentMethodDetails.authMethod),
            pan = response.paymentMethodDetails.pan,
            expirationMonth = response.paymentMethodDetails.expirationMonth,
            expirationYear = response.paymentMethodDetails.expirationYear
        )
    }

    fun sanitiseExpiryMonth(originalExpiryMonth: String): String {
        return "%02d".format(originalExpiryMonth.toInt()) // GPay may return 5 but BE expects 05
    }

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
