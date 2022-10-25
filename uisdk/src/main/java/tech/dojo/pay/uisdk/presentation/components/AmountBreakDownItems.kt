package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme

@Composable
internal fun AmountBreakDownItems(
    modifier: Modifier = Modifier,
    items: List<AmountBreakDownItem>
) {
    val heightValue: Int by remember { mutableStateOf(getItemHeight(items.size)) }
    LazyColumn(
        modifier = modifier
            .height(heightValue.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            Row(
                modifier = modifier.height(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DojoSpacer(width = 16.dp)
                Text(
                    text = item.caption,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = DojoTheme.typography.body1,
                    color = DojoTheme.colors.secondaryLabelTextColor
                )
                Spacer(Modifier.weight(4f))
                Text(
                    text = item.amount,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.padding(end = 16.dp),
                    style = DojoTheme.typography.body1,
                    color = DojoTheme.colors.secondaryLabelTextColor
                )
            }
        }
    }
}
private fun getItemHeight(listSize: Int) =
    if (listSize> 3 || listSize == 3) { 80 } else { listSize * 24 }

data class AmountBreakDownItem(
    val caption: String,
    val amount: String
)

@Preview("AmountBreakDownItems", group = "AmountBreakDownItems")
@Composable
internal fun PreviewAmountBreakDownItems() = DojoPreview {
    Column {
        AmountBreakDownItems(
            items = listOf(
                AmountBreakDownItem("Subtotal", "£74.63"),
                AmountBreakDownItem("VAT", "£14.93"),
                AmountBreakDownItem("VAT", "£14.93"),
                AmountBreakDownItem("VAT", "£14.93"),
                AmountBreakDownItem("VAT", "£14.93"),

            )
        )
        TotalDueItem(amount = "£89.55")
    }
}
