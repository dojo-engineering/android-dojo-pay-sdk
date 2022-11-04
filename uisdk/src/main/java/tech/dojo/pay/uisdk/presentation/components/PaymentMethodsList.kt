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
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntity

@Composable
internal fun PaymentMethodsList(
    modifier: Modifier = Modifier,
    paymentMethodItems: List<PaymentMethodItemViewEntity>,
    onItemClicked: ((PaymentMethodItemViewEntity) -> Unit)
) {
    var selectedOption: PaymentMethodItemViewEntity by remember {
        mutableStateOf(
            PaymentMethodItemViewEntity.WalletItem
        )
    }
    val onSelectionChange =
        { paymentMethodItem: PaymentMethodItemViewEntity -> selectedOption = paymentMethodItem }
    LaunchedEffect(Unit) {
        if (paymentMethodItems.isNotEmpty() && paymentMethodItems[0] is PaymentMethodItemViewEntity.WalletItem) onItemClicked(
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
                    is PaymentMethodItemViewEntity.WalletItem -> WalletItemWithRadioButton(
                        isSelected = selectedOption == item, onClick = {
                            onSelectionChange(it)
                            onItemClicked(it)
                        }
                    )

                    is PaymentMethodItemViewEntity.CardItem -> CardItemWithRadioButton(
                        isSelected = selectedOption == item, onClick = {
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
        val itemList = listOf<PaymentMethodItemViewEntity>(
            PaymentMethodItemViewEntity.CardItem(
                icon = R.drawable.ic_visa,
                scheme = "Visa",
                pan = "****9560"
            ),
            PaymentMethodItemViewEntity.CardItem(
                icon = R.drawable.ic_mastercard,
                scheme = "masterCard",
                pan = "****9560"
            ),
            PaymentMethodItemViewEntity.CardItem(
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