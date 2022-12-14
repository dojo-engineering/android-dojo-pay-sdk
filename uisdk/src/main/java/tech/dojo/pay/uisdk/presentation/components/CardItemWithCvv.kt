package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem

@Composable
internal fun CardItemWithCvv(
    onCvvValueChanged: (String) -> Unit,
    cvvValue: String,
    modifier: Modifier = Modifier,
    cardItem: PaymentMethodItemViewEntityItem.CardItemItem,
    onClick: (() -> Unit)? = null,
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick ?: {})
            .heightIn(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DojoSpacer(width = 16.dp)
        Icon(
            painter = painterResource(id = cardItem.icon),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(top = 2.dp)
                .width(30.dp)
                .heightIn(20.dp)
        )

        DojoSpacer(width = 16.dp)
        Column(
            modifier = Modifier
                .wrapContentHeight()
        ) {
            Text(
                text = cardItem.scheme,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = DojoTheme.typography.body2,
            )
            DojoSpacer(height = 8.dp)
            Text(
                text = cardItem.pan,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = DojoTheme.typography.subtitle1,
            )
        }
        DojoSpacer(width = 16.dp)

        Box(
            modifier = Modifier
                .padding(16.dp)
                .width(85.dp)
        ) {
            CvvInputField(
                cvvValue = cvvValue,
                cvvPlaceholder = stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_cvv),
                onCvvValueChanged = onCvvValueChanged,
                keyboardActions = keyboardActions
            )
        }

        Spacer(modifier = Modifier.weight(1F))
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            modifier = Modifier.padding(8.dp),
            contentDescription = null
        )
    }
}

@Preview("CardItemWithCvv", group = "CardItemWithCvv")
@Composable
internal fun PreviewCardItemWithCvv() = DojoPreview {
    var value by remember { mutableStateOf("") }
    val cvvValueState by remember {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }
    CardItemWithCvv(
        cvvValue = cvvValueState.text,
        onCvvValueChanged = { value = it },
        cardItem = PaymentMethodItemViewEntityItem.CardItemItem(
            id = "",
            icon = R.drawable.ic_visa,
            scheme = "Visa",
            pan = "****9560"
        ),
        onClick = {}
    )
}
