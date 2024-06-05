package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
internal fun CheckBoxItem(
    modifier: Modifier = Modifier,
    itemText: String,
    isChecked: Boolean = true,
    onCheckedChange: (Boolean) -> Unit,
) {
    val checkedState = remember { mutableStateOf(isChecked) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                checkedState.value = !checkedState.value
                onCheckedChange(checkedState.value)
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(25.dp)
                .border(
                    width = 1.dp,
                    color = if (checkedState.value) DojoTheme.colors.inputElementActiveTintColor else DojoTheme.colors.headerButtonTintColor,
                    shape = DojoTheme.shapes.small,
                )
                .background(DojoTheme.colors.primarySurfaceBackgroundColor)
                .align(Alignment.Top),
        ) {
            if (checkedState.value) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "",
                    tint = DojoTheme.colors.inputElementActiveTintColor,
                )
            }
        }
        DojoSpacer(width = 12.dp)
        Box(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
                .align(Alignment.Top),
        ) {
            Text(
                text = itemText,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                maxLines = 7,
                color = DojoTheme.colors.secondaryLabelTextColor,
                style = DojoTheme.typography.subtitle1,
            )
        }
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
