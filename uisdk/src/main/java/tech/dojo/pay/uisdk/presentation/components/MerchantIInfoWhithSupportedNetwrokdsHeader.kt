package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.medium

@Composable
internal fun MerchantIInfoWithSupportedNetworksHeader(
    modifier: Modifier = Modifier,
    merchantName: String,
    orderId: String,
    allowedPaymentMethodsIcons: List<Int>,
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
                style = DojoTheme.typography.h3.medium,
                color = DojoTheme.colors.primaryLabelTextColor.copy(alpha = ContentAlpha.high),
            )
            Text(
                text = orderId,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = DojoTheme.typography.body1,
                color = DojoTheme.colors.primaryLabelTextColor.copy(alpha = ContentAlpha.high),
            )
            SupportedPaymentMethods(Modifier.padding(top = 16.dp), allowedPaymentMethodsIcons)
        }
    }
}

@Preview("MerchantIInfoWithSupportedNetworksHeader", group = "MerchantIInfoWithSupportedNetworksHeader")
@Composable
internal fun PreviewMerchantIInfoWithSupportedNetworksHeader() = DojoPreview {
    MerchantIInfoWithSupportedNetworksHeader(merchantName = "El pastor", orderId = "12312312", allowedPaymentMethodsIcons = listOf(R.drawable.ic_amex))
}
