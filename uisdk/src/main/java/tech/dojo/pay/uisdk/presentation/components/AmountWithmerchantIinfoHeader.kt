package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.medium

@Composable
internal fun AmountWithMerchantIInfoHeader(
    modifier: Modifier = Modifier,
    amount: String,
    currencyLogo: String,
    merchantName: String
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
                text = merchantName,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = DojoTheme.typography.h6.medium,
                color = DojoTheme.colors.primaryLabelTextColor.copy(alpha = ContentAlpha.high)
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
                    color = DojoTheme.colors.primaryLabelTextColor.copy(alpha = ContentAlpha.high)
                )
                Text(
                    text = amount,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = DojoTheme.typography.h1.medium,
                    color = DojoTheme.colors.primaryLabelTextColor.copy(alpha = ContentAlpha.high)
                )
            }
        }
    }
}

@Preview("Header", group = "Header")
@Composable
internal fun PreviewHeader() = DojoPreview {
    AmountWithMerchantIInfoHeader(
        amount = "95.70",
        currencyLogo = "£",
        merchantName = "Dojo Cafe"
    )
}
