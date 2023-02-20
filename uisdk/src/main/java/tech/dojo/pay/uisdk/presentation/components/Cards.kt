package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme

@Composable
internal fun DojoExpandableCard(
    header: String,
    isExpanded: Boolean = true,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(isExpanded) }
    val rotateState = animateFloatAsState(
        targetValue = if (expanded) 180F else 0F
    )
    val interactionSource = remember { MutableInteractionSource() }
    val colors = TextFieldDefaults.outlinedTextFieldColors(
        textColor = DojoTheme.colors.primaryLabelTextColor,
        cursorColor = DojoTheme.colors.primaryLabelTextColor,
        unfocusedBorderColor = DojoTheme.colors.inputFieldDefaultBorderColor,
        backgroundColor = DojoTheme.colors.inputFieldBackgroundColor,
        focusedBorderColor = DojoTheme.colors.inputFieldSelectedBorderColor,
        placeholderColor = DojoTheme.colors.inputFieldPlaceholderColor,
        errorBorderColor = DojoTheme.colors.errorTextColor,
        errorCursorColor = DojoTheme.colors.errorTextColor,
        errorLabelColor = DojoTheme.colors.errorTextColor,
        errorLeadingIconColor = DojoTheme.colors.errorTextColor,
        errorTrailingIconColor = DojoTheme.colors.errorTextColor
    )
    Column(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = colors.indicatorColor(true, false, interactionSource).value,
                shape = DojoTheme.shapes.small
            ).background(DojoTheme.colors.inputFieldBackgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = header,
                style = DojoTheme.typography.subtitle1.copy(
                    color = colors.textColor(true).value
                ),
                color = DojoTheme.colors.secondaryLabelTextColor.copy(alpha = ContentAlpha.high)
            )
            Icon(
                painter = painterResource(R.drawable.ic_expand_more_24px),
                contentDescription = null,
                tint = DojoTheme.colors.primaryLabelTextColor,
                modifier = Modifier
                    .rotate(rotateState.value)
            )
        }
        AnimatedVisibility(
            visible = expanded
        ) {
            content.invoke(this)
        }
    }
}

@Preview("Expandable card", group = "Cards")
@Composable
fun DojoExpandableHeaderContentPreview() {
    DojoPreview {
        DojoExpandableCard(
            header = "Expandable Header"
        ) {
            LazyColumn {
                val items = listOf("1", "2")
                items(items) { item -> Text(text = item) }
            }
        }
    }
}
