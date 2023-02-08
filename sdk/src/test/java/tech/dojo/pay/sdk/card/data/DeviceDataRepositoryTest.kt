package tech.dojo.pay.sdk.card.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.data.entities.PaymentDetails
import tech.dojo.pay.sdk.card.data.mappers.CardPaymentRequestMapper
import tech.dojo.pay.sdk.card.data.remote.cardpayment.CardPaymentApi
import tech.dojo.pay.sdk.card.entities.DojoAddressDetails
import tech.dojo.pay.sdk.card.entities.DojoCardDetails
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad.FullCardPaymentPayload
import tech.dojo.pay.sdk.card.entities.DojoShippingDetails

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)

internal class DeviceDataRepositoryTest {

    @Mock
    lateinit var api: CardPaymentApi

    @Mock
    lateinit var requestMapper: CardPaymentRequestMapper

    private lateinit var repo: DeviceDataRepository

    @Test
    fun `calling collectDeviceData should call collectDeviceData from api`() = runTest {
        // arrange
        val paymentDetails = PaymentDetails(
            cardNumber = CARD_DETAILS.cardNumber,
            cardName = CARD_DETAILS.cardName,
            expiryDate = "${CARD_DETAILS.expiryMonth} / ${CARD_DETAILS.expiryYear}",
            cV2 = CARD_DETAILS.cv2,
            userEmailAddress = FULL_CARD_PAYLOAD.userEmailAddress,
            userPhoneNumber = FULL_CARD_PAYLOAD.userPhoneNumber,
            billingAddress = ADDRESS_DETAILS,
            shippingDetails = SHIPPING_DETAILS,
            metaData = FULL_CARD_PAYLOAD.metaData
        )
        whenever(requestMapper.mapToPaymentDetails(any())).thenReturn(paymentDetails)
        whenever(api.collectDeviceData(any(), any(), any())).thenReturn(
            DeviceData(
                "action",
                "token"
            )
        )
        // act
        repo = DeviceDataRepository(api, TOKEN, requestMapper)
        repo.collectDeviceData(FULL_CARD_PAYLOAD)
        // assert
        verify(api).collectDeviceData(
            token = TOKEN,
            payload = paymentDetails
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

        val FULL_CARD_PAYLOAD = FullCardPaymentPayload(
            cardDetails = CARD_DETAILS,
            userEmailAddress = "user@gmail.com",
            userPhoneNumber = "123456789",
            billingAddress = ADDRESS_DETAILS,
            shippingDetails = SHIPPING_DETAILS,
            metaData = mapOf("1" to "one")
        )
    }
}