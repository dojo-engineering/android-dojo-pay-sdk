package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.AmountBreakDown
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.CardItemWithCvv
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.DojoBottomSheet
import tech.dojo.pay.uisdk.presentation.components.DojoBrandFooter
import tech.dojo.pay.uisdk.presentation.components.DojoBrandFooterModes
import tech.dojo.pay.uisdk.presentation.components.DojoFullGroundButton
import tech.dojo.pay.uisdk.presentation.components.DojoOutlinedButton
import tech.dojo.pay.uisdk.presentation.components.GooglePayButton
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.components.WalletItem
import tech.dojo.pay.uisdk.presentation.components.WindowSize
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PaymentMethodCheckoutState
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel.PaymentMethodCheckoutViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun PaymentMethodsCheckOutScreen(
    windowSize: WindowSize,
    currentSelectedMethod: PaymentMethodItemViewEntityItem?,
    viewModel: PaymentMethodCheckoutViewModel,
    onAppBarIconClicked: () -> Unit,
    onManagePaymentClicked: () -> Unit,
    onPayByCard: () -> Unit,
    showDojoBrand: Boolean,
    additionalLegalText: String
) {
    val paymentMethodsSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing,
            ),
            confirmValueChange = { false },
            skipHalfExpanded = true,
        )
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.state.observeAsState().value ?: return
    if (currentSelectedMethod != null) {
        viewModel.onSavedPaymentMethodChanged(currentSelectedMethod)
    }
    DojoBottomSheet(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize(),
        sheetState = paymentMethodsSheetState,
        sheetBackgroundColor = DojoTheme.colors.primarySurfaceBackgroundColor,
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
                viewModel::onCvvValueChanged,
                windowSize,
                showDojoBrand,
                additionalLegalText
            )
        },
    ) {
        if (state.isBottomSheetVisible) {
            LaunchedEffect(Unit) { paymentMethodsSheetState.show() }
        }
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
    onCvvChanged: (String) -> Unit,
    windowSize: WindowSize,
    showDojoBrand: Boolean,
    additionalLegalText: String,
) {
    AppBar(coroutineScope, sheetState, onAppBarIconClicked)
    if (contentState.isBottomSheetLoading) {
        Loading()
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(fraction = if (windowSize.widthWindowType == WindowSize.WindowType.COMPACT) 1f else .6f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PaymentMethodItem(contentState, onManagePaymentClicked, onCvvChanged)
                AmountBreakDownItem(contentState)
                GooglePayButton(
                    contentState,
                    onGpayClicked,
                )
                PaymentMethodsButton(contentState, onPayByCard, onManagePaymentClicked)
                PayAmountButton(contentState, onPayAmount)
                FooterItem(showDojoBrand, additionalLegalText)
            }
        }
    }
}

@Composable
private fun FooterItem(showDojoBrand: Boolean, additionalLegalText: String) {
    val bottomPadding = if (additionalLegalText.isNotEmpty()) 8 else 24
    if (showDojoBrand) {
        DojoBrandFooter(
            modifier = Modifier.padding(24.dp, 8.dp, 16.dp, bottomPadding.dp),
            mode = DojoBrandFooterModes.DOJO_BRAND_ONLY,
        )
    } else {
        DojoBrandFooter(
            modifier = Modifier.padding(24.dp, 8.dp, 16.dp, 8.dp),
            mode = DojoBrandFooterModes.NONE,
        )
    }

    if (additionalLegalText.isNotEmpty()) {
        Text(
            text = additionalLegalText,
            style = DojoTheme.typography.subtitle2,
            color = DojoTheme.colors.secondaryLabelTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 16.dp)
        )
    }
}

@Composable
private fun AdditionalLegalText(text: String) {
    if (text.isNotEmpty()) {
        Text(
            text = text,
            style = DojoTheme.typography.subtitle2,
            color = DojoTheme.colors.secondaryLabelTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 16.dp)
        )
    }
}

@Composable
private fun AmountBreakDownItem(contentState: PaymentMethodCheckoutState) {
    AmountBreakDown(
        modifier = Modifier.padding(top = 16.dp),
        items = contentState.amountBreakDownList,
        totalAmount = contentState.totalAmount,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PaymentMethodItem(
    contentState: PaymentMethodCheckoutState,
    onManagePaymentClicked: () -> Unit,
    onCvvChanged: (String) -> Unit,
) {
    contentState.paymentMethodItem?.let {
        if (it is PaymentMethodItemViewEntityItem.WalletItemItem) {
            WalletItem(
                modifier = Modifier.padding(top = 8.dp),
            ) {
                onManagePaymentClicked()
            }
        } else {
            val keyboardController = LocalSoftwareKeyboardController.current
            CardItemWithCvv(
                modifier = Modifier.padding(top = 8.dp),
                cvvValue = contentState.cvvFieldState.value,
                onCvvValueChanged = { newValue -> onCvvChanged(newValue) },
                cardItem = it as PaymentMethodItemViewEntityItem.CardItemItem,
                onClick = {
                    onManagePaymentClicked()
                },
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                }),
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun AppBar(
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    onAppBarIconClicked: () -> Unit,
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
        },
    )
}

@ExperimentalMaterialApi
@Composable
private fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(DojoTheme.colors.primarySurfaceBackgroundColor.copy(alpha = 0.8f))
            .clickable(false) {},
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = DojoTheme.colors.loadingIndicatorColor,
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun GooglePayButton(
    googlePayVisibility: PaymentMethodCheckoutState,
    onGpayClicked: () -> Unit,
) {
    if (googlePayVisibility.isGooglePayButtonVisible) {
        GooglePayButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 16.dp, 8.dp),
        ) { onGpayClicked() }
    }
}

@Composable
private fun PaymentMethodsButton(
    contentState: PaymentMethodCheckoutState,
    onPayByCard: () -> Unit,
    onManagePaymentClicked: () -> Unit,
) {
    if (contentState.payWithCardButtonState.isVisible) {
        if (contentState.payWithCardButtonState.isPrimary) {
            DojoFullGroundButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp, 16.dp, 8.dp),
                text = stringResource(id = R.string.dojo_ui_sdk_pay_with_card_string),
            ) {
                if (contentState.payWithCardButtonState.navigateToCardCheckout) {
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
                text = stringResource(id = R.string.dojo_ui_sdk_pay_with_card_string),
            ) {
                if (contentState.payWithCardButtonState.navigateToCardCheckout) {
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
    onPayAmount: () -> Unit,
) {
    contentState.payAmountButtonState?.let {
        DojoFullGroundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp, 16.dp, 8.dp),
            enabled = contentState.payAmountButtonState.isEnabled,
            isLoading = contentState.payAmountButtonState.isLoading,
            text = stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_button_pay) + " " + contentState.totalAmount,
        ) {
            if (!contentState.payAmountButtonState.isLoading) {
                onPayAmount()
            }
        }
    }
}
