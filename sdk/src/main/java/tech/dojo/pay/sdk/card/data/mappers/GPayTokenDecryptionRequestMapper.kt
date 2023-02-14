package tech.dojo.pay.sdk.card.data.mappers

import org.json.JSONObject
import tech.dojo.pay.sdk.card.data.entities.DecryptGPayTokenBody

internal class GPayTokenDecryptionRequestMapper {
    fun apply(
        paymentInformationJson: String
    ): DecryptGPayTokenBody? {
        val gpayToken = getGpayToken(paymentInformationJson)
        return try {
            if (gpayToken != null) {
                DecryptGPayTokenBody(gpayToken)
            } else { null }
        } catch (e: Exception) { null }
    }

    private fun getGpayToken(
        paymentInformationJson: String,
    ): String? {
        return try {
            JSONObject(paymentInformationJson).getJSONObject("paymentMethodData")
                .getJSONObject("tokenizationData")
                .getString("token")
        } catch (e: Exception) {
            null
        }
    }
}
