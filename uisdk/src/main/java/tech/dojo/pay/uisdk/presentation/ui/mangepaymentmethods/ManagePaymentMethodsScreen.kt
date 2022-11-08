package tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.*
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.DojoBrandFooter
import tech.dojo.pay.uisdk.presentation.components.DojoOutlinedButton
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.AppBarIconType
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.viewmodel.MangePaymentViewModel

@Composable
internal fun ManagePaymentMethods(
    viewModel: MangePaymentViewModel,
    onCloseClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onNewCardButtonClicked: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        val scrollState = rememberScrollState()

        val state = viewModel.state.observeAsState().value ?: return@Surface
        if (state.showDialog) {
            SimpleAlertDialog(
                title = stringResource(id = R.string.dojo_ui_sdk_mange_payments_dialog_title),
                text = stringResource(id = R.string.dojo_ui_sdk_mange_payments_dialog_message),
                confirmButtonText = stringResource(id = R.string.dojo_ui_sdk_mange_payments_dialog_confirm_text),
                dismissButton = stringResource(id = R.string.dojo_ui_sdk_mange_payments_dialog_cancel_text),
                onConfirmButtonClicked = { viewModel.onDeletePaymentMethodClicked() },
                onDismissButtonClicked = { viewModel.onCancelDialogClicked() }
            )
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = Color.White,
            topBar = {
                DojoAppBar(
                    title = stringResource(id = R.string.dojo_ui_sdk_manage_payment_methods_title),
                    titleGravity = TitleGravity.LEFT,
                    navigationIcon = AppBarIcon.back(DojoTheme.colors.headerButtonTintColor) { onBackClicked() },
                    actionIcon = if (state.appBarIconType == AppBarIconType.CLOSE) {
                        AppBarIcon.close(DojoTheme.colors.headerButtonTintColor) { onCloseClicked() }
                    } else {
                        AppBarIcon.delete(DojoTheme.colors.headerButtonTintColor) { viewModel.onDeleteClicked() }
                    }
                )
            },
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    Column(
                        Modifier
                            .padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 200.dp),
                        verticalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        PaymentMethodsList(
                            paymentMethodItems = state.paymentMethodItems.items,
                            currentSelectedMethod = state.currentSelectedMethod,
                            isInEditMode = state.isInEditMode,
                            onItemChecked = { viewModel.onPaymentMethodChanged(it) },
                            onItemLongClicked = { viewModel.onPaymentMethodLongCLick(it) }
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .background(Color.White),
                    ) {
                        SingleButtonView(
                            text = stringResource(id = R.string.dojo_ui_sdk_pay_with_this_method),
                            enabled = state.isUsePaymentMethodButtonEnabled
                        ) { onBackClicked() }
                        DojoOutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            text = stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_title)
                        ) { onNewCardButtonClicked() }
                        DojoBrandFooter(
                            modifier = Modifier.padding(24.dp, 0.dp, 16.dp, 16.dp),
                            withTermsAndPrivacy = false
                        )
                    }
                }
            }
        )
    }
}
