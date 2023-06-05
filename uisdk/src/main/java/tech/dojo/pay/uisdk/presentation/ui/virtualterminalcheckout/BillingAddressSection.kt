package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.CountrySelectorField
import tech.dojo.pay.uisdk.presentation.components.InputFieldWithErrorMessage
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.medium
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.BillingAddressViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.viewmodel.VirtualTerminalViewModel

@Composable
internal fun BillingAddressSection(
    viewModel: VirtualTerminalViewModel
) {
    val state = viewModel.state.observeAsState().value ?: return
    if (state.billingAddressSection?.isVisible == true) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.background(DojoTheme.colors.primarySurfaceBackgroundColor),
        ) {
            HeaderTitle()
            Address1Field(state.billingAddressSection, viewModel)
            Address2Field(state.billingAddressSection, viewModel)
            CityField(state.billingAddressSection, viewModel)
            PostalCodeField(state.billingAddressSection, viewModel)
            CountryField(state.billingAddressSection, viewModel)
        }
    }
}

@Composable
private fun HeaderTitle() {
    Text(
        text = "Billing Address",
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = DojoTheme.typography.h6.medium,
        color = DojoTheme.colors.primaryLabelTextColor.copy(alpha = ContentAlpha.high)
    )
}

@Composable
private fun Address1Field(
    billingAddressViewState: BillingAddressViewState,
    viewModel: VirtualTerminalViewModel
) {
    var isTextNotFocused by remember { mutableStateOf(false) }
    InputFieldWithErrorMessage(
        modifier = Modifier.onFocusChanged {
            isTextNotFocused = if (it.isFocused) {
                true
            } else {
                if (isTextNotFocused) {
                    viewModel.onValidateAddress1Field(
                        billingAddressViewState.addressLine1.value, false
                    )
                }
                false
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        value = billingAddressViewState.addressLine1.value,
        isError = billingAddressViewState.addressLine1.isError,
        assistiveText = billingAddressViewState.addressLine1.errorMessages?.let {
            AnnotatedString(stringResource(id = it))
        },
        onValueChange = { viewModel.onAddress1FieldChanged(it, false) },
        label = buildAnnotatedString { append("Address 1") }
    )
}

@Composable
private fun Address2Field(
    billingAddressViewState: BillingAddressViewState,
    viewModel: VirtualTerminalViewModel
) {
    InputFieldWithErrorMessage(
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        value = billingAddressViewState.addressLine2.value,
        isError = billingAddressViewState.addressLine2.isError,
        assistiveText = billingAddressViewState.addressLine2.errorMessages?.let {
            AnnotatedString(stringResource(id = it))
        },
        onValueChange = { viewModel.onAddress2FieldChanged(it, false) },
        label = buildAnnotatedString { append("Address 2") }
    )
}

@Composable
private fun CityField(
    billingAddressViewState: BillingAddressViewState,
    viewModel: VirtualTerminalViewModel
) {
    var isTextNotFocused by remember { mutableStateOf(false) }
    InputFieldWithErrorMessage(
        modifier = Modifier.onFocusChanged {
            isTextNotFocused = if (it.isFocused) {
                true
            } else {
                if (isTextNotFocused) {
                    viewModel.onValidateCityField(billingAddressViewState.city.value, false)
                }
                false
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        value = billingAddressViewState.city.value,
        isError = billingAddressViewState.city.isError,
        assistiveText = billingAddressViewState.city.errorMessages?.let {
            AnnotatedString(stringResource(id = it))
        },
        onValueChange = { viewModel.onCityFieldChanged(it, false) },
        label = buildAnnotatedString { append("cirty") }
    )
}

@Composable
private fun PostalCodeField(
    billingAddressViewState: BillingAddressViewState,
    viewModel: VirtualTerminalViewModel
) {
    var isTextNotFocused by remember { mutableStateOf(false) }
    InputFieldWithErrorMessage(
        modifier = Modifier.onFocusChanged {
            isTextNotFocused = if (it.isFocused) {
                true
            } else {
                if (isTextNotFocused) {
                    viewModel.onValidatePostalCodeField(
                        billingAddressViewState.postalCode.value, false
                    )
                }
                false
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        value = billingAddressViewState.postalCode.value,
        isError = billingAddressViewState.postalCode.isError,
        assistiveText = billingAddressViewState.postalCode.errorMessages?.let {
            AnnotatedString(stringResource(id = it))
        },
        onValueChange = { viewModel.onSPostalCodeFieldChanged(it, false) },
        label = buildAnnotatedString { append("postal code") }
    )
}

@Composable
private fun CountryField(
    billingAddressViewState: BillingAddressViewState,
    viewModel: VirtualTerminalViewModel
) {
    CountrySelectorField(
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_billing_country)) },
        supportedCountriesViewEntity = billingAddressViewState.supportedCountriesList,
        onCountrySelected = { viewModel.onCountrySelected(it, false) }
    )
}
