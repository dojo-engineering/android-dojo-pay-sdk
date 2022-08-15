package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.uisdk.core.getActivity
import tech.dojo.pay.uisdk.presentation.PaymentFlowContainerActivity
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.DojoBottomSheet
import tech.dojo.pay.uisdk.presentation.components.DojoFullGroundButton
import tech.dojo.pay.uisdk.presentation.components.DojoOutlinedButton
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PaymentMethodCheckoutState
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel.PaymentMethodCheckoutViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun PaymentMethodsCheckOutScreen(
    viewModel: PaymentMethodCheckoutViewModel,
    onAppBarIconClicked: () -> Unit,
    onManagePaymentClicked: () -> Unit
) {
    val activity = LocalContext.current.getActivity<PaymentFlowContainerActivity>()
    LaunchedEffect(Unit) {
        DojoSdk.isGpayAvailable(
            activity = activity as Activity,
            dojoGPayConfig = DojoGPayConfig(
                merchantName = "Dojo Cafe (Paymentsense)",
                merchantId = "BCR2DN6T57R5ZI34",
                gatewayMerchantId = "119784244252745"
            ),
            { viewModel.handleGooglePayAvailable() },
            { viewModel.handleGooglePayUnAvailable() }
        )
    }
    val paymentMethodsSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = { false }
        )
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.state.observeAsState()
    DojoBottomSheet(
        modifier = Modifier.fillMaxSize(),
        sheetState = paymentMethodsSheetState,
        sheetContent = {
            BottomSheetItems(
                coroutineScope,
                paymentMethodsSheetState,
                state,
                onAppBarIconClicked,
                viewModel::onGpayCLicked,
                onManagePaymentClicked
            )
        }
    ) {
        if (state.value?.isBottomSheetVisible == true) {
            LaunchedEffect(Unit) { paymentMethodsSheetState.show() }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun BottomSheetItems(
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    googlePayVisibility: State<PaymentMethodCheckoutState?>,
    onAppBarIconClicked: () -> Unit,
    onGpayClicked: () -> Unit,
    onManagePaymentClicked: () -> Unit
) {
    AppBar(coroutineScope, sheetState, onAppBarIconClicked)
    GooglePayButton(googlePayVisibility, coroutineScope, sheetState, onGpayClicked)
    PaymentMethodsButton(onManagePaymentClicked)
}

@ExperimentalMaterialApi
@Composable
private fun AppBar(
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    onAppBarIconClicked: () -> Unit
) {
    DojoAppBar(
        modifier = Modifier.height(60.dp),
        title = "Payment method",
        titleGravity = TitleGravity.LEFT,
        actionIcon = AppBarIcon.close() {
            coroutineScope.launch {
                sheetState.hide()
            }
            onAppBarIconClicked()
        }
    )
}

@ExperimentalMaterialApi
@Composable
private fun GooglePayButton(
    googlePayVisibility: State<PaymentMethodCheckoutState?>,
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    onGpayClicked: () -> Unit
) {
    if (googlePayVisibility.value?.isGooglePayVisible == true) {
        DojoFullGroundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 16.dp, 24.dp, 8.dp),
            text = "google pay"
        ) {
            coroutineScope.launch {
                sheetState.hide()
                onGpayClicked()
            }
        }
    }
}

@Composable
private fun PaymentMethodsButton(onManagePaymentClicked: () -> Unit) {
    DojoOutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 8.dp, 24.dp, 16.dp),
        text = "manage payment methods"
    ) { onManagePaymentClicked() }
}
