package tech.dojo.pay.uisdk.presentation.components

import android.annotation.SuppressLint
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
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntity

@Composable
internal fun WalletItemWithRadioButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: ((PaymentMethodItemViewEntity.WalletItem) -> Unit),
) {
    Row(
        modifier = modifier.wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DojoSpacer(width = 16.dp)
        Icon(
            painter = painterResource(id = R.drawable.ic_google_pay_card),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(top = 2.dp)
        )

        DojoSpacer(width = 32.dp)
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.dojo_ui_sdk_google_pay_string),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = DojoTheme.typography.body1,
        )

        RadioButton(
            modifier = Modifier.padding(horizontal = 16.dp),
            selected = isSelected,
            onClick = { onClick.invoke(PaymentMethodItemViewEntity.WalletItem) } ,
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
fun PreviewWalletItemWithRadioButton() {
    DojoPreview {
        WalletItemWithRadioButton(onClick = { }, isSelected = true)
    }
}
