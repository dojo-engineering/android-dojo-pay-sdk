package tech.dojo.pay.uisdk.paymentflow.ui.paymentmethodcheckout

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent
import tech.dojo.pay.sdk.card.entities.DojoTotalAmount
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.uisdk.DojoSDKDropInUI
import tech.dojo.pay.uisdk.components.DojoPreview
import tech.dojo.pay.uisdk.components.theme.DojoTheme
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.paymentflow.PaymentFlowContainerActivity
import tech.dojo.pay.uisdk.paymentflow.contract.DojoPaymentFlowHandlerResultContract.Companion.KEY_PARAMS

class PaymentMethodCheckoutFragment : Fragment() {
    private val activityArguments: Bundle? by lazy {
        (activity as PaymentFlowContainerActivity).arguments
    }
    private lateinit var paymentToken: String
    private lateinit var gpayPaymentHandler: DojoGPayHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        configureDojoPayCore()
        getIntentParams()
        return ComposeView(requireContext()).apply {
            setContent {
                DojoTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Black.copy(alpha = 0.6f)
                    ) {
                        BuildPaymentMethodsSheet()
                    }
                }
            }
        }
    }

    private fun configureDojoPayCore() {
        DojoSdk.sandbox = DojoSDKDropInUI.sandbox
        gpayPaymentHandler = DojoSdk.createGPayHandler(activity as ComponentActivity) {
            (activity as PaymentFlowContainerActivity).returnResult(it)
            this.view?.findNavController()?.navigate(getNavDirections(it))
        }
    }

    private fun getNavDirections(result: DojoPaymentResult): NavDirections =
        PaymentMethodCheckoutFragmentDirections.paymentMethodCheckoutFragmentToPaymentResult(result)

    private fun getIntentParams() {
        paymentToken =
            (activityArguments?.getSerializable(KEY_PARAMS) as? DojoPaymentFlowParams)?.paymentToken
                ?: ""
    }

    @Composable
    private fun BuildPaymentMethodsSheet() {
        ShowPaymentMethodsSheet(activity as Activity) {
            gpayPaymentHandler.executeGPay(
                GPayPayload = DojoGPayPayload(
                    DojoGPayConfig(
                        merchantName = "Dojo Cafe (Paymentsense)",
                        merchantId = "BCR2DN6T57R5ZI34",
                        gatewayMerchantId = "119784244252745"
                    )
                ),
                paymentIntent = DojoPaymentIntent(
                    token = paymentToken,
                    totalAmount = DojoTotalAmount(10, "GBP")
                )
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() = DojoPreview {
        DojoTheme {
            BuildPaymentMethodsSheet()
        }
    }
}
