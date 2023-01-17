package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
internal fun SupportedPaymentMethods(
    modifier: Modifier = Modifier,
    allowedPaymentMethodsIcons: List<Int>
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(allowedPaymentMethodsIcons) { iconId ->
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = "",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                )
            }
        }
    }
}
