package tech.dojo.pay.sdk.card.presentation.gpay.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.contract.ApiTaskResult
import com.google.android.gms.wallet.contract.TaskResultContracts
import org.json.JSONException
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.R
import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract
import tech.dojo.pay.sdk.card.entities.DojoGPayParams
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams
import tech.dojo.pay.sdk.card.presentation.gpay.util.DojoGPayEngine
import tech.dojo.pay.sdk.card.presentation.gpay.viewmodel.DojoGPayViewModel
import tech.dojo.pay.sdk.card.presentation.gpay.viewmodel.DojoGPayViewModelFactory
import tech.dojo.pay.sdk.card.presentation.threeds.Dojo3DSBaseViewModel
import tech.dojo.pay.sdk.card.presentation.threeds.Dojo3DSViewModelHost

@Suppress("SwallowedException")
internal class DojoGPayActivity :
    AppCompatActivity(),
    Dojo3DSViewModelHost {

    private val viewModel: DojoGPayViewModel by viewModels {
        DojoGPayViewModelFactory(intent.extras, this)
    }
    override val threeDSViewModel: Dojo3DSBaseViewModel by lazy { viewModel }

    private val gPayEngine: DojoGPayEngine by lazy { DojoGPayEngine(this) }

    private val paymentDataLauncher = registerForActivityResult(
        TaskResultContracts.GetPaymentDataResult()
    ) { result -> handlePaymentDataResult(result) }

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
        viewModel.deviceData.observe(this) { deviceData -> viewModel.initCardinal() }
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
            ?.addOnCompleteListener { task -> paymentDataLauncher.launch(task) }
    }

    private fun handlePaymentDataResult(result: ApiTaskResult<PaymentData>) {
        val paymentData = result.result
        if (paymentData != null) {
            handlePaymentSuccess(paymentData)
        } else if (result.status.isSuccess) {
            returnResult(DojoPaymentResult.SDK_INTERNAL_ERROR)
        } else {
            if (!result.status.isCanceled) {
                Log.d("GPay Failed", result.status.toString())
            }
            returnResult(DojoPaymentResult.FAILED)
        }
    }

    private fun handlePaymentSuccess(paymentData: PaymentData) {
        try {
            viewModel.handlePaymentSuccessFromGpay(
                gPayData = paymentData.toJson(),
                dojoGPayParams = params
            )
        } catch (e: JSONException) {
            returnResult(DojoPaymentResult.SDK_INTERNAL_ERROR)
        }
    }

    private fun navigate3DS(params: ThreeDSParams) {
        try {
            viewModel.configureDCardinalInstance.cca_continue(
                params.md,
                params.jwt,
                this
            ) { _, validateResponse, serverJWT ->
                viewModel.on3dsCompleted(
                    serverJWT,
                    params.md,
                    validateResponse
                )
            }
        } catch (throwable: Throwable) {
            viewModel.on3dsCompleted()
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
