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
    currentSelectedMethod: PaymentMethodItemViewEntityItem?= null,
    paymentMethodItems: List<PaymentMethodItemViewEntityItem>,
    isInEditMode: Boolean,
    onItemChecked: ((PaymentMethodItemViewEntityItem) -> Unit),
    onItemLongClicked: ((PaymentMethodItemViewEntityItem) -> Unit)
) {
    var selectedOption: PaymentMethodItemViewEntityItem by remember {
        mutableStateOf(
            currentSelectedMethod ?: PaymentMethodItemViewEntityItem.WalletItemItem
        )
    }
    val onSelectionChange =
        { paymentMethodItem: PaymentMethodItemViewEntityItem -> selectedOption = paymentMethodItem }

    LaunchedEffect(Unit) {
        if (paymentMethodItems.isNotEmpty() && paymentMethodItems[0] is PaymentMethodItemViewEntityItem.WalletItemItem) onItemChecked(
            selectedOption
        )
    }
    Box(modifier = modifier) {
        LazyColumn(
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(paymentMethodItems) { item ->
                when (item) {
                    is PaymentMethodItemViewEntityItem.WalletItemItem -> WalletItemWithRadioButton(
                        isSelected = selectedOption == item,
                        showRadioButton = !isInEditMode,
                        onClick = {
                            onSelectionChange(it)
                            onItemChecked(it)
                        }
                    )

                    is PaymentMethodItemViewEntityItem.CardItemItem -> CardItemWithRadioButton(
                        isSelected = selectedOption == item,
                        inEditeMode = isInEditMode,
                        onClick = {
                            onSelectionChange(it)
                            onItemChecked(it)
                        },
                        cardItem = item,
                        onLongClick = {
                            onSelectionChange(it)
                            onItemLongClicked(it)
                        }
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
            onItemChecked = {
                println("============================= $it")
            },
            onItemLongClicked = {},
            currentSelectedMethod = null,
            isInEditMode = false
        )
    }
}
