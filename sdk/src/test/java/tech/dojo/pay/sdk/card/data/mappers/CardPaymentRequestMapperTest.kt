package tech.dojo.pay.sdk.card.data.mappers

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import tech.dojo.pay.sdk.card.data.entities.PaymentDetails
import tech.dojo.pay.sdk.card.entities.DojoAddressDetails
import tech.dojo.pay.sdk.card.entities.DojoCardDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.sdk.card.entities.DojoShippingDetails

@RunWith(MockitoJUnitRunner::class)
internal class CardPaymentRequestMapperTest {

    @Test
    fun `calling mapToPaymentDetails with FullCardPaymentPayload should return full PaymentDetails`() {
        val actual = CardPaymentRequestMapper().mapToPaymentDetails(FULL_CARD_PAYLOAD)
        Assert.assertEquals(paymentDetails, actual)
    }

    @Test
    fun `calling mapToPaymentDetails with SAVED_CARD_PAYLOAD should return PaymentDetails for saved card `() {
        val paymentDetailsForSavedCard = PaymentDetails(
            cV2 = "cvv",
            paymentMethodId = "paymentMethodId",
            userEmailAddress = "user@gmail.com",
            userPhoneNumber = "123456789",
            shippingDetails = SHIPPING_DETAILS,
            metaData = mapOf("1" to "one"),
        )
        val actual = CardPaymentRequestMapper().mapToPaymentDetails(SAVED_CARD_PAYLOAD)
        Assert.assertEquals(paymentDetailsForSavedCard, actual)
    }
    private companion object {

        val CARD_DETAILS = DojoCardDetails(
            cardNumber = "4456530000001096",
            cardName = "Card holder",
            expiryMonth = "12",
            expiryYear = "24",
            cv2 = "020",
            mitConsentGiven = true,
        )

        val ADDRESS_DETAILS = DojoAddressDetails(
            address1 = "Address",
            city = "City",
            state = "State",
            postcode = "Postcode",
            countryCode = "Country",
        )

        val SHIPPING_DETAILS = DojoShippingDetails(
            name = "name",
            address = ADDRESS_DETAILS,
        )

        val FULL_CARD_PAYLOAD = DojoCardPaymentPayLoad.FullCardPaymentPayload(
            cardDetails = CARD_DETAILS,
            userEmailAddress = "user@gmail.com",
            userPhoneNumber = "123456789",
            billingAddress = ADDRESS_DETAILS,
            shippingDetails = SHIPPING_DETAILS,
            metaData = mapOf("1" to "one"),
        )
        val SAVED_CARD_PAYLOAD = DojoCardPaymentPayLoad.SavedCardPaymentPayLoad(
            cv2 = "cvv",
            paymentMethodId = "paymentMethodId",
            userEmailAddress = "user@gmail.com",
            userPhoneNumber = "123456789",
            shippingDetails = SHIPPING_DETAILS,
            metaData = mapOf("1" to "one"),
        )
        val paymentDetails = PaymentDetails(
            cardNumber = CARD_DETAILS.cardNumber,
            cardName = CARD_DETAILS.cardName,
            expiryDate = "${CARD_DETAILS.expiryMonth} / ${CARD_DETAILS.expiryYear}",
            cV2 = CARD_DETAILS.cv2,
            userEmailAddress = FULL_CARD_PAYLOAD.userEmailAddress,
            userPhoneNumber = FULL_CARD_PAYLOAD.userPhoneNumber,
            billingAddress = ADDRESS_DETAILS,
            shippingDetails = SHIPPING_DETAILS,
            metaData = FULL_CARD_PAYLOAD.metaData,
            mitConsentGiven = true,
        )
    }
}
