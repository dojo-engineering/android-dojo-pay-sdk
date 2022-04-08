package tech.dojo.pay.sdk.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import tech.dojo.pay.sdk.R

class Dojo3DSFragment : Fragment() {

    private lateinit var webView: WebView
    private val viewModel: DojoCardPaymentViewModel by activityViewModels()

    private val stepUpUrl by lazy {
        requireNotNull(arguments).getString(KEY_STEP_UP_URL) as String
    }

    private val jwtToken by lazy {
        requireNotNull(arguments).getString(KEY_JWT) as String
    }

    private val md by lazy {
        requireNotNull(arguments).getString(KEY_MD) as String
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
        }

        viewModel.events.observe(viewLifecycleOwner) {
            if (it is DojoCardPaymentEvent.Show3dsScreen) {
                load3dsPage(it.pageContent)
            }
        }

        viewModel.fetchThreeDsPage(stepUpUrl, jwtToken, md)
    }

    private fun load3dsPage(htmlContent: String) {
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
    }

    private fun onWebViewEvent(event: String) {

    }

    companion object {
        const val KEY_STEP_UP_URL = "stepUpUrl"
        const val KEY_JWT = "jwt"
        const val KEY_MD = "md"
    }

}