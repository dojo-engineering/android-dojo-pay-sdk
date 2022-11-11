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
import tech.dojo.pay.uisdk.presentation.components.*
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.DojoBottomSheet
import tech.dojo.pay.uisdk.presentation.components.DojoBrandFooter
import tech.dojo.pay.uisdk.presentation.components.DojoFullGroundButton
import tech.dojo.pay.uisdk.presentation.components.DojoOutlinedButton
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.components.WalletItem
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PaymentMethodCheckoutState
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel.PaymentMethodCheckoutViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun PaymentMethodsCheckOutScreen(
    currentSelectedMethod: PaymentMethodItemViewEntityItem?,
    viewModel: PaymentMethodCheckoutViewModel,
    onAppBarIconClicked: () -> Unit,
    onManagePaymentClicked: () -> Unit,
    onPayByCard: () -> Unit
) {
    val activity = LocalContext.current.getActivity<PaymentFlowContainerActivity>()
    val paymentMethodsSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = { false }
        )
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.state.observeAsState().value ?: return
    if (state.gPayConfig?.allowedCardNetworks?.isNotEmpty() == true) {
        CheckGPayAvailability(state.gPayConfig, activity, viewModel)
    }
    if (currentSelectedMethod != null) {
        viewModel.onSavedPaymentMethodChanged(currentSelectedMethod)
    }
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
                onManagePaymentClicked,
                onPayByCard,
                viewModel::onPayAmountClicked,
                viewModel::observePaymentIntent,
                viewModel::onCvvValueChanged
            )
        }
    ) {
        if (state.isBottomSheetVisible) {
            LaunchedEffect(Unit) { paymentMethodsSheetState.show() }
        }
    }
}

@Composable
private fun CheckGPayAvailability(
    gPayConfig: DojoGPayConfig?,
    activity: PaymentFlowContainerActivity?,
    viewModel: PaymentMethodCheckoutViewModel
) {
    if (gPayConfig != null) {
        LaunchedEffect(Unit) {
            DojoSdk.isGpayAvailable(
                activity = activity as Activity,
                dojoGPayConfig = DojoGPayConfig(
                    merchantName = gPayConfig.merchantName,
                    merchantId = gPayConfig.merchantId,
                    gatewayMerchantId = gPayConfig.gatewayMerchantId,
                    allowedCardNetworks = gPayConfig.allowedCardNetworks
                ),
                { viewModel.handleGooglePayAvailable() },
                { viewModel.handleGooglePayUnAvailable() }
            )
        }
    } else {
        LaunchedEffect(Unit) { viewModel.handleGooglePayUnAvailable() }
    }
}

@ExperimentalMaterialApi
@Composable
private fun BottomSheetItems(
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    contentState: PaymentMethodCheckoutState,
    onAppBarIconClicked: () -> Unit,
    onGpayClicked: () -> Unit,
    onManagePaymentClicked: () -> Unit,
    onPayByCard: () -> Unit,
    onPayAmount: () -> Unit,
    observePaymentIntent: () -> Unit,
    onCvvChanged: (String) -> Unit
) {
    AppBar(coroutineScope, sheetState, onAppBarIconClicked)
    if (contentState.isBottomSheetLoading) {
        Loading()
    } else {
        PaymentMethodItem(contentState, onManagePaymentClicked, onCvvChanged)
        AmountBreakDownItem(contentState)
        GooglePayButton(
            contentState,
            coroutineScope,
            sheetState,
            onGpayClicked,
            observePaymentIntent
        )
        PaymentMethodsButton(contentState, onPayByCard, onManagePaymentClicked)
        PayAmountButton(contentState, onPayAmount)
        FooterItem()
    }
}

@Composable
private fun FooterItem() {
    DojoBrandFooter(
        modifier = Modifier.padding(24.dp, 0.dp, 16.dp, 24.dp),
    )
}

@Composable
private fun AmountBreakDownItem(contentState: PaymentMethodCheckoutState) {
    AmountBreakDown(
        modifier = Modifier.padding(top = 16.dp),
        items = contentState.amountBreakDownList,
        totalAmount = contentState.totalAmount
    )
}

@Composable
private fun PaymentMethodItem(
    contentState: PaymentMethodCheckoutState,
    onManagePaymentClicked: () -> Unit,
    onCvvChanged: (String) -> Unit
) {
    contentState.paymentMethodItem?.let {
        if (it is PaymentMethodItemViewEntityItem.WalletItemItem) {
            WalletItem(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                onManagePaymentClicked()
            }
        } else {
            CardItemWithCvv(
                modifier = Modifier.padding(top = 8.dp),
                cvvValue = contentState.cvvFieldState.value,
                onCvvValueChanged = { newValue -> onCvvChanged(newValue) },
                cardItem = it as PaymentMethodItemViewEntityItem.CardItemItem,
                onClick = {
                    onManagePaymentClicked()
                }
            )
        }
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
        actionIcon = AppBarIcon.close(DojoTheme.colors.headerButtonTintColor) {
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
    googlePayVisibility: PaymentMethodCheckoutState,
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    onGpayClicked: () -> Unit,
    observePaymentIntent: () -> Unit
) {
    if (googlePayVisibility.isGooglePayButtonVisible) {
        GooglePayButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 16.dp, 8.dp),
        ) {
            coroutineScope.launch {
                observePaymentIntent()
                sheetState.hide()
                onGpayClicked()
            }
        }
    }
}

@Composable
private fun PaymentMethodsButton(
    contentState: PaymentMethodCheckoutState,
    onPayByCard: () -> Unit,
    onManagePaymentClicked: () -> Unit
) {
    if (contentState.payWithCarButtonState.isVisibleL) {
        if (contentState.payWithCarButtonState.isPrimary) {
            DojoFullGroundButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp, 16.dp, 8.dp),
                text = stringResource(id = R.string.dojo_ui_sdk_pay_with_card_string)
            ) {
                if (contentState.payWithCarButtonState.navigateToCardCheckout) {
                    onPayByCard()
                } else {
                    onManagePaymentClicked()
                }

            }
        } else {
            DojoOutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp, 16.dp, 8.dp),
                text = stringResource(id = R.string.dojo_ui_sdk_pay_with_card_string)
            ) {
                if (contentState.payWithCarButtonState.navigateToCardCheckout) {
                    onPayByCard()
                } else {
                    onManagePaymentClicked()
                }
            }
        }
    }
}

@Composable
private fun PayAmountButton(
    contentState: PaymentMethodCheckoutState,
    onPayAmount: () -> Unit
) {
    contentState.payAmountButtonState?.let {
        DojoFullGroundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp, 16.dp, 8.dp),
            enabled = contentState.payAmountButtonState.isEnabled,
            isLoading = contentState.payAmountButtonState.isLoading,
            text = stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_button_pay) + " " + contentState.totalAmount
        ) {
            if (!contentState.payAmountButtonState.isLoading) {
                onPayAmount()
            }
        }
    }
}