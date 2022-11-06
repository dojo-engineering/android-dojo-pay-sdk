package tech.dojo.pay.uisdk.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem

@Composable
internal fun PaymentMethodsList(
    modifier: Modifier = Modifier,
    paymentMethodItems: List<PaymentMethodItemViewEntityItem>,
    onItemClicked: ((PaymentMethodItemViewEntityItem) -> Unit)
) {
    var selectedOption: PaymentMethodItemViewEntityItem by remember {
        mutableStateOf(
            PaymentMethodItemViewEntityItem.WalletItemItem
        )
    }
    val onSelectionChange =
        { paymentMethodItem: PaymentMethodItemViewEntityItem -> selectedOption = paymentMethodItem }
    LaunchedEffect(Unit) {
        if (paymentMethodItems.isNotEmpty() && paymentMethodItems[0] is PaymentMethodItemViewEntityItem.WalletItemItem) onItemClicked(
            selectedOption
        )
    }
    Box(modifier = modifier) {
        LazyColumn(
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(paymentMethodItems) { item ->
                when (item) {
                    is PaymentMethodItemViewEntityItem.WalletItemItem -> WalletItemWithRadioButton(
                        isSelected = selectedOption == item,
                        onClick = {
                            onSelectionChange(it)
                            onItemClicked(it)
                        }
                    )

                    is PaymentMethodItemViewEntityItem.CardItemItem -> CardItemWithRadioButton(
                        isSelected = selectedOption == item,
                        onClick = {
                            onSelectionChange(it)
                            onItemClicked(it)
                        },
                        cardItem = item
                    )
                }
            }
        }
    }
}

@SuppressLint("PaymentMethodsList")
@Preview("PaymentMethodsList", group = "PaymentMethodsList")
@Composable
fun PreviewPaymentMethodsList() {
    DojoPreview {
        val itemList = listOf<PaymentMethodItemViewEntityItem>(
            PaymentMethodItemViewEntityItem.CardItemItem(
                id = "",
                icon = R.drawable.ic_visa,
                scheme = "Visa",
                pan = "****9560"
            ),
            PaymentMethodItemViewEntityItem.CardItemItem(
                id = "",
                icon = R.drawable.ic_mastercard,
                scheme = "masterCard",
                pan = "****9560"
            ),
            PaymentMethodItemViewEntityItem.CardItemItem(
                id = "",
                icon = R.drawable.ic_amex,
                scheme = "Amex",
                pan = "****9560"
            )
        )
        PaymentMethodsList(
            paymentMethodItems = itemList,
            onItemClicked = {
                println("============================= $it")
            }
        )
    }
}
