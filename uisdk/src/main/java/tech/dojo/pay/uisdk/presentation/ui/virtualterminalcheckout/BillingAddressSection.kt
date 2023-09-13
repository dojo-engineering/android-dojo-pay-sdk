package tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.CountrySelectorField
import tech.dojo.pay.uisdk.presentation.components.InputFieldModifierWithFocusChangedAndScrollingLogic
import tech.dojo.pay.uisdk.presentation.components.InputFieldWithErrorMessage
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.medium
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.state.BillingAddressViewState
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.viewmodel.VirtualTerminalViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun BillingAddressSection(
    viewModel: VirtualTerminalViewModel,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    scrollState: ScrollState,
    keyboardController: SoftwareKeyboardController?,
) {
    val state = viewModel.state.observeAsState().value ?: return
    if (state.billingAddressSection?.isVisible == true) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.background(DojoTheme.colors.primarySurfaceBackgroundColor),
        ) {
            HeaderTitle()
            Address1Field(
                state.billingAddressSection,
                viewModel,
                coroutineScope,
                scrollToPosition,
                scrollState,
                keyboardController,
            )
            Address2Field(
                state.billingAddressSection,
                viewModel,
                coroutineScope,
                scrollToPosition,
                scrollState,
                keyboardController,
            )
            CityField(
                state.billingAddressSection,
                viewModel,
                coroutineScope,
                scrollToPosition,
                scrollState,
                keyboardController,
            )
            PostalCodeField(
                state.billingAddressSection,
                viewModel,
                coroutineScope,
                scrollToPosition,
                scrollState,
                keyboardController,
            )
            CountryField(
                state.billingAddressSection,
                viewModel,
            )
        }
    }
}

@Composable
private fun HeaderTitle() {
    Text(
        text = stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_title_billing),
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = DojoTheme.typography.h6.medium,
        color = DojoTheme.colors.primaryLabelTextColor.copy(alpha = ContentAlpha.high),
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Address1Field(
    billingAddressViewState: BillingAddressViewState,
    viewModel: VirtualTerminalViewModel,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    scrollState: ScrollState,
    keyboardController: SoftwareKeyboardController?,
) {
    val hasBeenFocused by remember { mutableStateOf(false) }
    val scrollOffset = with(LocalDensity.current) {
        billingAddressViewState.itemPoissonOffset.dp.toPx() + NORMAL_FILED_SIZE_DP.dp.toPx()
    }
    InputFieldWithErrorMessage(
        modifier = InputFieldModifierWithFocusChangedAndScrollingLogic(
            coroutineScope = coroutineScope,
            scrollState = scrollState,
            initialHasBeenFocused = hasBeenFocused,
            onValidate = {
                viewModel.onValidateAddress1Field(
                    billingAddressViewState.addressLine1.value,
                    false,
                )
            },
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        value = billingAddressViewState.addressLine1.value,
        isError = billingAddressViewState.addressLine1.isError,
        assistiveText = billingAddressViewState.addressLine1.errorMessages?.let {
            AnnotatedString(stringResource(id = it))
        },
        onValueChange = { viewModel.onAddress1FieldChanged(it, false) },
        label = buildAnnotatedString { append(stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_field_shipping_line_1)) },
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Address2Field(
    billingAddressViewState: BillingAddressViewState,
    viewModel: VirtualTerminalViewModel,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    scrollState: ScrollState,
    keyboardController: SoftwareKeyboardController?,
) {
    val hasBeenFocused by remember { mutableStateOf(false) }

    val scrollOffset = with(LocalDensity.current) {
        billingAddressViewState.itemPoissonOffset.dp.toPx() + (2 * NORMAL_FILED_SIZE_DP.dp.toPx())
    }
    val label = buildAnnotatedString {
        append(stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_field_shipping_line_2))
        append(" ")
        withStyle(SpanStyle(DojoTheme.colors.primaryLabelTextColor.copy(alpha = ContentAlpha.medium))) { append(stringResource(id = R.string.dojo_ui_sdk_dojo_ui_sdk_card_details_checkout_optional)) }
    }

    InputFieldWithErrorMessage(
        modifier = InputFieldModifierWithFocusChangedAndScrollingLogic(
            coroutineScope = coroutineScope,
            scrollState = scrollState,
            initialHasBeenFocused = hasBeenFocused,
            onValidate = { },
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        value = billingAddressViewState.addressLine2.value,
        isError = billingAddressViewState.addressLine2.isError,
        assistiveText = billingAddressViewState.addressLine2.errorMessages?.let {
            AnnotatedString(stringResource(id = it))
        },
        onValueChange = { viewModel.onAddress2FieldChanged(it, false) },
        label = label,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CityField(
    billingAddressViewState: BillingAddressViewState,
    viewModel: VirtualTerminalViewModel,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    scrollState: ScrollState,
    keyboardController: SoftwareKeyboardController?,
) {
    val hasBeenFocused by remember { mutableStateOf(false) }
    val scrollOffset = with(LocalDensity.current) {
        billingAddressViewState.itemPoissonOffset.dp.toPx() + (3 * NORMAL_FILED_SIZE_DP.dp.toPx())
    }

    InputFieldWithErrorMessage(
        modifier = InputFieldModifierWithFocusChangedAndScrollingLogic(
            coroutineScope = coroutineScope,
            scrollState = scrollState,
            initialHasBeenFocused = hasBeenFocused,
            onValidate = { viewModel.onValidateCityField(billingAddressViewState.city.value, false) },
        ).padding(top = 16.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        value = billingAddressViewState.city.value,
        isError = billingAddressViewState.city.isError,
        assistiveText = billingAddressViewState.city.errorMessages?.let {
            AnnotatedString(stringResource(id = it))
        },
        onValueChange = { viewModel.onCityFieldChanged(it, false) },
        label = buildAnnotatedString { append(stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_field_shipping_city)) },
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PostalCodeField(
    billingAddressViewState: BillingAddressViewState,
    viewModel: VirtualTerminalViewModel,
    coroutineScope: CoroutineScope,
    scrollToPosition: Float,
    scrollState: ScrollState,
    keyboardController: SoftwareKeyboardController?,
) {
    val hasBeenFocused by remember { mutableStateOf(false) }
    val scrollOffset = with(LocalDensity.current) {
        billingAddressViewState.itemPoissonOffset.dp.toPx() + (4 * NORMAL_FILED_SIZE_DP.dp.toPx())
    }

    InputFieldWithErrorMessage(
        modifier = InputFieldModifierWithFocusChangedAndScrollingLogic(
            coroutineScope = coroutineScope,
            scrollState = scrollState,
            initialHasBeenFocused = hasBeenFocused,
            onValidate = {
                viewModel.onValidatePostalCodeField(
                    billingAddressViewState.postalCode.value,
                    false,
                )
            },
        ).padding(top = 16.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        value = billingAddressViewState.postalCode.value,
        isError = billingAddressViewState.postalCode.isError,
        assistiveText = billingAddressViewState.postalCode.errorMessages?.let {
            AnnotatedString(stringResource(id = it))
        },
        onValueChange = { viewModel.onSPostalCodeFieldChanged(it, false) },
        label = buildAnnotatedString { append(stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_field_shipping_postcode)) },
    )
}

@Composable
private fun CountryField(
    billingAddressViewState: BillingAddressViewState,
    viewModel: VirtualTerminalViewModel,
) {
    CountrySelectorField(
        modifier = Modifier
            .padding(vertical = 16.dp),
        label = buildAnnotatedString { append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_field_shipping_country)) },
        supportedCountriesViewEntity = billingAddressViewState.supportedCountriesList,
        onCountrySelected = { viewModel.onCountrySelected(it, false) },
    )
}
private const val NORMAL_FILED_SIZE_DP = 100
