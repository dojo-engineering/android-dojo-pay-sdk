package tech.dojo.pay.uisdk.paymentflow.ui.result

import android.app.Activity
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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import tech.dojo.pay.uisdk.components.DojoPreview
import tech.dojo.pay.uisdk.components.theme.DojoTheme
import tech.dojo.pay.uisdk.paymentflow.ui.result.viewmodel.PaymentResultViewModel
import tech.dojo.pay.uisdk.paymentflow.ui.result.viewmodel.PaymentResultViewModelFactory

class PaymentResultFragment : Fragment() {
    private val args: PaymentResultFragmentArgs by navArgs()
    private val viewModel: PaymentResultViewModel by viewModels {
        PaymentResultViewModelFactory(args)
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
                        color = Color.Black.copy(alpha = 0.6f)
                    ) {
                        ShowResultSheet(activity as Activity, viewModel)
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() = DojoPreview {
        DojoTheme {
            ShowResultSheet(activity as Activity, viewModel)
        }
    }
}
