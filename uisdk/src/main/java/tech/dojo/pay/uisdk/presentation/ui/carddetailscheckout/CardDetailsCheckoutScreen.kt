package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import tech.dojo.pay.uisdk.presentation.components.AmountBanner
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.DojoBrandFooter
import tech.dojo.pay.uisdk.presentation.components.DojoFullGroundButton
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.state.CardDetailsCheckoutState
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel.CardDetailsCheckoutViewModel

@Composable
fun CardDetailsCheckoutScreen(
    viewModel: CardDetailsCheckoutViewModel,
    onCloseClicked: () -> Unit,
    onBackClicked: () -> Unit,
) {
    val state = viewModel.state.observeAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            val (appBar, banner, payBtn, footer) = createRefs()

            DojoAppBar(
                modifier = Modifier.constrainAs(appBar) {
                    start.linkTo(parent.start, 0.dp)
                    end.linkTo(parent.end, 0.dp)
                    top.linkTo(parent.top, 0.dp)
                    width = Dimension.fillToConstraints
                },
                title = stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_title),
                titleGravity = TitleGravity.LEFT,
                navigationIcon = AppBarIcon.back { onBackClicked() },
                actionIcon = AppBarIcon.close { onCloseClicked() }
            )
            AmountBanner(
                modifier = Modifier.constrainAs(banner) {
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    top.linkTo(appBar.bottom, 8.dp)
                    width = Dimension.fillToConstraints
                },
                amount = state.value?.totalAmount ?: "",
                currencyLogo = state.value?.amountCurrency ?: ""
            )

            DojoFullGroundButton(
                modifier = Modifier.constrainAs(payBtn) {
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    bottom.linkTo(footer.bottom, 46.dp)
                    width = Dimension.fillToConstraints
                },
                text = stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_button_pay),
                isLoading = state.value?.isLoading ?: false
            ) {
                if (state.value?.isLoading != true) {
                    viewModel.onPayWithCardClicked()
                }
            }

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
