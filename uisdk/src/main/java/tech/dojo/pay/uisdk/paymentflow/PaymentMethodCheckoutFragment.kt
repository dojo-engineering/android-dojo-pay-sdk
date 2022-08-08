package tech.dojo.pay.uisdk.paymentflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import tech.dojo.pay.uisdk.components.AppBarIcon
import tech.dojo.pay.uisdk.components.DojoAppBar
import tech.dojo.pay.uisdk.components.DojoBottomSheet
import tech.dojo.pay.uisdk.components.DojoPreview
import tech.dojo.pay.uisdk.components.TitleGravity
import tech.dojo.pay.uisdk.components.theme.DojoTheme

class PaymentMethodCheckoutFragment : Fragment() {
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
                        ShowPaymentMethodsSheet()
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() = DojoPreview {
        DojoTheme {
            ShowPaymentMethodsSheet()
        }
    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
    @Composable
    fun ShowPaymentMethodsSheet() {
        val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded, confirmStateChange = {
            if (it == ModalBottomSheetValue.Hidden) {
                this.activity?.finish()
            }
            true
        })
        val coroutineScope = rememberCoroutineScope()
        DojoTheme {
            DojoBottomSheet(
                modifier = Modifier.fillMaxSize(),
                sheetState = sheetState,
                sheetContent = { BottomSheetItems(coroutineScope, sheetState) }
            ) {}
        }
    }

    @ExperimentalMaterialApi
    @Composable
    private fun BottomSheetItems(
        coroutineScope: CoroutineScope,
        sheetState: ModalBottomSheetState
    ) {
        DojoAppBar(
            modifier = Modifier.height(200.dp),
            title = "Payment method",
            titleGravity = TitleGravity.LEFT,
            actionIcon = AppBarIcon.close(isSecondary = true) {
                coroutineScope.launch {
                    sheetState.hide()
                }
                this.activity?.finish()
            }
        )
    }
}
