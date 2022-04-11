package tech.dojo.pay.sdk.card

import android.webkit.JavascriptInterface

internal class ThreeDsInterface(
    private val onEventCallback: ((String) -> Unit)
) {

    @JavascriptInterface
    fun receiveMessage(event: String) {
        onEventCallback.invoke(event)
    }

}