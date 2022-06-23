package tech.dojo.pay.sdk.card

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import com.google.android.gms.wallet.PaymentsClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import tech.dojo.pay.sdk.R
import tech.dojo.pay.sdk.card.entities.PaymentResult

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

    private lateinit var paymentsClient: PaymentsClient

    private fun performGPay() {
        paymentsClient = PaymentsUtil.createPaymentsClient(this)
        possiblyShowGooglePayButton()
        viewModel.paymentResult.observe(this) { result ->
            var a = 0
        }
    }

    private val baseRequest = JSONObject().apply {
        put("apiVersion", 2)
        put("apiVersionMinor", 0)
    }

    private fun possiblyShowGooglePayButton() {

        val isReadyToPayJson = isReadyToPayRequest() ?: return
        val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString()) ?: return

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        val task = paymentsClient.isReadyToPay(request)
        task.addOnCompleteListener { completedTask ->
            try {
                completedTask.getResult(ApiException::class.java)?.let(::setGooglePayAvailable)
            } catch (exception: ApiException) {
                // Process error
                Log.w("isReadyToPay failed", exception)
            }
        }
    }

    private fun isReadyToPayRequest(): JSONObject? {
        return try {
            baseRequest.apply {
                put("allowedPaymentMethods", JSONArray().put(PaymentsUtil.baseCardPaymentMethod()))
            }

        } catch (e: JSONException) {
            null
        }
    }

    private fun setGooglePayAvailable(available: Boolean) {

        val priceCents: Long = 10

        val paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceCents)
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            AutoResolveHelper.resolveTask(
                paymentsClient.loadPaymentData(request), this, 99999) // TODO code
        }

        if (available) {
//            googlePayButton.visibility = View.VISIBLE
        } else {
//            Toast.makeText(
//                this,
//                "Unfortunately, Google Pay is not available on this device",
//                Toast.LENGTH_LONG).show();
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // Value passed in AutoResolveHelper
            99999 -> {
                when (resultCode) {
                    RESULT_OK ->
                        data?.let { intent ->
                            PaymentData.getFromIntent(intent)?.let(::handlePaymentSuccess)
                        }

                    RESULT_CANCELED -> {
                        // The user cancelled the payment attempt
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            Log.d("GPay Failed", it.status.toString())
                        }
                    }
                }

                // Re-enables the Google Pay payment button.
//                googlePayButton.isClickable = true
            }
        }
    }

    private fun handlePaymentSuccess(paymentData: PaymentData) {
        val paymentInformation = paymentData.toJson() ?: return

        try {
            // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
            val paymentMethodData = JSONObject(paymentInformation).getJSONObject("paymentMethodData")
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
                Toast.LENGTH_LONG).show();

        } catch (e: JSONException) {
            Log.e("handlePaymentSuccess", "Error: " + e.toString()) // TODO and remove this log as well
        }
    }
}