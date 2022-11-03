package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.medium

@Composable
internal fun AmountWithPaymentMethodsHeader(
    modifier: Modifier = Modifier,
    amount: String,
    currencyLogo: String,
    allowedPaymentMethodsIcons: List<Int>
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_you_pay),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = DojoTheme.typography.h6.medium,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.high)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ) {
                Text(
                    modifier = Modifier.padding(
                        start = 0.dp,
                        top = 12.dp,
                        bottom = 2.dp,
                        end = 8.dp
                    ),
                    text = currencyLogo,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = DojoTheme.typography.h3.medium,
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.high)
                )
                Text(
                    text = amount,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = DojoTheme.typography.h1.medium,
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.high)
                )
            }
            SupportedPaymentMethods(Modifier.padding(start = 32.dp,end= 32.dp, top=16.dp), allowedPaymentMethodsIcons)
        }
    }
}

@Preview("AmountBanner", group = "Footer")
@Composable
internal fun PreviewAmountBanner() = DojoPreview {
    AmountWithPaymentMethodsHeader(amount = "95.70", currencyLogo = "£", allowedPaymentMethodsIcons = listOf(R.drawable.ic_amex))
}
