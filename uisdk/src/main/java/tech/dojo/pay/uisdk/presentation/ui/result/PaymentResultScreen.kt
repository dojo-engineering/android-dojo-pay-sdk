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
import androidx.compose.material.LocalContentColor
import androidx.compose.material.ContentAlpha
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
import tech.dojo.pay.uisdk.entities.DojoThemeSettings
import tech.dojo.pay.uisdk.presentation.components.*
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.DojoBottomSheet
import tech.dojo.pay.uisdk.presentation.components.DojoFullGroundButton
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.bold
import tech.dojo.pay.uisdk.presentation.components.theme.medium
import tech.dojo.pay.uisdk.presentation.ui.result.state.PaymentResultState
import tech.dojo.pay.uisdk.presentation.ui.result.viewmodel.PaymentResultViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowResultSheetScreen(
    dojoThemeSettings: DojoThemeSettings?,
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
                dojoThemeSettings,
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
    dojoThemeSettings: DojoThemeSettings?,
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
        titleColor = dojoThemeSettings?.headerTintColor,
        actionIcon = AppBarIcon.close(tintColor = dojoThemeSettings?.headerTintColor) {
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
            onCloseFlowClicker,
            dojoThemeSettings
        )
        is PaymentResultState.FailedResult -> FailedResult(
            state,
            coroutineScope,
            sheetState,
            onCloseFlowClicker,
            onTryAgainClicked,
            dojoThemeSettings
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SuccessfulResult(
    state: PaymentResultState.SuccessfulResult,
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    onCloseFlowClicker: () -> Unit,
    dojoThemeSettings: DojoThemeSettings?
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
            color = dojoThemeSettings?.primaryLabelTextColor ?: LocalContentColor.current.copy(alpha = ContentAlpha.high),
            modifier = Modifier.constrainAs(status) {
                top.linkTo(img.bottom, 24.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Text(
            text = state.orderInfo,
            style = DojoTheme.typography.subtitle1.medium,
            color = dojoThemeSettings?.primaryLabelTextColor ?: LocalContentColor.current.copy(alpha = ContentAlpha.high),
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
            color = dojoThemeSettings?.secondaryLabelTextColor ?: LocalContentColor.current.copy(alpha = ContentAlpha.high),
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
            text = stringResource(id = R.string.dojo_payment_result_text_done),
            backgroundColor= dojoThemeSettings?.primaryCTAButtonActiveBackgroundColor
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
            })
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
    dojoThemeSettings: DojoThemeSettings?
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
            color = dojoThemeSettings?.primaryLabelTextColor ?: LocalContentColor.current.copy(alpha = ContentAlpha.high),
            modifier = Modifier.constrainAs(status) {
                top.linkTo(img.bottom, 24.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Text(
            text = state.details,
            style = DojoTheme.typography.subtitle1.medium,
            color = dojoThemeSettings?.primaryLabelTextColor ?: LocalContentColor.current.copy(alpha = ContentAlpha.high),
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
            text = stringResource(id = R.string.dojo_payment_result_text_try_again),
            backgroundColor= dojoThemeSettings?.primaryCTAButtonActiveBackgroundColor
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
            text = stringResource(id = R.string.dojo_payment_result_text_done),
            backgroundColor= dojoThemeSettings?.primaryCTAButtonActiveBackgroundColor
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
            })
    }
}
