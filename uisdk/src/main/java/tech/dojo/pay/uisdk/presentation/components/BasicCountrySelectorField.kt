package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.entity.SupportedCountriesViewEntity
import tech.dojo.pay.uisdk.R

@Composable
internal fun BasicCountrySelectorField(
    supportedCountriesViewEntity: List<SupportedCountriesViewEntity>,
    onCountrySelected: (SupportedCountriesViewEntity) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    enabled: Boolean = true,
    textHorizontalPadding: Dp = 16.dp,
    textVerticalPadding: Dp = 12.dp,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = TextFieldDefaults.outlinedTextFieldColors()
    var selectedCountry: SupportedCountriesViewEntity by remember {
        mutableStateOf(
            supportedCountriesViewEntity[0]
        )
    }
    var expanded by remember { mutableStateOf(false) }


    Column(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .border(
                width = 1.dp,
                color = colors.indicatorColor(enabled, isError, interactionSource).value,
                shape = DojoTheme.shapes.small
            )
            .clickable { expanded = !expanded }

    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = textHorizontalPadding, vertical = textVerticalPadding)
                .fillMaxHeight()
        ) {
            Row {
                Text(
                    modifier = Modifier.weight(1f, true),
                    text = selectedCountry.countryName,
                    style = DojoTheme.typography.subtitle1.copy(color = colors.textColor(enabled).value),
                    color = colors.textColor(enabled).value
                )
                ResourceIcon(
                    modifier = Modifier.padding(start = 4.dp),
                    id = R.drawable.ic_expand_more_24px
                )
                DropdownMenu(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredSizeIn(maxHeight = 200.dp),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    supportedCountriesViewEntity.forEach { countryItem ->
                        DropdownMenuItem(onClick = {
                            expanded = false
                            selectedCountry = countryItem
                            onCountrySelected(countryItem)
                        }) {
                            Text(
                                modifier = Modifier.weight(1f, true),
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
