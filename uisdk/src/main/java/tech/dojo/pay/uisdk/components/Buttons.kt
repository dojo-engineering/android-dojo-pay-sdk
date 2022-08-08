package tech.dojo.pay.uisdk.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.components.theme.DojoTheme

private val DojoButtonHeight = 44.dp

@Composable
private fun DojoButton(
    text: String,
    backgroundColor: Color,
    contentColor: Color,
    borderStroke: BorderStroke?,
    modifier: Modifier,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.height(DojoButtonHeight),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            disabledBackgroundColor = DojoTheme.colors.onBackground.copy(alpha = 0.1f)
        ),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
        shape = RoundedCornerShape(50),
        border = borderStroke,
        enabled = enabled
    ) {
        Text(
            text = text,
            style = DojoTheme.typography.subtitle1,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}


@Composable
internal fun DojoFullGroundButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = DojoTheme.colors.secondarySurface,
    contentColor: Color = DojoTheme.colors.onPrimary,
    onClick: () -> Unit
) {
    DojoButton(
        text = text,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        borderStroke = null,
        modifier = modifier,
        enabled = enabled,
        onClick = onClick
    )
}

@Composable
internal fun DojoOutlinedButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = DojoTheme.colors.background,
    contentColor: Color = DojoTheme.colors.secondarySurface,
    borderStroke: BorderStroke? = if (enabled) BorderStroke(
        1.dp,
        DojoTheme.colors.secondarySurface
    ) else null,
    onClick: () -> Unit
) {
    DojoButton(
        text = text,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        borderStroke = borderStroke,
        modifier = modifier,
        enabled = enabled,
        onClick = onClick
    )
}

@Preview("Button", group = "Buttons")
@Composable
fun PreviewButton() {
    DojoPreview() {
        DojoFullGroundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            text = "Button"
        ) {}
    }
}

@Preview("Outlined button", group = "Buttons")
@Composable
fun PreviewOtlinedButton() {
    DojoPreview() {
        DojoOutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            text = "Button"
        ) {}
    }
}