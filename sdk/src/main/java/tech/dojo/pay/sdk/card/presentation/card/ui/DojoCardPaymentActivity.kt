package tech.dojo.pay.sdk.card.presentation.card.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.R
import tech.dojo.pay.sdk.card.DojoCardPaymentResultContract
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams
import tech.dojo.pay.sdk.card.presentation.card.viewmodel.DojoCardPaymentViewModel
import tech.dojo.pay.sdk.card.presentation.card.viewmodel.DojoCardPaymentViewModelFactory
import tech.dojo.pay.sdk.card.presentation.threeds.Dojo3DSBaseViewModel
import tech.dojo.pay.sdk.card.presentation.threeds.Dojo3DSViewModelHost

@Suppress("SwallowedException")
internal class DojoCardPaymentActivity : AppCompatActivity(), Dojo3DSViewModelHost {

    private val viewModel: DojoCardPaymentViewModel by viewModels {
        DojoCardPaymentViewModelFactory(intent.extras, this)
    }
    override val threeDSViewModel: Dojo3DSBaseViewModel by lazy { viewModel }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dojo_card_payment)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        observeDeviceData()
        observeResult()
    }

    private fun observeDeviceData() {
        viewModel.deviceData.observe(this) { viewModel.initCardinal() }
    }

    private fun observeResult() {
        viewModel.paymentResult.observe(this) { result ->
            when (result) {
                is PaymentResult.Completed -> returnResult(result.value)
                is PaymentResult.ThreeDSRequired -> navigate3DS(result.params)
            }
        }
    }

    private fun returnResult(result: DojoPaymentResult) {
        val data = Intent()
        data.putExtra(DojoCardPaymentResultContract.KEY_RESULT, result)
        setResult(RESULT_OK, data)
        finish()
        overridePendingTransition(0, R.anim.exit)
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
                    validateResponse,
                )
            }
        } catch (throwable: Throwable) {
            viewModel.on3dsCompleted()
        }
    }

    override fun onBackPressed() {
        if (viewModel.canExit) super.onBackPressed()
    }
}
