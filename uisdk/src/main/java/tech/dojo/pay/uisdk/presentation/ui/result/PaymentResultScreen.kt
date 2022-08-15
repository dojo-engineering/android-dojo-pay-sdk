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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.DojoBottomSheet
import tech.dojo.pay.uisdk.presentation.components.DojoFullGroundButton
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.ui.result.state.PaymentResultState
import tech.dojo.pay.uisdk.presentation.ui.result.viewmodel.PaymentResultViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowResultSheetScreen(
    onCloseFlowClicker: () -> Unit,
    viewModel: PaymentResultViewModel
) {
    val paymentResultsheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Expanded,
            confirmStateChange = { false }
        )
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.state.observeAsState().value ?: return
    DojoBottomSheet(
        modifier = Modifier.fillMaxSize(),
        sheetState = paymentResultsheetState,
        sheetContent = {
            BottomSheetItems(
                coroutineScope,
                paymentResultsheetState,
                state,
                onCloseFlowClicker
            )
        }
    ) {}
}

@ExperimentalMaterialApi
@Composable
private fun BottomSheetItems(
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    state: PaymentResultState,
    onCloseFlowClicker: () -> Unit
) {
    DojoAppBar(
        modifier = Modifier.height(60.dp),
        title = "Payment method",
        titleGravity = TitleGravity.LEFT,
        actionIcon = AppBarIcon.close() {
            coroutineScope.launch {
                sheetState.hide()
            }
            onCloseFlowClicker()
        }
    )

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val (img, status, orderInfo, description, doneBtn) = createRefs()

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
            style = DojoTheme.typography.h5,
            modifier = Modifier.constrainAs(status) {
                top.linkTo(img.bottom, 24.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Text(
            text = state.orderInfo,
            style = DojoTheme.typography.subtitle1,
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
                bottom.linkTo(parent.bottom, 32.dp)
                width = Dimension.fillToConstraints
            },
            text = "Done"
        ) {
            coroutineScope.launch {
                sheetState.hide()
            }
            onCloseFlowClicker()
        }
    }
}
