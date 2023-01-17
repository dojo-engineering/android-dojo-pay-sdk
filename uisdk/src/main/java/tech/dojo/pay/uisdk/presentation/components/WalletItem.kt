package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.DojoSDKDropInUI
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.entities.color
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme

@Composable
internal fun WalletItem(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val forceLightMode = DojoSDKDropInUI.dojoThemeSettings?.forceLightMode ?: false

    val currentThemColor =
        if (isSystemInDarkTheme() && !forceLightMode) {
            DARK_COLOR_HEXA.color
        } else {
            Light_COLOR_HEXA.color
        }
    Row(
        modifier = modifier
            .clickable(onClick = onClick ?: {})
            .heightIn(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DojoSpacer(width = 16.dp)
        Icon(
            painter = painterResource(id = R.drawable.ic_google_pay_card),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(top = 2.dp)
                .width(25.dp)
                .heightIn(15.dp)
        )

        DojoSpacer(width = 8.dp)
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.dojo_ui_sdk_google_pay_string),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = DojoTheme.typography.body1,
            color = currentThemColor
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            modifier = Modifier.padding(8.dp),
            tint = currentThemColor,
            contentDescription = null
        )
    }
}

@Preview("WalletItem", group = "WalletItem")
@Composable
internal fun PreviewWalletItem() = DojoPreview {
    WalletItem()
}
