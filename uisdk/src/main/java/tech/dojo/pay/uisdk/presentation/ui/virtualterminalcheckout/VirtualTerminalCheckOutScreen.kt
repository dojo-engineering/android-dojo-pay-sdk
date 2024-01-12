package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout

import android.content.Context
import android.view.inputmethod.InputMethodManager
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.AmountWithMerchantIInfoHeader
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.KeyboardController
import tech.dojo.pay.uisdk.presentation.components.SingleButtonView
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.components.WindowSize
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.VirtualTerminalViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.viewmodel.VirtualTerminalViewModel
import java.util.Locale

@Suppress("LongMethod")
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
    val view = LocalView.current
    val keyboardController = object : KeyboardController {
        val imm = LocalContext.current.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        override fun show() {
            imm?.showSoftInput(view, 0)
        }
        override fun hide() {
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
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
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = if (windowSize.widthWindowType == WindowSize.WindowType.COMPACT) 1f else .6f)
                        .padding(it),
                ) {
                    Column(
                        Modifier
                            .verticalScroll(scrollState)
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(32.dp),
                    ) {
                        AmountWithMerchantIInfoHeader(
                            amount = state.paymentDetailsSection?.totalAmount.orEmpty(),
                            currencyLogo = state.paymentDetailsSection?.amountCurrency.orEmpty(),
                            merchantName = state.paymentDetailsSection?.merchantName.orEmpty(),
                        )
                        ShippingAddressSection(
                            viewModel = viewModel,
                            coroutineScope = coroutineScope,
                            scrollState = scrollState,
                            keyboardController = keyboardController,
                        )
                        BillingAddressSection(
                            viewModel = viewModel,
                            coroutineScope = coroutineScope,
                            scrollState = scrollState,
                            keyboardController = keyboardController,
                        )
                        CardDetailsSection(
                            viewModel = viewModel,
                            isDarkModeEnabled = isDarkModeEnabled,
                            coroutineScope = coroutineScope,
                            scrollState = scrollState,
                            keyboardController = keyboardController,
                            showDojoBrand = showDojoBrand,
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .background(DojoTheme.colors.primarySurfaceBackgroundColor),
                    ) {
                        PayButton(scrollState, state, viewModel)
                    }
                }
            }
            Loading(isVisible = state.isLoading)
        },
    )
}

@Composable
private fun Loading(isVisible: Boolean) {
    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DojoTheme.colors.primarySurfaceBackgroundColor)
                .clickable(false) {},
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                color = DojoTheme.colors.loadingIndicatorColor,
            )
        }
    }
}

@Composable
private fun AppBarItem(onBackClicked: () -> Unit, onCloseClicked: () -> Unit) {
    DojoAppBar(
        title = stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_title_payment_details),
        titleGravity = TitleGravity.LEFT,
        navigationIcon = AppBarIcon.back(DojoTheme.colors.headerButtonTintColor) { onBackClicked() },
        actionIcon = AppBarIcon.close(DojoTheme.colors.headerButtonTintColor) { onCloseClicked() },
    )
}

@Composable
private fun PayButton(
    scrollState: ScrollState,
    state: VirtualTerminalViewState,
    viewModel: VirtualTerminalViewModel,
) {
    val focusManager = LocalFocusManager.current
    SingleButtonView(
        scrollState = scrollState,
        text =
        String
            .format(Locale.getDefault(), "%s %s %s", stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_button_pay), state.paymentDetailsSection?.amountCurrency, state.paymentDetailsSection?.totalAmount),
        isLoading = state.payButtonSection?.isLoading ?: false,
        enabled = state.payButtonSection?.isEnabled ?: false,
    ) {
        if (state.payButtonSection?.isLoading == false) {
            focusManager.clearFocus()
            viewModel.onPayClicked()
        }
    }
}
