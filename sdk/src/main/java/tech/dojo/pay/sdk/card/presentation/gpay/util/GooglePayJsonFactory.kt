package tech.dojo.pay.sdk.card.presentation.gpay.util

import android.app.Activity
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.entities.DojoTotalAmount
import tech.dojo.pay.sdk.card.presentation.gpay.util.GPayConstants.CENTS
import tech.dojo.pay.sdk.card.presentation.gpay.util.GPayConstants.PAYMENT_GATEWAY_TOKENIZATION_NAME
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Contains helper static methods for dealing with the Payments API.
 *
 * Many of the parameters used in the code are optional and are set here merely to call out their
 * existence. Please consult the documentation to learn more and feel free to remove ones not
 * relevant to your implementation.
 */
@Suppress("SwallowedException")
object GooglePayJsonFactory {
    /**
     * Create a Google Pay API base request object with properties used in all requests.
     *
     * @return Google Pay API base request object.
     * @throws JSONException
     */
    private val baseRequest = JSONObject().apply {
        put("apiVersion", 2)
        put("apiVersionMinor", 0)
    }

    /**
     * Card authentication methods supported by your app and your gateway.
     *
     *
     * and make updates in Constants.java.
     *
     * @return Allowed card authentication methods.
     * @see [CardParameters](https://developers.google.com/pay/api/android/reference/object.CardParameters)
     */
    private val allowedCardAuthMethods = JSONArray(GPayConstants.SUPPORTED_METHODS)

    /**
     * return the Json  object for the is ready to pay request
     */
    fun getReadyToPayRequest(dojoGPayConfig: DojoGPayConfig): JSONObject? {
        return try {
            baseRequest.apply {
                put(
                    "allowedPaymentMethods",
                    JSONArray().put(baseCardPaymentMethod(dojoGPayConfig))
                )
            }
        } catch (e: JSONException) {
            null
        }
    }

    /**
     * Describe your app's support for the CARD payment method.
     *
     *
     * The provided properties are applicable to both an IsReadyToPayRequest and a
     * PaymentDataRequest.
     *
     * @return A CARD PaymentMethod object describing accepted cards.
     * @throws JSONException
     * @see [PaymentMethod](https://developers.google.com/pay/api/android/reference/object.PaymentMethod)
     */
    private fun baseCardPaymentMethod(dojoGPayConfig: DojoGPayConfig): JSONObject {
        return JSONObject().apply {
            put("type", "CARD")
            put(
                "parameters",
                createPaymentParamsJson(dojoGPayConfig.collectBilling, dojoGPayConfig.allowedCardNetworks)
            )
            put(
                "tokenizationSpecification",
                getTokenizationSpecification(dojoGPayConfig.gatewayMerchantId)
            )
        }
    }

    private fun createPaymentParamsJson(
        collectBilling: Boolean,
        allowedCardNetworks: List<CardsSchemes>
    ): JSONObject {
        val supportedCards = allowedCardNetworks.map { it.cardsSchemes }
        return JSONObject()
            .apply {
                put("allowedAuthMethods", allowedCardAuthMethods)
                put("allowedCardNetworks", JSONArray(supportedCards))
                if (collectBilling) {
                    put("billingAddressRequired", true)
                    put(
                        "billingAddressParameters",
                        JSONObject()
                            .put("format", "FULL")
                    )
                }
            }
    }

    private fun getTokenizationSpecification(gatewayMerchantId: String): JSONObject {
        return JSONObject()
            .put("type", "PAYMENT_GATEWAY")
            .put("parameters", JSONObject().put("gateway", PAYMENT_GATEWAY_TOKENIZATION_NAME).put("gatewayMerchantId", gatewayMerchantId))
    }

    /**
     * Creates an instance of [PaymentsClient] for use in an [Activity] using the
     * environment and theme set in [GPayConstants].
     *
     * @param activity is the caller's activity.
     */
    fun createPaymentsClient(activity: Activity): PaymentsClient {
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(getGpayEnvironment())
            .build()

        return Wallet.getPaymentsClient(activity, walletOptions)
    }

    /**
     * returns environment based on the sandBox status if it's on then it will return test environment
     * off it will return prod environment
     */
    private fun getGpayEnvironment() = when {
        DojoSdk.walletSandBox -> WalletConstants.ENVIRONMENT_TEST
        else -> WalletConstants.ENVIRONMENT_PRODUCTION
    }

    /**
     * Provide Google Pay API with a payment amount, currency, and amount status.
     *
     * @return information about the requested payment.
     * @throws JSONException
     * @see [TransactionInfo](https://developers.google.com/pay/api/android/reference/object.TransactionInfo)
     */
    @Throws(JSONException::class)
    private fun getTransactionInfo(price: String, currencyCode: String): JSONObject {
        return JSONObject().apply {
            put("totalPrice", price)
            put("totalPriceStatus", "FINAL")
            put("currencyCode", currencyCode)
        }
    }

    /**
     * An object describing information requested in a Google Pay payment sheet
     *
     * @return Payment data expected by your app.
     * @see [PaymentDataRequest](https://developers.google.com/pay/api/android/reference/object.PaymentDataRequest)
     */
    fun getPaymentDataRequest(
        totalAmountPayload: DojoTotalAmount,
        dojoGPayConfig: DojoGPayConfig
    ): JSONObject? {
        return try {
            baseRequest.apply {
                put(
                    "allowedPaymentMethods",
                    JSONArray().put(
                        baseCardPaymentMethod(dojoGPayConfig)
                    )
                )
                put(
                    "transactionInfo",
                    getTransactionInfo(
                        totalAmountPayload.amount.centsToString(),
                        totalAmountPayload.currencyCode
                    )
                )
                put("emailRequired", dojoGPayConfig.collectEmailAddress)

                put(
                    "merchantInfo",
                    JSONObject()
                        .put("merchantName", dojoGPayConfig.merchantName)
                        .put("merchantId", dojoGPayConfig.merchantId)
                )

                if (dojoGPayConfig.collectShipping) {
                    put("shippingAddressRequired", true)
                    put(
                        "shippingAddressParameters",
                        JSONObject().apply {
                            put("phoneNumberRequired", dojoGPayConfig.collectPhoneNumber)
                            put(
                                "allowedCountryCodes",
                                JSONArray(dojoGPayConfig.allowedCountryCodesForShipping)
                            )
                        }
                    )
                }
            }
        } catch (e: JSONException) {
            null
        }
    }
}

/**
 * Converts Long to a string format accepted by [GooglePayJsonFactory.getPaymentDataRequest].
 */
fun Long.centsToString() = BigDecimal(this)
    .divide(CENTS)
    .setScale(2, RoundingMode.HALF_EVEN)
    .toString()
