package tech.dojo.pay.uisdk.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme

@Composable
fun WalletItemWithRadioButton(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: (() -> Unit)?,
) {
    Row(
        modifier = modifier,
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
        )

        RadioButton(
            modifier = Modifier.padding(16.dp),
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF00857D),
                unselectedColor = Color(0xFF262626),
                disabledColor = Color.LightGray
            )
        )
    }
}

@SuppressLint("WalletItemWithRadioButton")
@Preview("WalletItemWithRadioButton", group = "WalletItemWithRadioButton")
@Composable
fun WalletItemWithRadioButton() {
    val radioState = remember { mutableStateOf(true) }

    DojoPreview {
        WalletItemWithRadioButton(
            selected = radioState.value,
            onClick = { radioState.value = !radioState.value }
        )
    }
}
