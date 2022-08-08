package tech.dojo.pay.uisdk.paymentflow.components

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.paymentflow.components.theme.DojoTheme

@Composable
internal fun DojoPreview(
    content: @Composable () -> Unit
) {
    DojoTheme {
        Surface(
            color = DojoTheme.colors.background,
            contentColor = DojoTheme.colors.onBackground,
            content = content,
            elevation = 8.dp
        )
    }
}
