package tech.dojo.pay.uisdk.paymentflow

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
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
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent
import tech.dojo.pay.sdk.card.entities.DojoTotalAmount
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.uisdk.DojoSDKDropInUI
import tech.dojo.pay.uisdk.components.AppBarIcon
import tech.dojo.pay.uisdk.components.DojoAppBar
import tech.dojo.pay.uisdk.components.DojoBottomSheet
import tech.dojo.pay.uisdk.components.DojoFullGroundButton
import tech.dojo.pay.uisdk.components.DojoOutlinedButton
import tech.dojo.pay.uisdk.components.DojoPreview
import tech.dojo.pay.uisdk.components.TitleGravity
import tech.dojo.pay.uisdk.components.theme.DojoTheme
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
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
        DojoSdk.sandbox = DojoSDKDropInUI.sandbox
        paymentToken =
            (activityArguments?.getSerializable(KEY_PARAMS) as? DojoPaymentFlowParams)?.paymentToken
                ?: ""
        gpayPaymentHandler = DojoSdk.createGPayHandler(activity as ComponentActivity) {
            (activity as PaymentFlowContainerActivity).returnResult(it)
            this.activity?.finish()
        }
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
            rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Expanded,
                confirmStateChange = { false }
            )

        val coroutineScope = rememberCoroutineScope()
        val googlePayVisibility = remember { mutableStateOf(true) }
        CheckGooglePayAvailability(googlePayVisibility)

        DojoTheme {
            DojoBottomSheet(
                modifier = Modifier.fillMaxSize(),
                sheetState = sheetState,
                sheetContent = {
                    BottomSheetItems(
                        coroutineScope,
                        sheetState,
                        googlePayVisibility
                    )
                }
            ) {}
        }

    }

    @Composable
    private fun CheckGooglePayAvailability(googlePayVisibility: MutableState<Boolean>) {
        DojoSdk.isGpayAvailable(
            activity = activity as Activity,
            dojoGPayConfig = DojoGPayConfig(
                merchantName = "Dojo Cafe (Paymentsense)",
                merchantId = "BCR2DN6T57R5ZI34",
                gatewayMerchantId = "119784244252745"
            ),
            { googlePayVisibility.value = true },
            { googlePayVisibility.value = false }
        )
    }

    @ExperimentalMaterialApi
    @Composable
    private fun BottomSheetItems(
        coroutineScope: CoroutineScope,
        sheetState: ModalBottomSheetState,
        googlePayVisibility: MutableState<Boolean>
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
        if (googlePayVisibility.value) {
            DojoFullGroundButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 16.dp, 24.dp, 8.dp),
                text = "google pay"
            ) {
                coroutineScope.launch {
                    sheetState.hide()
                }
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
        DojoOutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 8.dp, 24.dp, 16.dp),
            text = "manage payment methods"
        ) {}
    }
}
