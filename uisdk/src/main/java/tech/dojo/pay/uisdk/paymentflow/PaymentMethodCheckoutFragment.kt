package tech.dojo.pay.uisdk.paymentflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.dojo.pay.uisdk.components.AppBarIcon
import tech.dojo.pay.uisdk.components.DojoAppBar
import tech.dojo.pay.uisdk.components.DojoBottomSheet
import tech.dojo.pay.uisdk.components.DojoFullGroundButton
import tech.dojo.pay.uisdk.components.DojoOutlinedButton
import tech.dojo.pay.uisdk.components.DojoPreview
import tech.dojo.pay.uisdk.components.TitleGravity
import tech.dojo.pay.uisdk.components.theme.DojoTheme

class PaymentMethodCheckoutFragment : Fragment() {
    private val activityArguments: Bundle? by lazy { (activity as PaymentFlowContainerActivity).arguments }

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
        val sheetState =
            rememberModalBottomSheetState(ModalBottomSheetValue.Expanded, confirmStateChange = {
                if (it == ModalBottomSheetValue.Hidden) {
                    this.activity?.finish()
                }
                true
            })
        val progressIndicatorVisible = remember { mutableStateOf(false) }

        val coroutineScope = rememberCoroutineScope()
        DojoTheme {
            DojoBottomSheet(
                modifier = Modifier.fillMaxSize(),
                sheetState = sheetState,
                sheetContent = {
                    BottomSheetItems(
                        coroutineScope,
                        sheetState,
                        progressIndicatorVisible
                    )
                }
            ) {}
        }

    }

    @ExperimentalMaterialApi
    @Composable
    private fun BottomSheetItems(
        coroutineScope: CoroutineScope,
        sheetState: ModalBottomSheetState,
        progressIndicatorVisible: MutableState<Boolean>
    ) {
        DojoAppBar(
            modifier = Modifier.height(60.dp),
            title = "Payment method",
            titleGravity = TitleGravity.LEFT,
            actionIcon = AppBarIcon.close() {
                coroutineScope.launch {
                    sheetState.hide()
                }
                this.activity?.finish()
            }
        )
        DojoFullGroundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 16.dp, 24.dp, 8.dp),
            text = "google pay",
            isLoading = progressIndicatorVisible.value
        ) {}
        DojoOutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 8.dp, 24.dp, 16.dp),
            text = "manage payment methods",
            isLoading = progressIndicatorVisible.value
        ) {}
    }
}
