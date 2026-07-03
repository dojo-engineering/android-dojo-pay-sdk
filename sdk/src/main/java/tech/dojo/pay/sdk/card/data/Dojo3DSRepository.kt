package tech.dojo.pay.sdk.card.data

import android.util.Log
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.entities.AuthorizationBody
import tech.dojo.pay.sdk.card.data.entities.ValidateCardinalResponse
import tech.dojo.pay.sdk.card.data.remote.cardpayment.CardPaymentApi
import tech.dojo.pay.sdk.card.entities.PaymentResult

internal class Dojo3DSRepository(
    private val api: CardPaymentApi,
    private val token: String
) {
    companion object {
        private const val TAG = "Dojo3DSRepository"
    }

    suspend fun processAuthorization(
        jwt: String,
        transactionId: String,
        validateResponse: ValidateResponse?
    ): PaymentResult {
        if (jwt.isEmpty()) {
            Log.w(TAG, "processAuthorization: jwt is empty — authorization will likely fail.")
        }
        if (transactionId.isEmpty()) {
            Log.w(TAG, "processAuthorization: transactionId is empty — authorization will likely fail.")
        }
        val response =
            api.processAuthorization(
                token,
                AuthorizationBody(
                    jwt,
                    transactionId,
                    ValidateCardinalResponse(
                        isValidated = validateResponse?.isValidated,
                        errorNumber = validateResponse?.errorNumber,
                        errorDescription = validateResponse?.errorDescription ?: "",
                        actionCode = validateResponse?.actionCode?.string ?: ""
                    )
                )
            )
        val paymentResult = DojoPaymentResult.fromCode(response.statusCode)
        Log.d(TAG, "processAuthorization: statusCode=${response.statusCode}, paymentResult=$paymentResult")
        return PaymentResult.Completed(paymentResult)
    }
}
