package tech.dojo.pay.sdk.card.data

import com.google.gson.Gson
import org.json.JSONObject
import tech.dojo.pay.sdk.card.data.entities.GPayDetails
import tech.dojo.pay.sdk.card.entities.DojoGPayParams
import tech.dojo.pay.sdk.card.entities.GooglePayAddressDetails

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal class GpayPaymentRequestMapper(
    private val gson: Gson
) {
    fun apply(
        paymentInformationJson: String,
        dojoGPayParams: DojoGPayParams
    ): GPayDetails {
        return GPayDetails(
            token = getGpayToken(paymentInformationJson),
            email = getEmail(paymentInformationJson, dojoGPayParams),
            phoneNumber = getPhoneNumber(paymentInformationJson, dojoGPayParams),
            billingContact = getBillingAddress(paymentInformationJson, dojoGPayParams),
            shippingContact = getShippingAddress(paymentInformationJson, dojoGPayParams)
        )
    }

    private fun getGpayToken(
        paymentInformationJson: String,
    ): String? {
        return try {
            JSONObject(paymentInformationJson).getJSONObject("paymentMethodData")
                .getJSONObject("tokenizationData")
                .getString("token")
        } catch (e: Exception) {
            null
        }
    }

    private fun getEmail(
        paymentInformationJson: String,
        dojoGPayParams: DojoGPayParams
    ): String? {
        return try {
            if (dojoGPayParams.dojoGPayPayload.dojoGPayConfig.collectEmailAddress) {
                JSONObject(paymentInformationJson).getString("email")
            } else {
                dojoGPayParams.dojoGPayPayload.email
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getPhoneNumber(
        paymentInformationJson: String,
        dojoGPayParams: DojoGPayParams
    ): String? {
        return try {
            if (
                dojoGPayParams.dojoGPayPayload.dojoGPayConfig.collectPhoneNumber &&
                dojoGPayParams.dojoGPayPayload.dojoGPayConfig.collectShipping
            ) {
                JSONObject(paymentInformationJson).getJSONObject("shippingAddress")
                    .getString("phoneNumber")
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getBillingAddress(
        paymentInformationJson: String,
        dojoGPayParams: DojoGPayParams
    ): GooglePayAddressDetails? {
        return try {
            if (dojoGPayParams.dojoGPayPayload.dojoGPayConfig.collectBilling) {
                val billingAddressJson =
                    JSONObject(paymentInformationJson).getJSONObject("paymentMethodData")
                        .getJSONObject("info").getJSONObject("billingAddress")
                        .toString()
                gson.fromJson(billingAddressJson, GooglePayAddressDetails::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getShippingAddress(
        paymentInformationJson: String,
        dojoGPayParams: DojoGPayParams
    ): GooglePayAddressDetails? {
        return try {
            if (dojoGPayParams.dojoGPayPayload.dojoGPayConfig.collectShipping) {
                val shippingAddressJson =
                    JSONObject(paymentInformationJson).getJSONObject("shippingAddress")
                        .toString()
                gson.fromJson(shippingAddressJson, GooglePayAddressDetails::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
