package tech.dojo.pay.sdk.card

import android.webkit.JavascriptInterface
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult

internal class ThreeDsInterface(
    private val onEventCallback: ((DojoCardPaymentResult) -> Unit)
) {

    @JavascriptInterface
    fun receiveMessage(event: String) {
        val gson = GsonBuilder().create()
        val type = object : TypeToken<Map<String, Any>>() {}.type
        val result = gson.fromJson<Map<String, Any>>(event, type)

        if (result.containsKey("messageData")) {
            val paymentResult = if (result.containsKey("statusCode") && result["statusCode"] is Int) {
                requireNotNull(
                    DojoCardPaymentResult.values().find { it.code == result["statusCode"] })
            } else {
                DojoCardPaymentResult.SDK_INTERNAL_ERROR
            }
            onEventCallback.invoke(paymentResult)
        }
    }

}