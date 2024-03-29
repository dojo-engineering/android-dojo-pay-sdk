package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.bold

@Composable
internal fun SimpleAlertDialog(
    title: String,
    text: String,
    confirmButtonText: String,
    onConfirmButtonClicked: () -> Unit,
    dismissButton: String,
    onDismissButtonClicked: () -> Unit,
    isLoading: Boolean = false
) {
    AlertDialog(
        onDismissRequest = { },
        backgroundColor = DojoTheme.colors.primarySurfaceBackgroundColor,
        confirmButton = {
            if (isLoading) {
                CircularProgressIndicator(
                    color = DojoTheme.colors.loadingIndicatorColor,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                TextButton(onClick = { onConfirmButtonClicked.invoke() }) {
                    Text(
                        text = confirmButtonText,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = DojoTheme.typography.subtitle2,
                        color = DojoTheme.colors.errorTextColor
                    )
                }
            }
        },
        dismissButton = {
            if (!isLoading) {
                TextButton(onClick = { onDismissButtonClicked.invoke() }) {
                    Text(
                        text = dismissButton,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = DojoTheme.typography.subtitle2,
                        color = DojoTheme.colors.inputElementActiveTintColor
                    )
                }
            }
        },
        title = {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = DojoTheme.typography.body1.bold,
                color = DojoTheme.colors.primaryLabelTextColor
            )
        },
        text = {
            Text(
                text = text,
                overflow = TextOverflow.Ellipsis,
                style = DojoTheme.typography.subtitle1,
                color = DojoTheme.colors.secondaryLabelTextColor
            )
        }
    )
}
