package tech.dojo.pay.sdk.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import tech.dojo.pay.sdk.R
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

        fun newInstance(params: ThreeDSParams): Dojo3DSFragment =
            Dojo3DSFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_PARAMS, params)
                }
            }

        private const val KEY_PARAMS = "PARAMS"
    }
}