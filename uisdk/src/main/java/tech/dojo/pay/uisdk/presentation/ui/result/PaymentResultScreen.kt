package tech.dojo.pay.uisdk.presentation.ui.result

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
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
import tech.dojo.pay.uisdk.presentation.components.DojoFullGroundButton
import tech.dojo.pay.uisdk.presentation.components.DojoOutlinedButton
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.bold
import tech.dojo.pay.uisdk.presentation.components.theme.medium
import tech.dojo.pay.uisdk.presentation.ui.result.state.PaymentResultState
import tech.dojo.pay.uisdk.presentation.ui.result.viewmodel.PaymentResultViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowResultSheetScreen(
    onCloseFlowClicked: () -> Unit,
    onTryAgainClicked: () -> Unit,
    viewModel: PaymentResultViewModel
) {
    val paymentResultSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = { false }
        )
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.state.observeAsState().value ?: return
    DojoBottomSheet(
        modifier = Modifier.fillMaxSize(),
        sheetState = paymentResultSheetState,
        sheetContent = {
            BottomSheetItems(
                coroutineScope,
                paymentResultSheetState,
                state,
                onCloseFlowClicked,
                onTryAgainClicked
            )
        }
    ) {
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                paymentResultSheetState.show()
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
    onTryAgainClicked: () -> Unit
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
    when (state) {
        is PaymentResultState.SuccessfulResult -> SuccessfulResult(
            state,
            coroutineScope,
            sheetState,
            onCloseFlowClicker
        )
        is PaymentResultState.FailedResult -> HandleFaledResult(
            state,
            coroutineScope,
            sheetState,
            onCloseFlowClicker,
            onTryAgainClicked
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HandleFaledResult(
    state: PaymentResultState.FailedResult,
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    onCloseFlowClicker: () -> Unit,
    onTryAgainClicked: () -> Unit
) {
    when (state.showTryAgain) {
        true -> {
            FailedResult(
                state,
                coroutineScope,
                sheetState,
                onCloseFlowClicker,
                onTryAgainClicked
            )
        }
        else -> {
            FailedResultWithOutTryAgain(
                state,
                coroutineScope,
                sheetState,
                onCloseFlowClicker
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
            text = state.description,
            style = DojoTheme.typography.h5.bold,
            color = DojoTheme.colors.primaryLabelTextColor,
            modifier = Modifier.constrainAs(status) {
                top.linkTo(img.bottom, 24.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Text(
            text = state.orderInfo,
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
            text = state.status,
            style = DojoTheme.typography.subtitle1,
            color = DojoTheme.colors.secondaryLabelTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(description) {
                top.linkTo(orderInfo.bottom, 8.dp)
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

        DojoBrandFooter(
            modifier = Modifier.constrainAs(footer) {
                start.linkTo(parent.start, 8.dp)
                end.linkTo(parent.end, 8.dp)
                top.linkTo(doneBtn.bottom, 8.dp)
                bottom.linkTo(parent.bottom, 32.dp)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FailedResult(
    state: PaymentResultState.FailedResult,
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    onCloseFlowClicker: () -> Unit,
    onTryAgainClicked: () -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val (img, status, details, tryAgainBtn, doneBtn, footer) = createRefs()

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
            text = state.status,
            style = DojoTheme.typography.h5.bold,
            color = DojoTheme.colors.primaryLabelTextColor,
            modifier = Modifier.constrainAs(status) {
                top.linkTo(img.bottom, 24.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Text(
            text = state.details,
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
            modifier = Modifier.constrainAs(tryAgainBtn) {
                start.linkTo(parent.start, 8.dp)
                end.linkTo(parent.end, 8.dp)
                top.linkTo(details.bottom, 40.dp)
                width = Dimension.fillToConstraints
            },
            text = stringResource(id = R.string.dojo_ui_sdk_payment_result_button_try_again),
            backgroundColor = DojoTheme.colors.primaryCTAButtonActiveBackgroundColor
        ) {
            coroutineScope.launch {
                sheetState.hide()
                onTryAgainClicked()
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

        DojoBrandFooter(
            modifier = Modifier.constrainAs(footer) {
                start.linkTo(parent.start, 8.dp)
                end.linkTo(parent.end, 8.dp)
                top.linkTo(doneBtn.bottom, 8.dp)
                bottom.linkTo(parent.bottom, 32.dp)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FailedResultWithOutTryAgain(
    state: PaymentResultState.FailedResult,
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    onCloseFlowClicker: () -> Unit
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
            text = state.status,
            style = DojoTheme.typography.h5.bold,
            color = DojoTheme.colors.primaryLabelTextColor,
            modifier = Modifier.constrainAs(status) {
                top.linkTo(img.bottom, 24.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Text(
            text = state.details,
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
            text = stringResource(id = R.string.dojo_payment_result_text_done)
        ) {
            coroutineScope.launch {
                sheetState.hide()
            }
            onCloseFlowClicker()
        }

        DojoBrandFooter(
            modifier = Modifier.constrainAs(footer) {
                start.linkTo(parent.start, 8.dp)
                end.linkTo(parent.end, 8.dp)
                top.linkTo(doneBtn.bottom, 8.dp)
                bottom.linkTo(parent.bottom, 32.dp)
                width = Dimension.fillToConstraints
            }
        )
    }
}
