package tech.dojo.pay.uisdk.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
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
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem

@Composable
internal fun WalletItemWithRadioButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    showRadioButton: Boolean = true,
    onClick: ((PaymentMethodItemViewEntityItem.WalletItemItem) -> Unit),
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .clickable(
                enabled = showRadioButton,
                onClick = { onClick.invoke(PaymentMethodItemViewEntityItem.WalletItemItem) }
            )
            .padding(start = 8.dp, end = 16.dp,top= 16.dp, bottom = 18.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DojoSpacer(width = 16.dp)
        Icon(
            painter = painterResource(id = R.drawable.ic_google_pay_card),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier.padding(top = 2.dp)
        )

        DojoSpacer(width = 16.dp)
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.dojo_ui_sdk_google_pay_string),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = DojoTheme.typography.body1,
        )
        if (showRadioButton) {
            RadioButton(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterVertically),
                selected = isSelected,
                onClick = { onClick.invoke(PaymentMethodItemViewEntityItem.WalletItemItem) },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF00857D),
                    unselectedColor = Color(0xFF262626),
                    disabledColor = Color.LightGray
                )
            )
        }
    }
}

@SuppressLint("WalletItemWithRadioButton")
@Preview("WalletItemWithRadioButton", group = "WalletItemWithRadioButton")
@Composable
fun PreviewWalletItemWithRadioButton() {
    DojoPreview {
        WalletItemWithRadioButton(onClick = { }, isSelected = true)
    }
}
