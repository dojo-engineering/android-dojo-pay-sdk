package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.R


@Composable
internal fun SupportedPaymentMethods(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Icon(
            painter = painterResource(id = R.drawable.ic_visa),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(top = 2.dp)
                .width(25.dp)
                .heightIn(15.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_mastercard),
            contentDescription = "",
            tint = Color.Unspecified
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_amex),
            contentDescription = "",
            tint = Color.Unspecified
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_maestro),
            contentDescription = "",
            tint = Color.Unspecified
        )
    }
}