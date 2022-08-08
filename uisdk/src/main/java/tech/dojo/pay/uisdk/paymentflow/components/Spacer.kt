package tech.dojo.pay.uisdk.paymentflow.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun DojoSpacer(
    width: Dp = 0.dp,
    height: Dp = 0.dp
) {
    Spacer(modifier = Modifier.size(width, height))
}
