package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AmountBreakDown(
    modifier: Modifier = Modifier,
    items: List<AmountBreakDownItem>,
    totalAmount: String
) {
    Column(
        modifier = modifier

    ) {
        if (items.isNotEmpty()){ AmountBreakDownItems(items = items) }
        TotalDueItem(amount = totalAmount)
    }
}

@Preview("AmountBreakDown", group = "AmountBreakDown")
@Composable
internal fun PreviewAmountBreakDown() = DojoPreview {
    AmountBreakDown(
        items = listOf(
            AmountBreakDownItem("Subtotal", "£74.63"),
            AmountBreakDownItem("VAT", "£14.93"),
        ),
        totalAmount = "£89.55"
    )
}