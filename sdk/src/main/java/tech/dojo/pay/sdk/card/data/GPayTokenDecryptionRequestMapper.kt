package tech.dojo.pay.sdk.card.data

import com.google.gson.Gson
import org.json.JSONObject
import tech.dojo.pay.sdk.card.data.entities.DecryptGPayTokenBody

class GPayTokenDecryptionRequestMapper(private val gson: Gson) {
    fun apply(
        paymentInformationJson: String
    ): DecryptGPayTokenBody? {
        getGpayToken(paymentInformationJson)?.let {
            return gson.fromJson(
                it,
                DecryptGPayTokenBody::class.java
            )
        }
        return null
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