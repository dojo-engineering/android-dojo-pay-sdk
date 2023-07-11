package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
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
) {
    if (supportedCountriesViewEntity.isNotEmpty()) {
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
                enabled,
            )
        }
    }
}
