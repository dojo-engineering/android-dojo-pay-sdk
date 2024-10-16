package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import tech.dojo.pay.uisdk.presentation.components.theme.bold

@Composable
internal fun TotalDueItem(
    modifier: Modifier = Modifier,
    amount: String
) {
    Row(
        modifier = modifier.heightIn(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DojoSpacer(width = 16.dp)
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.dojo_ui_sdk_payment_method_total_due_title),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = DojoTheme.typography.body1.bold,
            color = DojoTheme.colors.primaryLabelTextColor
        )

        Text(
            text = amount,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(16.dp),
            style = DojoTheme.typography.body1.bold,
            color = DojoTheme.colors.primaryLabelTextColor
        )
    }
}

@Preview("TotalDueItem", group = "TotalDueItem")
@Composable
internal fun PreviewTotalDueItem() = DojoPreview {
    TotalDueItem(amount = "£89.55")
}
