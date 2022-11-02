package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme

@Composable
fun CheckBoxItem(
    modifier: Modifier = Modifier,
    itemText: String,
    onCheckedChange: (Boolean) -> (Unit),
) {
    val checkedState = remember { mutableStateOf(true) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState.value,
            onCheckedChange ={
                checkedState.value = it
                onCheckedChange(it)
            }
        )
        DojoSpacer(width = 8.dp)
        Text(
            text = itemText,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = DojoTheme.typography.subtitle1,
        )
    }
}

@Preview("CheckBoxItem", group = "CheckBoxItem")
@Composable
internal fun PreviewCheckBoxItem() = DojoPreview {
    CheckBoxItem(
        itemText = "Save card for future use",
        onCheckedChange = {},
    )
}
