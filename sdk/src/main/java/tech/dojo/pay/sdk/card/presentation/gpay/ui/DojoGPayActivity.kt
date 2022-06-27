package tech.dojo.pay.sdk.card.presentation.gpay.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.gms.wallet.*
import com.google.android.gms.wallet.AutoResolveHelper.RESULT_ERROR
import org.json.JSONException
import org.json.JSONObject
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.R
import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract
import tech.dojo.pay.sdk.card.entities.DojoGPayParams
import tech.dojo.pay.sdk.card.presentation.gpay.util.GOOGLE_PAY_ACTIVITY_REQUEST_CODE
import tech.dojo.pay.sdk.card.presentation.gpay.util.DojoGPayEngine
import tech.dojo.pay.sdk.card.presentation.gpay.viewmodel.DojoGPayViewModel
import tech.dojo.pay.sdk.card.presentation.gpay.viewmodel.DojoGPayViewModelFactory

internal class DojoGPayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dojo_card_payment)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        performGPay()
    }

    private val viewModel: DojoGPayViewModel by viewModels {
        DojoGPayViewModelFactory(intent.extras)
    }

    private val gPayEngine: DojoGPayEngine by lazy { DojoGPayEngine(this) }

    private fun performGPay() {
        gPayEngine.isReadyToPay(
            onGpayAvailable = { startPaymentProcess() },
            onGpayUnavailable = { returnResult(DojoPaymentResult.SDK_INTERNAL_ERROR) }
        )
        viewModel.paymentResult.observe(this) { result ->
            var a = 0
        }
    }

    private fun startPaymentProcess() {
        val params = requireNotNull(intent.extras)
            .getSerializable(DojoCardPaymentResultContract.KEY_PARAMS) as DojoGPayParams
        gPayEngine.payWithGoogle(params.totalAmountPayload)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // Value passed in AutoResolveHelper
            GOOGLE_PAY_ACTIVITY_REQUEST_CODE -> {
                when (resultCode) {
                    RESULT_OK ->
                        data?.let { intent ->
                            PaymentData.getFromIntent(intent)?.let(::handlePaymentSuccess)
                        }

                    RESULT_CANCELED -> {
                        // The user cancelled the payment attempt
                    }

                    RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)
                            ?.let { Log.d("GPay Failed", it.status.toString()) }
                        returnResult(DojoPaymentResult.FAILED)
                    }
                }
            }
        }
    }

    private fun handlePaymentSuccess(paymentData: PaymentData) {
        val paymentInformation = paymentData.toJson()

        try {
            // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
            val paymentMethodData =
                JSONObject(paymentInformation).getJSONObject("paymentMethodData")
            val billingName = paymentMethodData.getJSONObject("info")
                .getJSONObject("billingAddress").getString("name")
            Log.d("BillingName", billingName)


//            viewModel.sendGPayDataToServer(gPayData = paymentMethodData.toString())
//            Toast.makeText(this, getString(R.string.payments_show_name, billingName), Toast.LENGTH_LONG).show()

            // Logging token string.
//            Log.d("GooglePaymentToken", paymentMethodData
//                .getJSONObject("tokenizationData")
//                .getString("token")) // TODO remove this log

            Toast.makeText(
                this,
                paymentMethodData.toString(),
                Toast.LENGTH_LONG
            ).show();

        } catch (e: JSONException) {
            Log.e(
                "handlePaymentSuccess",
                "Error: " + e.toString()
            ) // TODO and remove this log as well
        }
    }

    private fun returnResult(result: DojoPaymentResult) {
        val data = Intent()
        data.putExtra(DojoCardPaymentResultContract.KEY_RESULT, result)
        setResult(RESULT_OK, data)
        finish()
        overridePendingTransition(0, R.anim.exit)
    }

}