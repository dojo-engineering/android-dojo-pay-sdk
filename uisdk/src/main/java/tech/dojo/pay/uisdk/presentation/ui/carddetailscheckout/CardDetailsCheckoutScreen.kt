package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.*
import tech.dojo.pay.uisdk.presentation.components.AmountBanner
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.CardInputField
import tech.dojo.pay.uisdk.presentation.components.CardNumberInPutField
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.DojoBrandFooter
import tech.dojo.pay.uisdk.presentation.components.DojoFullGroundButton
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel.CardDetailsCheckoutViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CardDetailsCheckoutScreen(
    viewModel: CardDetailsCheckoutViewModel,
    onCloseClicked: () -> Unit,
    onBackClicked: () -> Unit,
) {
    val state = viewModel.state.observeAsState().value ?: return
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val (appBar, banner, cardHolderName, cardInputField, emailField, payBtn, footer) = createRefs()

            DojoAppBar(
                modifier = Modifier.constrainAs(appBar) {
                    start.linkTo(parent.start, 0.dp)
                    end.linkTo(parent.end, 0.dp)
                    top.linkTo(parent.top, 0.dp)
                    width = Dimension.fillToConstraints
                },
                title = stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_title),
                titleGravity = TitleGravity.LEFT,
                navigationIcon = AppBarIcon.back { onBackClicked() },
                actionIcon = AppBarIcon.close { onCloseClicked() }
            )
            Column(
                modifier = Modifier.constrainAs(banner) {
                    top.linkTo(appBar.bottom, 16.dp)
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    width = Dimension.fillToConstraints
                },
                verticalArrangement = Arrangement.spacedBy(32.dp)

            ) {
                AmountBanner(
                    amount = state.totalAmount,
                    currencyLogo = state.amountCurrency
                )


                if (true) {
                    InputFieldWithErrorMessage(
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                        value = state.emailInputField.value,
                        onValueChange = { viewModel.onEmailValueChanged(it) },
                        label = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    letterSpacing = 0.15.sp
                                ),
                            ) {
                                append(stringResource(R.string.dojo_ui_sdk_card_details_email_field))
                            }
                        }
                    )
                }

                InputFieldWithErrorMessage(
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    value = state.cardHolderInputField.value,
                    onValueChange = { viewModel.onCardHolderValueChanged(it) },
                    label = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                letterSpacing = 0.15.sp
                            ),
                        ) {
                            append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_field_card_name))
                        }
                    }
                )

                CardNumberInPutField(
                    label = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                letterSpacing = 0.15.sp
                            ),
                        ) {
                            append(stringResource(R.string.dojo_ui_sdk_card_details_checkout_field_pan))
                        }
                    },
                    cardNumberValue = state.cardDetailsInPutField.cardNumberValue,
                    cardNumberPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_pan),
                    onCardNumberValueChanged = { viewModel.onCardNumberValueChanged(it) }
                )


            }
            DojoFullGroundButton(
                modifier = Modifier.constrainAs(payBtn) {
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    bottom.linkTo(footer.bottom, 46.dp)
                    width = Dimension.fillToConstraints
                },
                text = stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_button_pay) + " " + state.amountCurrency + " " + state.totalAmount,
                isLoading = state.isLoading,
                enabled = state.isEnabled
            ) {
                if (!state.isLoading) {
                    viewModel.onPayWithCardClicked()
                }
            }

            DojoBrandFooter(
                modifier = Modifier.constrainAs(footer) {
                    start.linkTo(parent.start, 24.dp)
                    end.linkTo(parent.end, 24.dp)
                    bottom.linkTo(parent.bottom, 24.dp)
                    width = Dimension.fillToConstraints
                },
                withTermsAndPrivacy = true
            )
        }
    }
}
