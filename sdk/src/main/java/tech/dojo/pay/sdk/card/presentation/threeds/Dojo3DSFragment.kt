package tech.dojo.pay.sdk.card.presentation.threeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import tech.dojo.pay.sdk.R
import tech.dojo.pay.sdk.card.entities.ThreeDSParams
import tech.dojo.pay.sdk.card.presentation.card.util.ThreeDsInterface

internal class Dojo3DSFragment private constructor() : Fragment() {

    private lateinit var webView: WebView

    private val viewModel by lazy {
        (requireActivity() as Dojo3DSViewModelHost).threeDSViewModel
    }
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

        view.findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            activity?.onBackPressed()
        }
//
//        webView = view.findViewById<WebView>(R.id.webView).apply {
//            settings.javaScriptEnabled = true
//            settings.useWideViewPort = true
//            addJavascriptInterface(ThreeDsInterface(viewModel::on3DSCompleted), "androidListener")
//            webViewClient = object : WebViewClient() {
//                override fun onPageFinished(view: WebView, url: String) {
//                    super.onPageFinished(view, url)
//                    webView.loadUrl(
//                        "javascript:(function() {" +
//                            "var response = document.getElementById('psThreeDSecureResponse').value;\n" +
//                            " androidListener.receiveMessage(response);" +
//                            "})()"
//                    )
//                }
//            }
//        }
//
//        viewModel.threeDsPage.observe(viewLifecycleOwner) {
//            val page = it.replace("<head>", "<head> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">")
//            webView.loadDataWithBaseURL(null, page, "text/html", "utf-8", null)
//        }
//
//        viewModel.fetchThreeDsPage(params)
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
