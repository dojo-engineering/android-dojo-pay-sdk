package tech.dojo.pay.sdk.card

import android.webkit.JavascriptInterface
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import tech.dojo.pay.sdk.DojoPaymentResult

internal class ThreeDsInterface(
    private val onEventCallback: ((DojoPaymentResult) -> Unit)
) {

    @JavascriptInterface
    fun receiveMessage(event: String) {
        val gson = GsonBuilder().create()
        val type = object : TypeToken<Map<String, Any>>() {}.type
        val result = gson.fromJson<Map<String, Any>>(event, type)

        val statusCode = (result["statusCode"] as? Double)?.toInt()

        val paymentResult = if (statusCode != null) {
            DojoPaymentResult.fromCode(statusCode)
        } else {
            DojoPaymentResult.SDK_INTERNAL_ERROR
        }

        onEventCallback.invoke(paymentResult)
    }
}
