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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem

@Composable
internal fun CardItemWithRadioButton(
    modifier: Modifier = Modifier,
    cardItem: PaymentMethodItemViewEntityItem.CardItemItem,
    isSelected: Boolean,
    onClick: ((PaymentMethodItemViewEntityItem.CardItemItem) -> Unit),
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DojoSpacer(width = 16.dp)
        Icon(
            painter = painterResource(id = cardItem.icon),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(top = 2.dp)
                .width(25.dp)
                .heightIn(15.dp)
        )

        DojoSpacer(width = 32.dp)
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f)
        ) {
            Text(
                text = cardItem.scheme,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = DojoTheme.typography.caption,
            )
            DojoSpacer(height = 8.dp)
            Text(
                text = cardItem.pan,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = DojoTheme.typography.body1,
            )
        }

        RadioButton(
            modifier = Modifier.padding(horizontal = 16.dp),
            selected = isSelected,
            onClick = { onClick.invoke(cardItem) },
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF00857D),
                unselectedColor = Color(0xFF262626),
                disabledColor = Color.LightGray
            )
        )
    }
}

@SuppressLint("CardItemWithRadioButton")
@Preview("CardItemWithRadioButton", group = "CardItemWithRadioButton")
@Composable
fun PreviewCardItemWithRadioButton() {
    DojoPreview {
        CardItemWithRadioButton(
            cardItem = PaymentMethodItemViewEntityItem.CardItemItem(
                id = "",
                icon = R.drawable.ic_visa,
                scheme = "Visa",
                pan = "****9560"
            ),
            onClick = {
            },
            isSelected = true
        )
    }
}
