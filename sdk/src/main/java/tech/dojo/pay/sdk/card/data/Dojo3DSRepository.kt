package tech.dojo.pay.sdk.card.data

import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.entities.AuthorizationBody
import tech.dojo.pay.sdk.card.data.remote.cardpayment.CardPaymentApi
import tech.dojo.pay.sdk.card.entities.PaymentResult

internal class Dojo3DSRepository(
    private val api: CardPaymentApi,
    private val token: String
) {
    suspend fun processAuthorization(
        jwt: String,
        transactionId: String,
        validateResponse: ValidateResponse?
    ): PaymentResult {
        val response =
            api.processAuthorization(token, AuthorizationBody(jwt, transactionId, validateResponse))
        val paymentResult = DojoPaymentResult.fromCode(response.statusCode)
        return PaymentResult.Completed(paymentResult)
    }
}
