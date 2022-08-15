package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme

@Composable
@OptIn(ExperimentalMaterialApi::class)
internal fun DojoBottomSheet(
    modifier: Modifier = Modifier,
    sheetBackgroundColor: Color? = null,
    sheetContentColor: Color? = null,
    scrimColor: Color? = null,
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
        sheetBackgroundColor = sheetBackgroundColor ?: DojoTheme.colors.background,
        sheetContentColor = sheetContentColor ?: DojoTheme.colors.onBackground,
        scrimColor = scrimColor ?: DojoTheme.colors.onBackground.copy(alpha = 0.35f),
        sheetContent = {
            sheetContent()
        },
        content = content
    )
}
