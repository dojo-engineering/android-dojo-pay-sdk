package tech.dojo.pay.uisdk.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.components.theme.DojoTheme

@Composable
@OptIn(ExperimentalMaterialApi::class)
internal fun DojoBottomSheet(
    modifier: Modifier = Modifier,
    sheetContent: @Composable ColumnScope.() -> Unit,
    sheetState: ModalBottomSheetState,
    content: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        sheetBackgroundColor = DojoTheme.colors.background,
        sheetContentColor = DojoTheme.colors.onBackground,
        scrimColor = DojoTheme.colors.onBackground.copy(alpha = 0.35f),
        sheetContent = {
            sheetContent()
        },
        content = content
    )
}
