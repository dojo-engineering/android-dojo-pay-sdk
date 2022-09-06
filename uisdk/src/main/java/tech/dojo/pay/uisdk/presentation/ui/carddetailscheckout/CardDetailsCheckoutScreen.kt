package tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.*
import tech.dojo.pay.uisdk.presentation.components.AmountBanner
import tech.dojo.pay.uisdk.presentation.components.AppBarIcon
import tech.dojo.pay.uisdk.presentation.components.DojoAppBar
import tech.dojo.pay.uisdk.presentation.components.DojoBrandFooter
import tech.dojo.pay.uisdk.presentation.components.DojoFullGroundButton
import tech.dojo.pay.uisdk.presentation.components.TitleGravity
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel.CardDetailsCheckoutViewModel

@Composable
fun CardDetailsCheckoutScreen(
    viewModel: CardDetailsCheckoutViewModel,
    onCloseClicked: () -> Unit,
    onBackClicked: () -> Unit,
) {
    val state = viewModel.state.observeAsState().value ?: return

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (appBar, banner, cardHolderName, cardInputField, payBtn, footer) = createRefs()

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
            AmountBanner(
                modifier = Modifier.constrainAs(banner) {
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    top.linkTo(appBar.bottom, 16.dp)
                    width = Dimension.fillToConstraints
                },
                amount = state.totalAmount,
                currencyLogo = state.amountCurrency
            )
            InputFieldWithErrorMessage(
                modifier = Modifier.constrainAs(cardHolderName) {
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    top.linkTo(banner.bottom, 16.dp)
                    width = Dimension.fillToConstraints
                },
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
                },
            )
            CardInputField(
                modifier = Modifier.constrainAs(cardInputField) {
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    top.linkTo(cardHolderName.bottom, 16.dp)
                    width = Dimension.fillToConstraints
                },
                label =
                buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            letterSpacing = 0.15.sp
                        ),
                    ) {
                        append(
                            stringResource(
                                R.string.dojo_ui_sdk_card_details_checkout_field_pan
                            )
                        )
                    }
                },
                maxLines= 1,
                cardNumberPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_pan),
                cardNumberValue = state.cardDetailsInPutField.cardNumberValue,
                onCardNumberValueChanged = { viewModel.onCardNumberValueChanged(it) },
                cvvPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_cvv),
                cvvValue = state.cardDetailsInPutField.cvvValue,
                onCvvValueChanged = { viewModel.onCvvValueChanged(it) },
                expireDaterPlaceholder = stringResource(R.string.dojo_ui_sdk_card_details_checkout_placeholder_expiry),
                expireDateValue = state.cardDetailsInPutField.expireDateValueValue,
                onExpireDateValueChanged = { viewModel.onExpireDareValueChanged(it) }
            )


            DojoFullGroundButton(
                modifier = Modifier.constrainAs(payBtn) {
                    start.linkTo(parent.start, 24.dp)
                    end.linkTo(parent.end, 24.dp)
                    bottom.linkTo(footer.bottom, 46.dp)
                    width = Dimension.fillToConstraints
                },
                text = stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_button_pay)+" "+state.amountCurrency+" "+state.totalAmount,
                isLoading = state.isLoading
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
