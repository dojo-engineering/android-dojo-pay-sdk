package tech.dojo.pay.uisdk.domain.mapper

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.sdk.card.entities.WalletSchemes
import tech.dojo.pay.uisdk.data.entities.Amount
import tech.dojo.pay.uisdk.data.entities.BillingAddress
import tech.dojo.pay.uisdk.data.entities.Branding
import tech.dojo.pay.uisdk.data.entities.Config
import tech.dojo.pay.uisdk.data.entities.Customer
import tech.dojo.pay.uisdk.data.entities.CustomerEmail
import tech.dojo.pay.uisdk.data.entities.ItemLines
import tech.dojo.pay.uisdk.data.entities.MerchantConfig
import tech.dojo.pay.uisdk.data.entities.PaymentIntentPayload
import tech.dojo.pay.uisdk.data.entities.ShippingAddress
import tech.dojo.pay.uisdk.data.entities.SupportedPaymentMethods
import tech.dojo.pay.uisdk.domain.entities.AmountDomainEntity
import tech.dojo.pay.uisdk.domain.entities.ItemLinesDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentIntentDomainEntity

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class PaymentIntentDomainEntityMapperTest {
    private lateinit var mapper: PaymentIntentDomainEntityMapper

    @Before
    fun setUp() {
        mapper = PaymentIntentDomainEntityMapper()
    }

    @Test
    fun `when calling apply with valid PaymentIntentPayload then should map to PaymentIntentDomainEntity with all valid data`() =
        runTest {
            // arrange
            val raw = createValidPaymentIntentPayload()
            val expected = getValidPaymentIntentDomainEntity()
            // act
            val actual = mapper.mapPayload(raw)
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `when calling apply with valid PaymentIntentPayload with status as captured then should map to PaymentIntentDomainEntity with isPaymentAlreadyCollected as true`() =
        runTest {
            // arrange
            val raw = createValidPaymentIntentPayload().copy(status = "Captured")
            val expected = getValidPaymentIntentDomainEntity().copy(isPaymentAlreadyCollected = true)
            // act
            val actual = mapper.mapPayload(raw)
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `when calling apply with valid PaymentIntentPayload with status as authorized then should map to PaymentIntentDomainEntity with isPaymentAlreadyCollected as true`() =
        runTest {
            // arrange
            val raw = createValidPaymentIntentPayload().copy(status = "Authorized")
            val expected = getValidPaymentIntentDomainEntity().copy(isPaymentAlreadyCollected = true)
            // act
            val actual = mapper.mapPayload(raw)
            // assert
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `when calling apply with valid PaymentIntentPayload with invalid currencyCode should return null`() =
        runTest {
            // arrange
            val raw = createValidPaymentIntentPayload().copy(
                amount = Amount(
                    10L,
                    "123",
                ),
            )
            // act
            val actual = mapper.mapPayload(raw)
            // assert
            assertNull(actual)
        }

    @Test
    fun `when calling apply with invalid PaymentIntentPayload should return null `() =
        runTest {
            // arrange
            val raw = PaymentIntentPayload()
            // act
            val actual = mapper.mapPayload(raw)
            // assert
            assertNull(actual)
        }

    private fun createValidPaymentIntentPayload() = PaymentIntentPayload(
        id = "id",
        captureMode = "captureMode",
        transactionSource = "transactionSource",
        clientSessionSecret = "clientSessionSecret",
        clientSessionSecretExpirationDate = "clientSessionSecretExpirationDate",
        status = "status",
        paymentMethods = listOf("paymentMethods"),
        amount = Amount(
            10L,
            "GBP",
        ),
        customer = Customer(id = "id"),
        reference = "reference",
        merchantConfig = MerchantConfig(
            supportedPaymentMethods = SupportedPaymentMethods(
                cardSchemes = listOf(CardsSchemes.MASTERCARD),
            ),
        ),
        paymentSource = "virtual-terminal",
        config = Config(
            tradingName = "tradingName",
            branding = Branding(logoURL = "logoURL", faviconURL = "faviconURL"),
            customerEmail = CustomerEmail(true),
            billingAddress = BillingAddress(true),
            shippingDetails = ShippingAddress(true),
        ),
        merchantInitiatedType = "merchantInitiatedType",
        itemLines = listOf(
            ItemLines(
                caption = "caption",
                amountTotal = Amount(
                    10L,
                    "GBP",
                ),
            ),
        ),

    )

    private fun getValidPaymentIntentDomainEntity(): PaymentIntentDomainEntity {
        val amountItem = Amount(
            10L,
            "GBP",
        )
        val id = "id"
        val clientSessionSecret = "clientSessionSecret"
        val amount = AmountDomainEntity(10L, "0.10", "GBP")
        val supportedCardsSchemes = listOf(CardsSchemes.MASTERCARD)
        val supportedWalletSchemes = emptyList<WalletSchemes>()
        val itemLines = listOf(ItemLinesDomainEntity("caption", amountItem))
        val customerId = "id"
        val collectionEmailRequired = true
        val isVirtualTerminalPayment = true
        val collectionBillingAddressRequired = true
        val isPreAuthPayment = false
        val orderId = "reference"
        val collectionShippingAddressRequired = true
        val isSetUpIntentPayment = true
        val merchantName = "tradingName"

        return PaymentIntentDomainEntity(
            id = id,
            paymentToken = clientSessionSecret,
            amount = amount,
            supportedCardsSchemes = supportedCardsSchemes,
            supportedWalletSchemes = supportedWalletSchemes,
            itemLines = itemLines,
            customerId = customerId,
            collectionEmailRequired = collectionEmailRequired,
            isVirtualTerminalPayment = isVirtualTerminalPayment,
            collectionBillingAddressRequired = collectionBillingAddressRequired,
            isPreAuthPayment = isPreAuthPayment,
            orderId = orderId,
            collectionShippingAddressRequired = collectionShippingAddressRequired,
            isSetUpIntentPayment = isSetUpIntentPayment,
            merchantName = merchantName,
        )
    }
}
