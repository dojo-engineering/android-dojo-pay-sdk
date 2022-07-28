package tech.dojo.pay.sdksample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.DojoCardDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad.FullCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad.SavedCardPaymentPayLoad
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent
import tech.dojo.pay.sdk.card.entities.DojoTotalAmount
import tech.dojo.pay.sdksample.databinding.ActivityCardPaymentBinding
import tech.dojo.pay.sdksample.token.TokenGenerator

abstract class CardPaymentBaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCardPaymentBinding

    abstract fun onSandboxChecked(isChecked: Boolean)

    abstract fun onPayClicked(token: String, payload: FullCardPaymentPayload)
    abstract fun onPaySavedCardClicked(token: String, payload: SavedCardPaymentPayLoad)

    abstract fun onGPayClicked(
        dojoGPayPayload: DojoGPayPayload,
        dojoPaymentIntent: DojoPaymentIntent
    )

    fun showResult(result: DojoPaymentResult) {
        showDialog(
            title = "Payment result",
            message = "${result.name} (${result.code})"
        )
        displayToken("")
    }

    fun setProgressIndicatorVisible(visible: Boolean) {
        binding.viewProgress.isVisible = visible
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCardDetails(Cards.ThreeDSV2)
        setCardListeners()
        setTokenListener()

        binding.btnPay.setOnClickListener {
            val (month, year) = binding.expiryDate.text.toString().split("/")

            onPayClicked(
                token = binding.token.text.toString(),
                payload = FullCardPaymentPayload(
                    DojoCardDetails(
                        cardNumber = binding.cardNumber.text.toString(),
                        cardName = binding.cardHolder.text.toString(),
                        expiryMonth = month,
                        expiryYear = year,
                        cv2 = binding.securityCode.text.toString()
                    )
                )
            )
        }
        binding.btnPaySavedCard.setOnClickListener {
            onPaySavedCardClicked(
                token = binding.token.text.toString(),
                payload = SavedCardPaymentPayLoad(
                    cv2 = "020",
                    paymentMethodId = "pm_6qTon7QGRK_7y2kFOmrbag"
                )
            )
        }
        DojoSdk.isGpayAvailable(
            this,
            DojoGPayConfig(
                collectShipping = binding.checkboxShippingAddress.isChecked,
                collectBilling = binding.checkboxBillingAddress.isChecked,
                collectPhoneNumber = binding.checkboxPhoneNumber.isChecked,
                collectEmailAddress = binding.checkboxEmail.isChecked,
                merchantName = "Dojo Cafe (Paymentsense)",
                merchantId = "BCR2DN6T57R5ZI34",
                gatewayMerchantId = "119784244252745"
            ),
            { binding.btnGPay.googlePayButton.visibility = View.VISIBLE },
            { binding.btnGPay.googlePayButton.visibility = View.GONE }
        )
        binding.btnGPay.googlePayButton.setOnClickListener {
            onGPayClicked(
                dojoGPayPayload = DojoGPayPayload(
                    DojoGPayConfig(
                        collectShipping = binding.checkboxShippingAddress.isChecked,
                        allowedCountryCodesForShipping = if (binding.checkboxShippingAddress.isChecked) {
                            listOf("US", "GB", "DE")
                        } else null,
                        collectBilling = binding.checkboxBillingAddress.isChecked,
                        collectPhoneNumber = binding.checkboxPhoneNumber.isChecked,
                        collectEmailAddress = binding.checkboxEmail.isChecked,
                        merchantName = "Dojo Cafe (Paymentsense)",
                        merchantId = "BCR2DN6T57R5ZI34",
                        gatewayMerchantId = "119784244252745"
                    )
                ),
                dojoPaymentIntent = DojoPaymentIntent(
                    token = binding.token.text.toString(),
                    totalAmount = DojoTotalAmount(10, "GBP")
                )
            )
        }
    }

    private fun setCardListeners() {
        binding.btn3DSV2.setOnClickListener {
            setCardDetails(Cards.ThreeDSV2)
        }

        binding.btn3DSV1.setOnClickListener {
            setCardDetails(Cards.ThreeDSV1)
        }

        binding.btnNo3DS.setOnClickListener {
            setCardDetails(Cards.NoThreeDS)
        }
    }

    private fun setTokenListener() {
        DojoSdk.sandbox = binding.checkboxSandbox.isChecked

        binding.checkboxSandbox.setOnCheckedChangeListener { _, isChecked ->
            binding.btnGenerateToken.visibility = if (isChecked) View.VISIBLE else View.GONE
            displayToken("")
            onSandboxChecked(isChecked)
        }

        binding.btnGenerateToken.setOnClickListener {
            lifecycleScope.launch {
                showLoading()
                try {
                    displayToken(TokenGenerator.generateToken())
                } catch (e: Throwable) {
                    showTokenError(e)
                } finally {
                    hidLoading()
                }
            }
        }
    }

    private fun showLoading() {
        binding.viewProgress.visibility = View.VISIBLE
        binding.btnGPay.googlePayButton.isEnabled = false
        binding.btnPay.isEnabled = false
    }

    private fun hidLoading() {
        binding.viewProgress.visibility = View.GONE
        binding.btnGPay.googlePayButton.isEnabled = true
        binding.btnPay.isEnabled = true
    }

    private fun setCardDetails(details: DojoCardDetails) {
        binding.cardNumber.setText(details.cardNumber)
        binding.cardHolder.setText(details.cardName)
        binding.expiryDate.setText("${details.expiryMonth}/${details.expiryYear}")
        binding.securityCode.setText(details.cv2)
    }

    private fun showTokenError(e: Throwable) {
        binding.viewProgress.visibility = View.GONE
        showDialog(
            title = "Can't generate token",
            message = e.message ?: ""
        )
    }

    private fun displayToken(token: String) {
        binding.token.setText(token)
        binding.token.visibility = View.VISIBLE
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .create()
            .show()
    }
}
