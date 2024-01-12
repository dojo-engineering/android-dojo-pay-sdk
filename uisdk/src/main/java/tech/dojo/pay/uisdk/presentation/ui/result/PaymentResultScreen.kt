package tech.dojo.pay.uisdk.presentation.ui.result

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.DojoBottomSheet
import tech.dojo.pay.uisdk.presentation.components.DojoBrandFooter
import tech.dojo.pay.uisdk.presentation.components.DojoBrandFooterModes
import tech.dojo.pay.uisdk.presentation.components.DojoFullGroundButton
import tech.dojo.pay.uisdk.presentation.components.DojoOutlinedButton
import tech.dojo.pay.uisdk.presentation.components.DojoSpacer
import tech.dojo.pay.uisdk.presentation.components.KeyboardController
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.components.WindowSize
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.bold
import tech.dojo.pay.uisdk.presentation.ui.result.state.PaymentResultState
import tech.dojo.pay.uisdk.presentation.ui.result.viewmodel.PaymentResultViewModel

@OptIn(ExperimentalMaterialApi::class)
@Suppress("LongMethod")
@Composable
internal fun ShowResultSheetScreen(
    windowSize: WindowSize,
    onCloseFlowClicked: () -> Unit,
    onTryAgainClicked: () -> Unit,
    viewModel: PaymentResultViewModel,
    showDojoBrand: Boolean,
) {
    val paymentResultSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing,
            ),
            confirmValueChange = { false },
            skipHalfExpanded = true,
        )
    val coroutineScope = rememberCoroutineScope()
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

    LaunchedEffect(Unit) {
        keyboardController.hide()
        paymentResultSheetState.show()
    }

    DojoBottomSheet(
        modifier = Modifier.fillMaxSize(),
        sheetState = paymentResultSheetState,
        sheetContent = {
            BottomSheetItems(
                coroutineScope,
                paymentResultSheetState,
                state,
                onCloseFlowClicked,
                onTryAgainClicked,
                viewModel,
                windowSize,
                showDojoBrand,
            )
        },
    ) {}
}

@ExperimentalMaterialApi
@Composable
private fun BottomSheetItems(
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    state: PaymentResultState,
    onCloseFlowClicker: () -> Unit,
    onTryAgainClicked: () -> Unit,
    viewModel: PaymentResultViewModel,
    windowSize: WindowSize,
    showDojoBrand: Boolean,
) {
    DojoAppBar(
        modifier = Modifier.height(120.dp),
        title = state.appBarTitle,
        titleGravity = TitleGravity.LEFT,
        titleColor = DojoTheme.colors.headerTintColor,
        actionIcon = AppBarIcon.close(tintColor = DojoTheme.colors.headerButtonTintColor) {
            coroutineScope.launch {
                sheetState.hide()
            }
            onCloseFlowClicker()
        },
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(fraction = if (windowSize.widthWindowType == WindowSize.WindowType.COMPACT) 1f else .6f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (state) {
                is PaymentResultState.SuccessfulResult -> SuccessfulResult(
                    state,
                    coroutineScope,
                    sheetState,
                    onCloseFlowClicker,
                    showDojoBrand,
                )

                is PaymentResultState.FailedResult -> FailedResult(
                    state,
                    coroutineScope,
                    sheetState,
                    onCloseFlowClicker,
                    onTryAgainClicked,
                    viewModel,
                    showDojoBrand,

                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SuccessfulResult(
    state: PaymentResultState.SuccessfulResult,
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    onCloseFlowClicker: () -> Unit,
    showDojoBrand: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.padding(bottom = 16.dp),
            painter = painterResource(id = state.imageId),
            contentDescription = "",
            contentScale = ContentScale.Crop,
        )
        Text(
            text = state.status,
            style = DojoTheme.typography.h5.bold,
            color = DojoTheme.colors.primaryLabelTextColor,
        )
        Text(
            text = state.orderInfo,
            style = DojoTheme.typography.subtitle1,
            textAlign = TextAlign.Center,
            color = DojoTheme.colors.primaryLabelTextColor,
            modifier = Modifier.padding(top = 16.dp),
        )

        DojoSpacer(height = 32.dp)
        DojoFullGroundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            text = stringResource(id = R.string.dojo_ui_sdk_payment_result_button_done),
            backgroundColor = DojoTheme.colors.primaryCTAButtonActiveBackgroundColor,
            onClick = {
                coroutineScope.launch {
                    sheetState.hide()
                }
                onCloseFlowClicker()
            },
        )

        DojoBrandFooter(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            mode = if (showDojoBrand) {
                DojoBrandFooterModes.DOJO_BRAND_ONLY
            } else {
                DojoBrandFooterModes.NONE
            },
        )
        DojoSpacer(height = 16.dp)
    }
}

@Suppress("LongMethod")
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FailedResult(
    state: PaymentResultState.FailedResult,
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    onCloseFlowClicker: () -> Unit,
    onTryAgainClicked: () -> Unit,
    viewModel: PaymentResultViewModel,
    showDojoBrand: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.padding(bottom = 16.dp),
            painter = painterResource(id = state.imageId),
            contentDescription = "",
            contentScale = ContentScale.Crop,
        )

        Text(
            text = state.status,
            style = DojoTheme.typography.h5.bold,
            textAlign = TextAlign.Center,
            color = DojoTheme.colors.primaryLabelTextColor,
            modifier = Modifier.padding(top = 16.dp),
        )

        Text(
            text = state.details,
            style = DojoTheme.typography.subtitle1,
            color = DojoTheme.colors.secondaryLabelTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp),
        )

        DojoFullGroundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            text = stringResource(id = R.string.dojo_ui_sdk_payment_result_button_try_again),
            backgroundColor = DojoTheme.colors.primaryCTAButtonActiveBackgroundColor,
            onClick = { viewModel.onTryAgainClicked() },
        )

        DojoOutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            text = stringResource(id = R.string.dojo_ui_sdk_payment_result_button_done),
            borderStrokeColor = DojoTheme.colors.primaryCTAButtonActiveBackgroundColor,
            onClick = {
                coroutineScope.launch {
                    sheetState.hide()
                }
                onCloseFlowClicker()
            },
        )

        DojoBrandFooter(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            mode = if (showDojoBrand) {
                DojoBrandFooterModes.DOJO_BRAND_ONLY
            } else {
                DojoBrandFooterModes.NONE
            },
        )
        DojoSpacer(height = 16.dp)
    }

    if (state.shouldNavigateToPreviousScreen) {
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                sheetState.hide()
                onTryAgainClicked()
            }
        }
    }
}
