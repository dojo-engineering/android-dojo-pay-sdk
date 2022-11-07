package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.bold

@Composable
internal fun SimpleAlertDialog(
    title: String,
    text: String,
    confirmButtonText: String,
    onConfirmButtonClicked: () -> Unit,
    dismissButton: String,
    onDismissButtonClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            TextButton(onClick = { onConfirmButtonClicked.invoke() })
            {
                Text(
                    text = confirmButtonText,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = DojoTheme.typography.caption,
                    color = DojoTheme.colors.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissButtonClicked.invoke() })
            {
                Text(
                    text = dismissButton,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = DojoTheme.typography.caption,
                    color = DojoTheme.colors.primary
                )
            }
        },
        title = {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = DojoTheme.typography.body1.bold,
            )
        },
        text = {
            Text(
                text = text,
                overflow = TextOverflow.Ellipsis,
                style = DojoTheme.typography.caption,
            )
        }
    )
}