package tech.dojo.pay.uisdk.paymentflow.ui.carddetailscheckout

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.DojoCardDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.uisdk.components.theme.DojoTheme
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.paymentflow.PaymentFlowContainerActivity
import tech.dojo.pay.uisdk.paymentflow.contract.DojoPaymentFlowHandlerResultContract

class CardDetailsCheckoutFragment : Fragment() {
    private val activityArguments: Bundle? by lazy {
        (activity as PaymentFlowContainerActivity).arguments
    }
    private lateinit var paymentToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                DojoTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.White
                    ) {
                        CardDetailsCheckoutScreen(
                            {
                                (activity as PaymentFlowContainerActivity).returnResult(
                                    DojoPaymentResult.DECLINED
                                )
                                activity?.finish()
                            },
                            { findNavController().popBackStack() },
                            {
                                DojoSdk.startCardPayment(
                                    activity as Activity,
                                    paymentToken,
                                    getPaymentPayLoad()
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    private fun getPaymentPayLoad(): DojoCardPaymentPayLoad.FullCardPaymentPayload =
        DojoCardPaymentPayLoad.FullCardPaymentPayload(
            DojoCardDetails(
                cardNumber = "4456530000001096",
                cardName = "Card holder",
                expiryMonth = "12",
                expiryYear = "24",
                cv2 = "020"
            )
        )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = DojoSdk.parseCardPaymentResult(requestCode, resultCode, data)
        (activity as PaymentFlowContainerActivity).returnResult(
            result ?: DojoPaymentResult.DECLINED
        )
        this.view?.findNavController()
            ?.navigate(getNavDirections(result ?: DojoPaymentResult.DECLINED))
    }

    private fun getNavDirections(result: DojoPaymentResult): NavDirections =
        CardDetailsCheckoutFragmentDirections.cardDetailsCheckoutFragmentToPaymentResult(result)

    private fun getIntentParams() {
        paymentToken =
            (activityArguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as? DojoPaymentFlowParams)?.paymentToken
                ?: ""
    }
}
