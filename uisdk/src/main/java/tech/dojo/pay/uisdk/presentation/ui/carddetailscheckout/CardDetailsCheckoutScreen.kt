package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.DojoFullGroundButton
import tech.dojo.pay.uisdk.presentation.components.TitleGravity

@Composable
fun CardDetailsCheckoutScreen(
    onCloseClicked:  () -> Unit,
    onBackClicked: () -> Unit,
    onPayButtonClicked: () -> Unit
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
        ) { onPayButtonClicked() }
    }
}
