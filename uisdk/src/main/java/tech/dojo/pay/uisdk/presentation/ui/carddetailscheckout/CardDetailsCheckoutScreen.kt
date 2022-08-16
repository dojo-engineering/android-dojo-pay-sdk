package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.DojoFullGroundButton
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel.CardDetailsCheckoutViewModel

@Composable
fun CardDetailsCheckoutScreen(
    viewModel: CardDetailsCheckoutViewModel,
    onCloseClicked: () -> Unit,
    onBackClicked: () -> Unit,
) {
    androidx.compose.material.Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        DojoAppBar(
            title = "Pay With New Card",
            titleGravity = TitleGravity.LEFT,
            navigationIcon = AppBarIcon.back { onBackClicked() },
            actionIcon = AppBarIcon.close { onCloseClicked() }
        )
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            val (doneBtn) = createRefs()
            DojoFullGroundButton(
                modifier = Modifier.constrainAs(doneBtn) {
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    bottom.linkTo(parent.bottom, 32.dp)
                    width = Dimension.fillToConstraints
                },
                text = "pay"
            ) { viewModel.onPayWithCardClicked() }
        }
    }
}