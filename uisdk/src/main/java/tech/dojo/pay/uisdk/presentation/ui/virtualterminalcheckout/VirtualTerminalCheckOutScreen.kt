package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.AmountWithMerchantIInfoHeader
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.SingleButtonView
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.components.WindowSize
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.VirtualTerminalViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.viewmodel.VirtualTerminalViewModel

@Suppress("LongMethod")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
internal fun VirtualTerminalCheckOutScreen(
    windowSize: WindowSize,
    viewModel: VirtualTerminalViewModel,
    onCloseClicked: () -> Unit,
    onBackClicked: () -> Unit,
    isDarkModeEnabled: Boolean,
    showDojoBrand: Boolean,
) {
    val state = viewModel.state.observeAsState().value ?: return
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var scrollToPosition by remember { mutableStateOf(0F) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = DojoTheme.colors.primarySurfaceBackgroundColor,
        topBar = { AppBarItem(onBackClicked, onCloseClicked) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 40.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.isLoading) {
                    Loading()
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(fraction = if (windowSize.widthWindowType == WindowSize.WindowType.COMPACT) 1f else .6f)
                            .padding(it)
                    ) {
                        Column(
                            Modifier
                                .verticalScroll(scrollState)
                                .wrapContentHeight()
                                .onGloballyPositioned { layoutCoordinates ->
                                    scrollToPosition =
                                        scrollState.value + layoutCoordinates.positionInRoot().y
                                }
                                .imePadding()
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
                            verticalArrangement = Arrangement.spacedBy(32.dp)
                        ) {
                            AmountWithMerchantIInfoHeader(
                                amount = state.paymentDetailsSection?.totalAmount.orEmpty(),
                                currencyLogo = state.paymentDetailsSection?.amountCurrency.orEmpty(),
                                merchantName = state.paymentDetailsSection?.merchantName.orEmpty(),
                                orderInfo = stringResource(id = R.string.dojo_ui_sdk_order_info) +
                                    state.paymentDetailsSection?.orderId.orEmpty()
                            )
                            ShippingAddressSection(
                                viewModel = viewModel,
                                coroutineScope = coroutineScope,
                                scrollToPosition = scrollToPosition,
                                scrollState = scrollState,
                                keyboardController = keyboardController
                            )
                            BillingAddressSection(
                                viewModel = viewModel,
                                coroutineScope = coroutineScope,
                                scrollToPosition = scrollToPosition,
                                scrollState = scrollState,
                                keyboardController = keyboardController
                            )
                            CardDetailsSection(
                                viewModel = viewModel,
                                isDarkModeEnabled = isDarkModeEnabled,
                                coroutineScope = coroutineScope,
                                scrollToPosition = scrollToPosition,
                                scrollState = scrollState,
                                keyboardController = keyboardController,
                                showDojoBrand = showDojoBrand
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(0.dp),
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .background(DojoTheme.colors.primarySurfaceBackgroundColor)
                        ) {
                            PayButton(
                                scrollState,
                                state,
                                viewModel,
                                keyboardController
                            )
                        }
                    }
                }
            }
        }
    )
}

@ExperimentalMaterialApi
@Composable
private fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DojoTheme.colors.primarySurfaceBackgroundColor.copy(alpha = 0.8f))
            .clickable(false) {},
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = DojoTheme.colors.loadingIndicatorColor
        )
    }
}

@Composable
private fun AppBarItem(onBackClicked: () -> Unit, onCloseClicked: () -> Unit) {
    DojoAppBar(
        title = stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_title_payment_details),
        titleGravity = TitleGravity.LEFT,
        navigationIcon = AppBarIcon.back(DojoTheme.colors.headerButtonTintColor) { onBackClicked() },
        actionIcon = AppBarIcon.close(DojoTheme.colors.headerButtonTintColor) { onCloseClicked() }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PayButton(
    scrollState: ScrollState,
    state: VirtualTerminalViewState,
    viewModel: VirtualTerminalViewModel,
    keyboardController: SoftwareKeyboardController?,
) {
    SingleButtonView(
        scrollState = scrollState,
        text = stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_button_pay) + " " + state.paymentDetailsSection?.amountCurrency + " " + state.paymentDetailsSection?.totalAmount,
        isLoading = state.payButtonSection?.isLoading ?: false,
        enabled = state.payButtonSection?.isEnabled ?: false
    ) {
        if (state.payButtonSection?.isLoading == false) {
            keyboardController?.hide()
            viewModel.onPayClicked()
        }
    }
}
