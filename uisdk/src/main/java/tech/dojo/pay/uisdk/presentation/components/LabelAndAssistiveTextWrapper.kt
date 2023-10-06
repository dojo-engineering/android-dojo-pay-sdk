package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.DojoSDKDropInUI
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme

@Composable
internal fun LabelAndAssistiveTextWrapper(
    modifier: Modifier = Modifier,
    label: AnnotatedString? = null,
    assistiveText: AnnotatedString? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier
        .then(Modifier.testTag(
            DojoSDKDropInUI.dojoThemeSettings?.analyticsExcludedFieldsIdentifier?: ""))
    ) {
        if (!label.isNullOrEmpty()) {
            Label(text = label, enabled = enabled)
            DojoSpacer(height = 6.dp)
        }

        content()

        if (!assistiveText.isNullOrEmpty()) {
            DojoSpacer(height = 6.dp)
            AssistiveText(
                text = assistiveText,
                enabled = enabled,
                isError = isError,
            )
        }
    }
}

@Composable
internal fun Label(
    text: AnnotatedString,
    enabled: Boolean,
) {
    Text(
        text = text,
        style = DojoTheme.typography.subtitle1,
        color = DojoTheme.colors.primaryLabelTextColor.copy(
            alpha = if (enabled) ContentAlpha.high else ContentAlpha.disabled,
        ),
    )
}

@Composable
internal fun AssistiveText(
    text: AnnotatedString,
    enabled: Boolean,
    isError: Boolean,
) {
    Row() {
        if (isError) {
            Icon(
                modifier = Modifier.padding(end = 8.dp),
                painter = painterResource(id = R.drawable.ic_error_18),
                tint = DojoTheme.colors.errorTextColor,
                contentDescription = null,
            )
        }
        Text(
            text = text,
            style = DojoTheme.typography.subtitle2,
            color = when {
                isError -> DojoTheme.colors.errorTextColor
                else -> LocalContentColor.current.copy(
                    alpha = if (enabled) ContentAlpha.medium else ContentAlpha.disabled,
                )
            },
        )
    }
}

@Preview("LabelAndAssistiveTextWrapper", group = "Input")
@Composable
fun AssistiveTextPreview() {
    DojoPreview {
        LabelAndAssistiveTextWrapper(
            label = AnnotatedString("Label"),
            assistiveText = AnnotatedString("Assistive text"),
            isError = true,
        ) {
            Text(
                modifier = Modifier.background(Color.Magenta),
                text = "Lorem ipsum dolor sit amet",
            )
        }
    }
}
