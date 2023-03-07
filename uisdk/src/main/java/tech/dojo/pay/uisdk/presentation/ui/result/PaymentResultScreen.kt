package tech.dojo.pay.uisdk.presentation.ui.result

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.components.WindowSize
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.bold
import tech.dojo.pay.uisdk.presentation.components.theme.medium
import tech.dojo.pay.uisdk.presentation.ui.result.state.PaymentResultState
import tech.dojo.pay.uisdk.presentation.ui.result.viewmodel.PaymentResultViewModel
@Suppress("LongMethod")
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
internal fun ShowResultSheetScreen(
    windowSize: WindowSize,
    onCloseFlowClicked: () -> Unit,
    onTryAgainClicked: () -> Unit,
    viewModel: PaymentResultViewModel,
    showDojoBrand: Boolean
) {
    val paymentResultSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = { false }
        )
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.state.observeAsState().value ?: return

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) { keyboardController?.hide() }

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
                showDojoBrand
            )
        }
    ) {
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                paymentResultSheetState.animateTo(ModalBottomSheetValue.Expanded, tween(800))
            }
        }
    }
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
    showDojoBrand: Boolean
) {
    DojoAppBar(
        modifier = Modifier.height(60.dp),
        title = stringResource(id = state.appBarTitleId),
        titleGravity = TitleGravity.LEFT,
        titleColor = DojoTheme.colors.headerTintColor,
        actionIcon = AppBarIcon.close(tintColor = DojoTheme.colors.headerButtonTintColor) {
            coroutineScope.launch {
                sheetState.hide()
            }
            onCloseFlowClicker()
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(fraction = if (windowSize.widthWindowType == WindowSize.WindowType.COMPACT) 1f else .6f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                is PaymentResultState.SuccessfulResult -> SuccessfulResult(
                    state,
                    coroutineScope,
                    sheetState,
                    onCloseFlowClicker,
                    showDojoBrand
                )
                is PaymentResultState.FailedResult -> HandleFailedResult(
                    state,
                    coroutineScope,
                    sheetState,
                    onCloseFlowClicker,
                    onTryAgainClicked,
                    viewModel,
                    showDojoBrand
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HandleFailedResult(
    state: PaymentResultState.FailedResult,
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    onCloseFlowClicker: () -> Unit,
    onTryAgainClicked: () -> Unit,
    viewModel: PaymentResultViewModel,
    showDojoBrand: Boolean
) {
    when (state.showTryAgain) {
        true -> {
            FailedResult(
                state,
                coroutineScope,
                sheetState,
                onCloseFlowClicker,
                onTryAgainClicked,
                viewModel,
                showDojoBrand

            )
        }
        else -> {
            FailedResultWithOutTryAgain(
                state,
                coroutineScope,
                sheetState,
                onCloseFlowClicker,
                showDojoBrand
            )
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
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val (img, status, orderInfo, description, doneBtn, footer) = createRefs()

        Image(
            painter = painterResource(id = state.imageId),
            contentDescription = "",
            alignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .constrainAs(img) {
                    top.linkTo(parent.top, 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = stringResource(id = state.status),
            style = DojoTheme.typography.h5.bold,
            color = DojoTheme.colors.primaryLabelTextColor,
            modifier = Modifier.constrainAs(status) {
                top.linkTo(img.bottom, 24.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Text(
            text = stringResource(id = R.string.dojo_ui_sdk_payment_result_order_info) + ": " + state.orderInfo,
            style = DojoTheme.typography.subtitle1.medium,
            color = DojoTheme.colors.primaryLabelTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(orderInfo) {
                top.linkTo(status.bottom, 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        Text(
            text = stringResource(id = R.string.dojo_ui_sdk_payment_result_successful_description) + ": " + state.description,
            style = DojoTheme.typography.subtitle1,
            color = DojoTheme.colors.secondaryLabelTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(description) {
                top.linkTo(orderInfo.bottom, 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        DojoFullGroundButton(
            modifier = Modifier.constrainAs(doneBtn) {
                start.linkTo(parent.start, 8.dp)
                end.linkTo(parent.end, 8.dp)
                top.linkTo(description.bottom, 40.dp)
                width = Dimension.fillToConstraints
            },
            text = stringResource(id = R.string.dojo_ui_sdk_payment_result_button_done),
            backgroundColor = DojoTheme.colors.primaryCTAButtonActiveBackgroundColor
        ) {
            coroutineScope.launch {
                sheetState.hide()
            }
            onCloseFlowClicker()
        }

        if (showDojoBrand) {
            DojoBrandFooter(
                modifier = Modifier.constrainAs(footer) {
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    top.linkTo(doneBtn.bottom, 8.dp)
                    bottom.linkTo(parent.bottom, 32.dp)
                    width = Dimension.fillToConstraints
                },
                mode = DojoBrandFooterModes.DOJO_BRAND_ONLY
            )
        } else {
            DojoBrandFooter(
                modifier = Modifier.constrainAs(footer) {
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    top.linkTo(doneBtn.bottom, 8.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                    width = Dimension.fillToConstraints
                },
                mode = DojoBrandFooterModes.NONE
            )
        }
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
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val (img, status, details, orderInfo, tryAgainBtn, doneBtn, footer) = createRefs()

        Image(
            painter = painterResource(id = state.imageId),
            contentDescription = "",
            alignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .constrainAs(img) {
                    top.linkTo(parent.top, 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = stringResource(id = state.status),
            style = DojoTheme.typography.h5.bold,
            textAlign = TextAlign.Center,
            color = DojoTheme.colors.primaryLabelTextColor,
            modifier = Modifier.constrainAs(status) {
                top.linkTo(img.bottom, 24.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        Text(
            text = stringResource(id = R.string.dojo_ui_sdk_payment_result_order_info) + ": " + state.orderInfo,
            style = DojoTheme.typography.subtitle1.medium,
            textAlign = TextAlign.Center,
            color = DojoTheme.colors.primaryLabelTextColor,
            modifier = Modifier.constrainAs(orderInfo) {
                top.linkTo(status.bottom, 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        Text(
            text = stringResource(id = state.details),
            style = DojoTheme.typography.subtitle1,
            color = DojoTheme.colors.secondaryLabelTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(details) {
                top.linkTo(orderInfo.bottom, 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        DojoFullGroundButton(
            modifier = Modifier.constrainAs(tryAgainBtn) {
                start.linkTo(parent.start, 8.dp)
                end.linkTo(parent.end, 8.dp)
                top.linkTo(details.bottom, 40.dp)
                width = Dimension.fillToConstraints
            },
            text = stringResource(id = R.string.dojo_ui_sdk_payment_result_button_try_again),
            isLoading = state.isTryAgainLoading,
            backgroundColor = DojoTheme.colors.primaryCTAButtonActiveBackgroundColor
        ) {
            if (!state.isTryAgainLoading) {
                viewModel.onTryAgainClicked()
            }
        }
        DojoOutlinedButton(
            modifier = Modifier.constrainAs(doneBtn) {
                start.linkTo(parent.start, 8.dp)
                end.linkTo(parent.end, 8.dp)
                top.linkTo(tryAgainBtn.bottom, 16.dp)
                width = Dimension.fillToConstraints
            },
            text = stringResource(id = R.string.dojo_ui_sdk_payment_result_button_done),
            borderStrokeColor = DojoTheme.colors.primaryCTAButtonActiveBackgroundColor
        ) {
            coroutineScope.launch {
                sheetState.hide()
            }
            onCloseFlowClicker()
        }

        if (showDojoBrand) {
            DojoBrandFooter(
                modifier = Modifier.constrainAs(footer) {
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    top.linkTo(doneBtn.bottom, 8.dp)
                    bottom.linkTo(parent.bottom, 32.dp)
                    width = Dimension.fillToConstraints
                },
                mode = DojoBrandFooterModes.DOJO_BRAND_ONLY
            )
        } else {
            DojoBrandFooter(
                modifier = Modifier.constrainAs(footer) {
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    top.linkTo(doneBtn.bottom, 8.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                    width = Dimension.fillToConstraints
                },
                mode = DojoBrandFooterModes.NONE
            )
        }
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FailedResultWithOutTryAgain(
    state: PaymentResultState.FailedResult,
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    onCloseFlowClicker: () -> Unit,
    showDojoBrand: Boolean
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val (img, status, details, doneBtn, footer) = createRefs()

        Image(
            painter = painterResource(id = state.imageId),
            contentDescription = "",
            alignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .constrainAs(img) {
                    top.linkTo(parent.top, 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = stringResource(id = state.status),
            style = DojoTheme.typography.h5.bold,
            color = DojoTheme.colors.primaryLabelTextColor,
            modifier = Modifier.constrainAs(status) {
                top.linkTo(img.bottom, 24.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Text(
            text = stringResource(id = state.details),
            style = DojoTheme.typography.subtitle1,
            color = DojoTheme.colors.secondaryLabelTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(details) {
                top.linkTo(status.bottom, 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        DojoFullGroundButton(
            modifier = Modifier.constrainAs(doneBtn) {
                start.linkTo(parent.start, 8.dp)
                end.linkTo(parent.end, 8.dp)
                top.linkTo(details.bottom, 40.dp)
                width = Dimension.fillToConstraints
            },
            text = stringResource(id = R.string.dojo_ui_sdk_payment_result_button_done)
        ) {
            coroutineScope.launch {
                sheetState.hide()
            }
            onCloseFlowClicker()
        }
        if (showDojoBrand) {
            DojoBrandFooter(
                modifier = Modifier.constrainAs(footer) {
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    top.linkTo(doneBtn.bottom, 8.dp)
                    bottom.linkTo(parent.bottom, 32.dp)
                    width = Dimension.fillToConstraints
                },
                mode = DojoBrandFooterModes.DOJO_BRAND_ONLY
            )
        } else {
            DojoBrandFooter(
                modifier = Modifier.constrainAs(footer) {
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    top.linkTo(doneBtn.bottom, 8.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                    width = Dimension.fillToConstraints
                },
                mode = DojoBrandFooterModes.NONE
            )
        }
    }
}
