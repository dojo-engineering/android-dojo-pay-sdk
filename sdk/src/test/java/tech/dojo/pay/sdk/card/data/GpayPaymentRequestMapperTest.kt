package tech.dojo.pay.sdk.card.data

import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tech.dojo.pay.sdk.card.data.GpayPaymentRequestMapperTestData.billingAddressJson
import tech.dojo.pay.sdk.card.data.GpayPaymentRequestMapperTestData.billingContact
import tech.dojo.pay.sdk.card.data.GpayPaymentRequestMapperTestData.dojoGPayPayload
import tech.dojo.pay.sdk.card.data.GpayPaymentRequestMapperTestData.shippingAddressJson
import tech.dojo.pay.sdk.card.data.GpayPaymentRequestMapperTestData.shippingContact
import tech.dojo.pay.sdk.card.data.entities.GPayDetails
import tech.dojo.pay.sdk.card.entities.GooglePayAddressDetails

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class GpayPaymentRequestMapperTest {

    private val gsoMock: Gson = mock()
    private val mapper = GpayPaymentRequestMapper(gsoMock)

    @Test
    fun `calling apply with all parameters enabled on the dojoGPayConfig should return full GPayDetails`() {
        // arrange
        val gPayParams = GpayPaymentRequestMapperTestData.dojoGPayParams
        val paymentInformationJson = GpayPaymentRequestMapperTestData.paymentInformationFullJson
        whenever(
            gsoMock.fromJson(
                billingAddressJson,
                GooglePayAddressDetails::class.java
            )
        ).thenReturn(billingContact)
        whenever(
            gsoMock.fromJson(
                shippingAddressJson,
                GooglePayAddressDetails::class.java
            )
        ).thenReturn(shippingContact)

        val expected = GPayDetails(
            token = "testToken",
            email = "testEmail",
            phoneNumber = "testPhone",
            billingContact = billingContact,
            shippingContact = shippingContact
        )
        // act
        val result = mapper.apply(paymentInformationJson, gPayParams)
        //assert
        Assert.assertEquals(expected, result)
    }

    @Test
    fun `calling apply with false email parameters  on the dojoGPayConfig should return GPayDetails with email from dojoGPayPayload`() {
        // arrange
        val gPayParams = GpayPaymentRequestMapperTestData.dojoGPayParams.copy(
            dojoGPayPayload = dojoGPayPayload.copy(
                dojoGPayConfig = GpayPaymentRequestMapperTestData.dojoGPayParams.dojoGPayPayload.dojoGPayConfig.copy(
                    collectEmailAddress = false
                )
            )
        )
        val paymentInformationJson = GpayPaymentRequestMapperTestData.paymentInformationFullJson
        whenever(
            gsoMock.fromJson(
                billingAddressJson,
                GooglePayAddressDetails::class.java
            )
        ).thenReturn(billingContact)
        whenever(
            gsoMock.fromJson(
                shippingAddressJson,
                GooglePayAddressDetails::class.java
            )
        ).thenReturn(shippingContact)

        val expected = GPayDetails(
            token = "testToken",
            email = "testEmailFormDojoGPayPayload",
            phoneNumber = "testPhone",
            billingContact = billingContact,
            shippingContact = shippingContact
        )
        // act
        val result = mapper.apply(paymentInformationJson, gPayParams)
        //assert
        Assert.assertEquals(expected, result)
    }

    @Test
    fun `calling apply with false collectPhoneNumber parameters  on the dojoGPayConfig should return GPayDetails with null PhoneNumber`() {
        // arrange
        val gPayParams = GpayPaymentRequestMapperTestData.dojoGPayParams.copy(
            dojoGPayPayload = dojoGPayPayload.copy(
                dojoGPayConfig = GpayPaymentRequestMapperTestData.dojoGPayParams.dojoGPayPayload.dojoGPayConfig.copy(
                    collectPhoneNumber = false
                )
            )
        )
        val paymentInformationJson = GpayPaymentRequestMapperTestData.paymentInformationFullJson
        whenever(
            gsoMock.fromJson(
                billingAddressJson,
                GooglePayAddressDetails::class.java
            )
        ).thenReturn(billingContact)
        whenever(
            gsoMock.fromJson(
                shippingAddressJson,
                GooglePayAddressDetails::class.java
            )
        ).thenReturn(shippingContact)

        val expected = GPayDetails(
            token = "testToken",
            email = "testEmail",
            phoneNumber = null,
            billingContact = billingContact,
            shippingContact = shippingContact
        )
        // act
        val result = mapper.apply(paymentInformationJson, gPayParams)
        //assert
        Assert.assertEquals(expected, result)
    }

    @Test
    fun `calling apply with false collectShipping parameters  on the dojoGPayConfig should return GPayDetails with null PhoneNumber and shipping`() {
        // arrange
        val gPayParams = GpayPaymentRequestMapperTestData.dojoGPayParams.copy(
            dojoGPayPayload = dojoGPayPayload.copy(
                dojoGPayConfig = GpayPaymentRequestMapperTestData.dojoGPayParams.dojoGPayPayload.dojoGPayConfig.copy(
                    collectShipping = false
                )
            )
        )
        val paymentInformationJson = GpayPaymentRequestMapperTestData.paymentInformationFullJson
        whenever(
            gsoMock.fromJson(
                billingAddressJson,
                GooglePayAddressDetails::class.java
            )
        ).thenReturn(billingContact)

        val expected = GPayDetails(
            token = "testToken",
            email = "testEmail",
            phoneNumber = null,
            billingContact = billingContact,
            shippingContact = null
        )
        // act
        val result = mapper.apply(paymentInformationJson, gPayParams)
        //assert
        Assert.assertEquals(expected, result)
    }

    @Test
    fun `calling apply with false collectBilling parameters  on the dojoGPayConfig should return GPayDetails with null PhoneNumber and shipping`() {
        // arrange
        val gPayParams = GpayPaymentRequestMapperTestData.dojoGPayParams.copy(
            dojoGPayPayload = dojoGPayPayload.copy(
                dojoGPayConfig = GpayPaymentRequestMapperTestData.dojoGPayParams.dojoGPayPayload.dojoGPayConfig.copy(
                    collectBilling = false
                )
            )
        )
        val paymentInformationJson = GpayPaymentRequestMapperTestData.paymentInformationFullJson
        whenever(
            gsoMock.fromJson(
                shippingAddressJson,
                GooglePayAddressDetails::class.java
            )
        ).thenReturn(shippingContact)
        val expected = GPayDetails(
            token = "testToken",
            email = "testEmail",
            phoneNumber = "testPhone",
            billingContact = null,
            shippingContact = shippingContact
        )
        // act
        val result = mapper.apply(paymentInformationJson, gPayParams)
        //assert
        Assert.assertEquals(expected, result)
    }

    @Test
    fun `calling apply with all parameters enabled but no data  on the dojoGPayConfig should return PayDetails with null values`() {
        // arrange
        val gPayParams = GpayPaymentRequestMapperTestData.dojoGPayParams
        val paymentInformationJson = GpayPaymentRequestMapperTestData.paymentInformationJsonNoData

        val expected = GPayDetails(
            token = null,
            email = null,
            phoneNumber = null,
            billingContact = null,
            shippingContact = null
        )
        // act
        val result = mapper.apply(paymentInformationJson, gPayParams)
        //assert
        Assert.assertEquals(expected, result)
    }
}