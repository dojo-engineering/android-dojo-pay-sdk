package tech.dojo.pay.sdk.card.presentation.gpay.util

import android.app.Activity
import android.util.Log
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.PaymentsClient
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.entities.DojoTotalAmount

class DojoGPayEngine(
    private val activity: Activity,
) {
    private val paymentsClient: PaymentsClient by lazy { PaymentsUtil.createPaymentsClient(activity) }

    /**
     * Check that Google Pay is available and ready
     */
    internal fun isReadyToPay(
        dojoGPayConfig: DojoGPayConfig,
        onGpayAvailable: () -> Unit,
        onGpayUnavailable: () -> Unit
    ) {
        val isReadyToPayJson = PaymentsUtil.getReadyToPayRequest(dojoGPayConfig)
        if (isReadyToPayJson != null) {
            val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString())
            // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
            // Success and Failure Listener to be triggered when the result of the call is known.
            paymentsClient
                .isReadyToPay(request)
                .addOnSuccessListener { onGpayAvailable() }
                .addOnFailureListener {
                    Log.w("isReadyToPay failed", it)
                    onGpayUnavailable()
                }
        } else {
            onGpayUnavailable()
        }
    }

    /**
     * start the payment process for google pay
     */
    internal fun payWithGoogle(
        totalAmountPayload: DojoTotalAmount,
        dojoGPayConfig: DojoGPayConfig
    ) {
        val paymentDataRequestJson =
            PaymentsUtil.getPaymentDataRequest(totalAmountPayload, dojoGPayConfig)
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        AutoResolveHelper.resolveTask(
            paymentsClient.loadPaymentData(request), activity, GOOGLE_PAY_ACTIVITY_REQUEST_CODE
        )
    }
}

const val GOOGLE_PAY_ACTIVITY_REQUEST_CODE = 99999