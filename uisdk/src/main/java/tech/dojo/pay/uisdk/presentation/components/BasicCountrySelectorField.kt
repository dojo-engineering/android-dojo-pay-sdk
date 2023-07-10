package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity

@Composable
internal fun BasicCountrySelectorField(
    supportedCountriesViewEntity: List<SupportedCountriesViewEntity>,
    onCountrySelected: (SupportedCountriesViewEntity) -> Unit,
    enabled: Boolean = true,
) {
    val colors = TextFieldDefaults.outlinedTextFieldColors(
        textColor = DojoTheme.colors.primaryLabelTextColor,
        cursorColor = DojoTheme.colors.primaryLabelTextColor,
        unfocusedBorderColor = DojoTheme.colors.inputFieldDefaultBorderColor,
        backgroundColor = DojoTheme.colors.inputFieldBackgroundColor,
        focusedBorderColor = DojoTheme.colors.inputFieldSelectedBorderColor,
        placeholderColor = DojoTheme.colors.inputFieldPlaceholderColor,
        errorBorderColor = DojoTheme.colors.errorTextColor,
        errorCursorColor = DojoTheme.colors.errorTextColor,
        errorLabelColor = DojoTheme.colors.errorTextColor,
        errorLeadingIconColor = DojoTheme.colors.errorTextColor,
        errorTrailingIconColor = DojoTheme.colors.errorTextColor
    )

    var selectedCountry: SupportedCountriesViewEntity by remember {
        mutableStateOf(supportedCountriesViewEntity[0])
    }
    var expanded by remember { mutableStateOf(false) }

    DojoExpandableCard(
        header = selectedCountry.countryName,
        isExpanded = expanded
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(DojoTheme.colors.inputFieldBackgroundColor)

        ) {
            items(supportedCountriesViewEntity) { countryItem ->
                Text(
                    modifier = Modifier
                        .clickable {
                            expanded = false
                            selectedCountry = countryItem
                            onCountrySelected(countryItem)
                        }
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .fillMaxWidth(),
                    text = countryItem.countryName,
                    style = DojoTheme.typography.subtitle1.copy(
                        color = colors.textColor(
                            enabled
                        ).value
                    ),
                    color = colors.textColor(enabled).value
                )
            }
        }
    }
}

@Preview("country selection Group", group = "countrySelection")
@Composable
internal fun Preview() = DojoPreview {
    BasicCountrySelectorField(
        listOf(
            SupportedCountriesViewEntity("USA", "de", false),
            SupportedCountriesViewEntity("UK", "de", false),
            SupportedCountriesViewEntity("Egypt", "de", false),
            SupportedCountriesViewEntity("Germany", "de", false),
            SupportedCountriesViewEntity("CANDA", "de", false)
        ),
        {}
    )
}
