package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.core.getActivity
import tech.dojo.pay.uisdk.presentation.PaymentFlowContainerActivity
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.DojoBottomSheet
import tech.dojo.pay.uisdk.presentation.components.DojoFullGroundButton
import tech.dojo.pay.uisdk.presentation.components.DojoOutlinedButton
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
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
    contentState: State<PaymentMethodCheckoutState?>,
    onAppBarIconClicked: () -> Unit,
    onGpayClicked: () -> Unit,
    onManagePaymentClicked: () -> Unit
) {
    AppBar(coroutineScope, sheetState, onAppBarIconClicked)
    if (contentState.value?.isLoading == true) {
        Loading()
    } else {
        GooglePayButton(contentState, coroutineScope, sheetState, onGpayClicked)
        PaymentMethodsButton(onManagePaymentClicked)
    }
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
        title = stringResource(id = R.string.dojo_ui_sdk_payment_method_checkout_title),
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
private fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(DojoTheme.colors.background.copy(alpha = 0.8f))
            .clickable(false) {},
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = DojoTheme.colors.primaryLabelTextColor
        )
    }
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
        text = stringResource(id = R.string.dojo_ui_sdk_manage_payment_methods_title)
    ) { onManagePaymentClicked() }
}
