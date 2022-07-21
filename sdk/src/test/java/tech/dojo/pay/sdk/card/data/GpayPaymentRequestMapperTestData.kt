package tech.dojo.pay.sdk.card.data

import com.google.gson.Gson
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.entities.DojoGPayParams
import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent
import tech.dojo.pay.sdk.card.entities.DojoTotalAmount
import tech.dojo.pay.sdk.card.entities.GooglePayAddressDetails

internal object GpayPaymentRequestMapperTestData {

    private val dojoGPayConfig: DojoGPayConfig = DojoGPayConfig(
        collectShipping = true,
        collectBilling = true,
        collectEmailAddress = true,
        collectPhoneNumber = true,
        merchantName = "test",
        merchantId = "test",
        gatewayMerchantId = "test",
        allowedCountryCodesForShipping = listOf("us", "GP")
    )
    val dojoGPayPayload = DojoGPayPayload(
        dojoGPayConfig = dojoGPayConfig,
        email = "testEmailFormDojoGPayPayload",
    )
    private val dojoPaymentIntent = DojoPaymentIntent(
        token = "",
        totalAmount = DojoTotalAmount(0L, "")
    )
    val dojoGPayParams = DojoGPayParams(
        dojoGPayPayload = dojoGPayPayload,
        dojoPaymentIntent = dojoPaymentIntent
    )
    val paymentInformationFullJson = "{\n" +
            "  \"apiVersion\": 2,\n" +
            "  \"apiVersionMinor\": 0,\n" +
            "  \"email\": \"testEmail\",\n" +
            "  \"paymentMethodData\": {\n" +
            "    \"description\": \"test\",\n" +
            "    \"info\": {\n" +
            "      \"assuranceDetails\": {\n" +
            "        \"accountVerified\": true,\n" +
            "        \"cardHolderAuthenticated\": true\n" +
            "      },\n" +
            "      \"billingAddress\": {\n" +
            "        \"address1\": \"Dubai\",\n" +
            "        \"address2\": \"\",\n" +
            "        \"address3\": \"\",\n" +
            "        \"administrativeArea\": \"Dubaiّ\",\n" +
            "        \"countryCode\": \"AE\",\n" +
            "        \"locality\": \"\",\n" +
            "        \"name\": \"testName\",\n" +
            "        \"postalCode\": \"\",\n" +
            "        \"sortingCode\": \"\"\n" +
            "      },\n" +
            "      \"cardDetails\": \"4674\",\n" +
            "      \"cardNetwork\": \"MASTERCARD\"\n" +
            "    },\n" +
            "    \"tokenizationData\": {\n" +
            "      \"token\": \"testToken\",\n" +
            "      \"type\": \"PAYMENT_GATEWAY\"\n" +
            "    },\n" +
            "    \"type\": \"CARD\"\n" +
            "  },\n" +
            "  \"shippingAddress\": {\n" +
            "    \"address1\": \"Kurfürstenstraße\",\n" +
            "    \"address2\": \"\",\n" +
            "    \"address3\": \"\",\n" +
            "    \"administrativeArea\": \"\",\n" +
            "    \"countryCode\": \"DE\",\n" +
            "    \"locality\": \"Berlin\",\n" +
            "    \"name\": \"TestName\",\n" +
            "    \"phoneNumber\": \"testPhone\",\n" +
            "    \"postalCode\": \"10787\",\n" +
            "    \"sortingCode\": \"\"\n" +
            "  }\n" +
            "}"
    val billingAddressJson =
        "{\"address3\":\"\",\"sortingCode\":\"\",\"address2\":\"\",\"address1\":\"Dubai\",\"countryCode\":\"AE\",\"postalCode\":\"\",\"locality\":\"\",\"name\":\"testName\",\"administrativeArea\":\"Dubaiّ\"}"
    val billingContact =
        Gson().fromJson(billingAddressJson, GooglePayAddressDetails::class.java)

    val shippingAddressJson =
        "{\"phoneNumber\":\"testPhone\",\"address3\":\"\",\"sortingCode\":\"\",\"address2\":\"\",\"address1\":\"Kurfürstenstraße\",\"countryCode\":\"DE\",\"postalCode\":\"10787\",\"locality\":\"Berlin\",\"name\":\"TestName\",\"administrativeArea\":\"\"}"
    val shippingContact = Gson().fromJson(shippingAddressJson, GooglePayAddressDetails::class.java)

    val paymentInformationJsonNoData = "{\n" +
            "  \"apiVersion\": 2,\n" +
            "  \"apiVersionMinor\": 0,\n" +
            "  \"paymentMethodData\": {\n" +
            "    \"description\": \"Mastercard •••• 4674\",\n" +
            "    \"info\": {\n" +
            "      \"assuranceDetails\": {\n" +
            "        \"accountVerified\": true,\n" +
            "        \"cardHolderAuthenticated\": true\n" +
            "      },\n" +
            "      \"cardDetails\": \"4674\",\n" +
            "      \"cardNetwork\": \"MASTERCARD\"\n" +
            "    },\n" +
            "    \"tokenizationData\": {\n" +
            "      \"type\": \"PAYMENT_GATEWAY\"\n" +
            "    },\n" +
            "    \"type\": \"CARD\"\n" +
            "  }\n" +
            "}"
}
