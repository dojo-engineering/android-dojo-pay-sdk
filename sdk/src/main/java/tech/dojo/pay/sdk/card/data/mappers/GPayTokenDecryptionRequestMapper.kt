package tech.dojo.pay.sdk.card.data.mappers

import com.google.gson.Gson
import org.json.JSONObject
import tech.dojo.pay.sdk.card.data.entities.DecryptGPayTokenBody

internal class GPayTokenDecryptionRequestMapper(private val gson: Gson) {
    fun apply(
        paymentInformationJson: String
    ): DecryptGPayTokenBody? {
        val gpayToken = getGpayToken(paymentInformationJson)
        return try {
            if (gpayToken != null) {
                gson.fromJson(gpayToken, DecryptGPayTokenBody::class.java)
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