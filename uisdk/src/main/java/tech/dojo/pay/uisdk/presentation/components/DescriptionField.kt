package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme

@Composable
fun DescriptionField(
    value: String,
    maxCharacters: Int,
    modifier: Modifier = Modifier,
    label: AnnotatedString? = null,
    enabled: Boolean = true,
    placeholder: String? = null,
    textFieldHeight: Dp = 100.dp,
    focusRequester: FocusRequester? = null,
    onDescriptionChanged: (String) -> Unit
) {
    val textFieldModifier = Modifier
        .height(textFieldHeight)
        .fillMaxWidth()

    if (focusRequester != null) {
        textFieldModifier.focusRequester(focusRequester)
    }

    Column(
        modifier = modifier
    ) {
        if (label != null) {
            Label(text = label, enabled = enabled)
            DojoSpacer(height = 6.dp)
        }

        OutlinedTextField(
            placeholder = {
                if (!placeholder.isNullOrBlank()) {
                    Text(placeholder)
                }
            },
            value = value,
            onValueChange = {
                if (value != it && it.length <= maxCharacters) onDescriptionChanged(it)
            },
            textStyle = DojoTheme.typography.subtitle1,
            singleLine = false,
            modifier = textFieldModifier,
            enabled = enabled,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent
            )
        )

        Text(
            text = "${value.length} / $maxCharacters",
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview("DescriptionField", group = "Input")
@Composable
fun DescriptionFieldPreview() {
    DojoPreview {
        DescriptionField(
            label = AnnotatedString("Label"),
            value = "Value",
            maxCharacters = 100,
            placeholder = "Test Placeholder",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
        ) {}
    }
}
