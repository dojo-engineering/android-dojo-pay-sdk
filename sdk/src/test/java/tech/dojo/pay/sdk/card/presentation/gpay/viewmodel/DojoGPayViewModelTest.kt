package tech.dojo.pay.sdk.card.presentation.gpay.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.Dojo3DSRepository
import tech.dojo.pay.sdk.card.data.GPayRepository
import tech.dojo.pay.sdk.card.data.GpayPaymentRequestMapper
import tech.dojo.pay.sdk.card.data.entities.GPayDetails
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import tech.dojo.pay.sdk.card.entities.DojoGPayParams
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams
import tech.dojo.pay.sdk.card.entities.DojoTotalAmount

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class DojoGPayViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: GPayRepository

    @Mock
    lateinit var dojo3DSRepository: Dojo3DSRepository

    @Mock
    lateinit var mapper: GpayPaymentRequestMapper

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `WHEN payment processing completes THEN payment result is returned AND user can exit`() =
        runTest {
            // arrange
            val dojoGPayParams = DojoGPayParams(
                dojoGPayPayload = DojoGPayPayload(
                    DojoGPayConfig(
                        merchantName = "",
                        merchantId = "",
                        gatewayMerchantId = ""
                    )
                ),
                dojoPaymentIntent = DojoPaymentIntent("", DojoTotalAmount(0L, ""))
            )
            val gPayDetails = GPayDetails(
                token = "testToken",
                email = "testEmailFormDojoGPayPayload",
                phoneNumber = "testPhone",
                billingContact = null,
                shippingContact = null
            )
            val result = PaymentResult.Completed(DojoPaymentResult.SUCCESSFUL)
            whenever(repository.processPayment(any())).thenReturn(result)
            whenever(mapper.apply(any(), any())).thenReturn(gPayDetails)
            // act
            val viewModel = DojoGPayViewModel(repository, dojo3DSRepository, mapper)
            viewModel.sendGPayDataToServer("", dojoGPayParams)
            // assert
            Assert.assertEquals(result, viewModel.paymentResult.value)
            Assert.assertTrue(viewModel.canExit)
        }

    @Test
    fun `WHEN 3DS page is fetched THEN html is loaded`() = runTest {
        // arrange
        val threeDsHtml = "html"
        whenever(dojo3DSRepository.fetch3dsPage(any())).thenReturn(threeDsHtml)
        // act
        val viewModel = DojoGPayViewModel(repository, dojo3DSRepository, mapper)
        val params = ThreeDSParams("url", "jwt", "md")
        viewModel.fetchThreeDsPage(params)
        // assert
        Assert.assertEquals(threeDsHtml, viewModel.threeDsPage.value)
    }
}
