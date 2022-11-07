package tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.*
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.DojoBrandFooter
import tech.dojo.pay.uisdk.presentation.components.DojoFullGroundButton
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
        val state = viewModel.state.observeAsState().value  ?: return@Surface
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (appBar,walletItem, selectPaymentMethod, payWithNewCard, footer) = createRefs()

            DojoAppBar(
                modifier = Modifier.constrainAs(appBar) {
                    start.linkTo(parent.start, 0.dp)
                    end.linkTo(parent.end, 0.dp)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                },
                title = stringResource(id = R.string.dojo_ui_sdk_manage_payment_methods_title),
                titleGravity = TitleGravity.LEFT,
                navigationIcon = AppBarIcon.back(DojoTheme.colors.headerButtonTintColor) { onBackClicked() },
                actionIcon = if(state.appBarIconType== AppBarIconType.CLOSE){
                    AppBarIcon.close(DojoTheme.colors.headerButtonTintColor) { onCloseClicked() }
                }else{
                    AppBarIcon.delete (DojoTheme.colors.headerButtonTintColor) { viewModel.onDeleteClicked() }

                }
            )


            PaymentMethodsList(
                modifier = Modifier.constrainAs(walletItem) {
                    start.linkTo(parent.start, 0.dp)
                    end.linkTo(parent.end, 0.dp)
                    top.linkTo(appBar.bottom)
                    width = Dimension.fillToConstraints
                },
                paymentMethodItems = state.paymentMethodItems.items,
                onItemChecked = { viewModel.onPaymentMethodChanged(it) },
                onItemLongClicked = {viewModel.onPaymentMethodLongCLick(it)}
            )

            DojoOutlinedButton(
                modifier = Modifier.constrainAs(selectPaymentMethod) {
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    bottom.linkTo(payWithNewCard.bottom, 56.dp)
                    width = Dimension.fillToConstraints
                },
                text = stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_title)
            ) { onNewCardButtonClicked() }
            DojoFullGroundButton(
                modifier = Modifier.constrainAs(payWithNewCard) {
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    bottom.linkTo(footer.bottom, 46.dp)
                    width = Dimension.fillToConstraints
                },
                text = stringResource(id = R.string.dojo_ui_sdk_pay_with_this_method)
            ) { onBackClicked() }

            DojoBrandFooter(
                modifier = Modifier.constrainAs(footer) {
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    bottom.linkTo(parent.bottom, 24.dp)
                    width = Dimension.fillToConstraints
                },
                withTermsAndPrivacy = true
            )
        }
    }
}
