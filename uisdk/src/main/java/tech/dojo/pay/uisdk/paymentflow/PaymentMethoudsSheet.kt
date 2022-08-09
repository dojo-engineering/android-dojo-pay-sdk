package tech.dojo.pay.uisdk.paymentflow

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.uisdk.components.AppBarIcon
import tech.dojo.pay.uisdk.components.DojoAppBar
import tech.dojo.pay.uisdk.components.DojoBottomSheet
import tech.dojo.pay.uisdk.components.DojoFullGroundButton
import tech.dojo.pay.uisdk.components.DojoOutlinedButton
import tech.dojo.pay.uisdk.components.TitleGravity
import tech.dojo.pay.uisdk.components.theme.DojoTheme

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
internal fun ShowPaymentMethodsSheet(
    attachedActivity: Activity,
    onGpayClicked: () -> Unit
) {
    val paymentMethodssheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Expanded,
            confirmStateChange = { false }
        )

    val coroutineScope = rememberCoroutineScope()
    val googlePayVisibility = remember { mutableStateOf(true) }
    CheckGooglePayAvailability(attachedActivity,googlePayVisibility)

    DojoTheme {
        DojoBottomSheet(
            modifier = Modifier.fillMaxSize(),
            sheetState = paymentMethodssheetState,
            sheetContent = {
                BottomSheetItems(
                    attachedActivity,
                    coroutineScope,
                    paymentMethodssheetState,
                    googlePayVisibility,
                    onGpayClicked
                )
            }
        ) {}
    }

}

@Composable
private fun CheckGooglePayAvailability(
    attachedActivity: Activity,
    googlePayVisibility: MutableState<Boolean>
) {
    DojoSdk.isGpayAvailable(
        activity = attachedActivity,
        dojoGPayConfig = DojoGPayConfig(
            merchantName = "Dojo Cafe (Paymentsense)",
            merchantId = "BCR2DN6T57R5ZI34",
            gatewayMerchantId = "119784244252745"
        ),
        { googlePayVisibility.value = true },
        { googlePayVisibility.value = false }
    )
}

@ExperimentalMaterialApi
@Composable
private fun BottomSheetItems(
    attachedActivity: Activity,
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    googlePayVisibility: MutableState<Boolean>,
    onGpayClicked: () -> Unit
) {
    DojoAppBar(
        modifier = Modifier.height(60.dp),
        title = "Payment method",
        titleGravity = TitleGravity.LEFT,
        actionIcon = AppBarIcon.close() {
            coroutineScope.launch {
                sheetState.hide()
            }
            attachedActivity.finish()
        }
    )
    if (googlePayVisibility.value) {
        DojoFullGroundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 16.dp, 24.dp, 8.dp),
            text = "google pay"
        ) {
            coroutineScope.launch {
                sheetState.hide()
                onGpayClicked()
            }
        }
    }
    DojoOutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp, 8.dp, 24.dp, 16.dp),
        text = "manage payment methods"
    ) {}
}