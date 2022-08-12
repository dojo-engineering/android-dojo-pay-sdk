package tech.dojo.pay.uisdk.paymentflow.ui.paymentmethodcheckout

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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import tech.dojo.pay.uisdk.components.AppBarIcon
import tech.dojo.pay.uisdk.components.DojoAppBar
import tech.dojo.pay.uisdk.components.DojoBottomSheet
import tech.dojo.pay.uisdk.components.DojoFullGroundButton
import tech.dojo.pay.uisdk.components.DojoOutlinedButton
import tech.dojo.pay.uisdk.components.TitleGravity
import tech.dojo.pay.uisdk.paymentflow.ui.paymentmethodcheckout.state.PaymentMethodCheckoutState
import tech.dojo.pay.uisdk.paymentflow.ui.paymentmethodcheckout.viewmodel.PaymentMethodCheckoutViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ShowPaymentMethodsSheet(
    viewModel: PaymentMethodCheckoutViewModel,
    onAppBarIconClicked: () -> Unit,
    onGpayClicked: () -> Unit,
    onManagePaymentClicked: () -> Unit
) {
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
                onGpayClicked,
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
