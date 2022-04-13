package tech.dojo.pay.sdk.card.fingerprint

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import tech.dojo.pay.sdk.card.DojoCardPaymentViewModel
import tech.dojo.pay.sdk.card.data.entities.DeviceData

internal class FingerPrintCaptureFragment private constructor(): Fragment() {

    private val viewModel: DojoCardPaymentViewModel by activityViewModels()

    private val deviceData: DeviceData by lazy {
        requireArguments().getSerializable(KEY_DEVICE_DATA) as DeviceData
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = WebView(requireContext()).apply {
        settings.javaScriptEnabled = true
        addJavascriptInterface(FingerPrintCaptureListener(), "Android")
        val html = getHtml(deviceData.token, deviceData.formAction)
        loadData(html, "text/html", "utf-8")
    }

    private fun getHtml(token: String, formAction: String): String =
        """
        <html>
            <body onload="document.forms[0].submit()">
                <form id="ddc-form" target=”ddc-iframe”  method="POST" action="$formAction">
                    <input id="ddc-input" name="JWT" type="hidden" value="$token" />
                </form>
                <iframe name=”ddc-iframe” height="1" width="1"> </iframe>
            </body>
            <script>
                window.addEventListener('message', (event) => {
                    Android.onFingerPrintCaptured();
                });
            </script>
        </html>
        """.trimIndent()

    private inner class FingerPrintCaptureListener {

        @JavascriptInterface
        fun onFingerPrintCaptured() {
            viewModel.onFingerprintCaptured()
        }
    }

    companion object {

        fun newInstance(deviceData: DeviceData): FingerPrintCaptureFragment =
            FingerPrintCaptureFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_DEVICE_DATA, deviceData)
                }
            }

        private const val KEY_DEVICE_DATA = "DEVICE_DATA"
    }

}