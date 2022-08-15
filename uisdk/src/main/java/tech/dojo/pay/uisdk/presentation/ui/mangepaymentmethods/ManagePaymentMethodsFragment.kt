package tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.presentation.components.DojoPreview
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.PaymentFlowContainerActivity

class ManagePaymentMethodsFragment : Fragment() {
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
                        ManagePaymentMethods(
                            {
                                (activity as PaymentFlowContainerActivity).returnResult(
                                    DojoPaymentResult.DECLINED
                                )
                                activity?.finish()
                            },
                            { findNavController().popBackStack() },
                            {
                                findNavController().navigate(
                                    ManagePaymentMethodsFragmentDirections
                                        .managePaymentMethodsFragmentToCardDetailsCheckoutFragment()
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() = DojoPreview {
        DojoTheme {
            ManagePaymentMethods({
                (activity as PaymentFlowContainerActivity).returnResult(
                    DojoPaymentResult.DECLINED
                )
                activity?.finish()
            }, {
                findNavController().popBackStack()
            }, {})
        }
    }
}
