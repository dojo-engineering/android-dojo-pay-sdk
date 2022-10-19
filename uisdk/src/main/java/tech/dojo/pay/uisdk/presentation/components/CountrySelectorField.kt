package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity

@Composable
internal fun CountrySelectorField(
    supportedCountriesViewEntity: List<SupportedCountriesViewEntity>,
    onCountrySelected: (SupportedCountriesViewEntity) -> Unit,
    modifier: Modifier = Modifier,
    label: AnnotatedString? = null,
    assistiveText: AnnotatedString? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    textHorizontalPadding: Dp = 16.dp,
    textVerticalPadding: Dp = 12.dp
) {
    LabelAndAssistiveTextWrapper(
        modifier = modifier,
        label = label,
        assistiveText = assistiveText,
        isError = isError,
        enabled = enabled
    ) {
        BasicCountrySelectorField(
            supportedCountriesViewEntity,
            onCountrySelected,
            modifier,
            isError,
            enabled,
            textHorizontalPadding,
            textVerticalPadding
        )
    }
}