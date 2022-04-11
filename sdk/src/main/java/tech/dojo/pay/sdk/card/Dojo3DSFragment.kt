package tech.dojo.pay.sdk.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import tech.dojo.pay.sdk.R
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams


internal class Dojo3DSFragment private constructor() : Fragment() {

    private lateinit var webView: WebView
    private val viewModel: DojoCardPaymentViewModel by activityViewModels()

    private val params: ThreeDSParams by lazy {
        requireArguments().getSerializable(KEY_PARAMS) as ThreeDSParams
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_dojo_3ds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = view.findViewById<WebView>(R.id.webView).apply {
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            addJavascriptInterface(ThreeDsInterface(::onThreeDsEventChanged), "androidListener")
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    webView.loadUrl(
                        "javascript:(function() {" +
                                "window.parent.addEventListener ('message', function(event) {" +
                                " androidListener.receiveMessage(JSON.stringify(event.data));});" +
                                "})()"
                    )
                }
            }
        }

        viewModel.paymentResult.observe(viewLifecycleOwner) {
            if (it is PaymentResult.ShowThreeDsPage) {
                val page = it.pageContent.replace("<head>", "<head> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">")
                webView.loadDataWithBaseURL(null, page, "text/html", "utf-8", null)
            }
        }

        viewModel.fetchThreeDsPage(params)
    }

    private fun onThreeDsEventChanged(event: String) {

    }

    companion object {

        fun newInstance(params: ThreeDSParams): Dojo3DSFragment =
            Dojo3DSFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_PARAMS, params)
                }
            }

        private const val KEY_PARAMS = "PARAMS"
    }
}