package tech.dojo.pay.sdk.card.presentation.gpay.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.commit
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.AutoResolveHelper.RESULT_ERROR
import com.google.android.gms.wallet.PaymentData
import org.json.JSONException
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.R
import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract
import tech.dojo.pay.sdk.card.entities.DojoGPayParams
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams
import tech.dojo.pay.sdk.card.presentation.gpay.util.DojoGPayEngine
import tech.dojo.pay.sdk.card.presentation.gpay.util.GOOGLE_PAY_ACTIVITY_REQUEST_CODE
import tech.dojo.pay.sdk.card.presentation.gpay.viewmodel.DojoGPayViewModel
import tech.dojo.pay.sdk.card.presentation.gpay.viewmodel.DojoGPayViewModelFactory
import tech.dojo.pay.sdk.card.presentation.threeds.Dojo3DSFragment

internal class DojoGPayActivity : AppCompatActivity() {

    private val viewModel: DojoGPayViewModel by viewModels {
        DojoGPayViewModelFactory(intent.extras)
    }

    private val gPayEngine: DojoGPayEngine by lazy { DojoGPayEngine(this) }

    val params: DojoGPayParams by lazy {
        requireNotNull(intent.extras)
            .getSerializable(DojoCardPaymentResultContract.KEY_PARAMS) as DojoGPayParams
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dojo_card_payment)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        observeLiveData()
        performGPay()
    }

    private fun observeLiveData() {
        viewModel.paymentResult.observe(this) { result ->
            when (result) {
                is PaymentResult.Completed -> returnResult(result.value)
                is PaymentResult.ThreeDSRequired -> navigate3DS(result.params)
            }
        }
    }

    private fun performGPay() {
        gPayEngine.isReadyToPay(
            params.dojoGPayPayload.dojoGPayConfig,
            onGpayAvailable = { startPaymentProcess() },
            onGpayUnavailable = { returnResult(DojoPaymentResult.SDK_INTERNAL_ERROR) }
        )
    }

    private fun startPaymentProcess() {
        gPayEngine.payWithGoogle(
            params.dojoPaymentIntent.totalAmount,
            params.dojoGPayPayload.dojoGPayConfig
        ) { returnResult(DojoPaymentResult.SDK_INTERNAL_ERROR) }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // Value passed in AutoResolveHelper
            GOOGLE_PAY_ACTIVITY_REQUEST_CODE -> handleGPayResults(resultCode, data)
        }
    }

    private fun handleGPayResults(resultCode: Int, data: Intent?) {
        when (resultCode) {
            RESULT_OK -> handleOkResultFromGPay(data)
            RESULT_CANCELED -> returnResult(DojoPaymentResult.FAILED)
            RESULT_ERROR -> {
                AutoResolveHelper.getStatusFromIntent(data)
                    ?.let { Log.d("GPay Failed", it.status.toString()) }
                returnResult(DojoPaymentResult.FAILED)
            }
        }
    }

    private fun handleOkResultFromGPay(data: Intent?) {
        if (data != null && PaymentData.getFromIntent(data) != null) {
            PaymentData.getFromIntent(data)?.let(::handlePaymentSuccess)
        } else {
            returnResult(DojoPaymentResult.SDK_INTERNAL_ERROR)
        }
    }

    private fun handlePaymentSuccess(paymentData: PaymentData) {
        try {
            viewModel.sendGPayDataToServer(gPayData = paymentData.toJson(), dojoGPayParams = params)
        } catch (e: JSONException) {
            returnResult(DojoPaymentResult.SDK_INTERNAL_ERROR)
        }
    }

    private fun navigate3DS(params: ThreeDSParams) {
        supportFragmentManager.commit {
            setCustomAnimations(R.anim.enter, 0)
            replace(R.id.container, Dojo3DSFragment.newInstance(params))
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