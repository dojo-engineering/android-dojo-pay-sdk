package tech.dojo.pay.sdk.card.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import tech.dojo.pay.sdk.card.DojoCardPaymentViewModel
import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.data.entities.PaymentDetails
import tech.dojo.pay.sdk.card.entities.DojoAddressDetails
import tech.dojo.pay.sdk.card.entities.DojoCardDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult
import tech.dojo.pay.sdk.card.entities.DojoShippingDetails
import tech.dojo.pay.sdk.card.entities.PaymentResult
import java.lang.IllegalArgumentException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class CardPaymentRepositoryTest {

    @Mock
    lateinit var api: CardPaymentApi

    private lateinit var repo: CardPaymentRepository

    @Before
    fun setup() {
        repo = CardPaymentRepository(api, TOKEN, PAYLOAD)
    }

    @Test
    fun `device data collected`() = runTest {
        whenever(api.collectDeviceData(any(), any())).thenReturn(DeviceData("action", "token"))
        repo.collectDeviceData()
        verify(api).collectDeviceData(
            token = TOKEN,
            payload = PaymentDetails(
                cardNumber = CARD_DETAILS.cardNumber,
                cardName = CARD_DETAILS.cardName,
                expiryDate = "${CARD_DETAILS.expiryMonth} / ${CARD_DETAILS.expiryYear}",
                cV2 = CARD_DETAILS.cv2,
                userEmailAddress = PAYLOAD.userEmailAddress,
                userPhoneNumber = PAYLOAD.userPhoneNumber,
                billingAddress = ADDRESS_DETAILS,
                shippingDetails = SHIPPING_DETAILS,
                metaData = PAYLOAD.metaData
            )
        )
    }

    private companion object {
        const val TOKEN = "TOKEN"

        val CARD_DETAILS = DojoCardDetails(
            cardNumber = "4456530000001096",
            cardName = "Card holder",
            expiryMonth = "12",
            expiryYear = "24",
            cv2 = "020"
        )

        val ADDRESS_DETAILS = DojoAddressDetails(
            address1 = "Address",
            city = "City",
            state = "State",
            postcode = "Postcode",
            countryCode = "Country"
        )

        val SHIPPING_DETAILS = DojoShippingDetails(
            name = "name",
            address = ADDRESS_DETAILS
        )

        val PAYLOAD = DojoCardPaymentPayload(
            cardDetails = CARD_DETAILS,
            userEmailAddress = "user@gmail.com",
            userPhoneNumber = "123456789",
            billingAddress = ADDRESS_DETAILS,
            shippingDetails = SHIPPING_DETAILS,
            metaData = mapOf("1" to "one")
        )
    }
}