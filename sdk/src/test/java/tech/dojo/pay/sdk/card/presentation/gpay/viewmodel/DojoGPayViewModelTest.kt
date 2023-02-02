package tech.dojo.pay.sdk.card.presentation.gpay.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import tech.dojo.pay.sdk.card.data.Dojo3DSRepository
import tech.dojo.pay.sdk.card.data.GPayRepository
import tech.dojo.pay.sdk.card.data.mappers.GpayPaymentRequestMapper

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

//    @Test
//    fun `WHEN payment processing completes THEN payment result is returned AND user can exit`() =
//        runTest {
//            // arrange
//            val dojoGPayParams = DojoGPayParams(
//                dojoGPayPayload = DojoGPayPayload(
//                    DojoGPayConfig(
//                        merchantName = "",
//                        merchantId = "",
//                        gatewayMerchantId = ""
//                    )
//                ),
//                dojoPaymentIntent = DojoPaymentIntent("", DojoTotalAmount(0L, ""))
//            )
//            val gPayDetails = GPayDetails(
//                token = "testToken",
//                email = "testEmailFormDojoGPayPayload",
//                phoneNumber = "testPhone",
//                billingContact = null,
//                shippingContact = null
//            )
//            val result = PaymentResult.Completed(DojoPaymentResult.SUCCESSFUL)
//            whenever(repository.processPayment(any())).thenReturn(result)
//            whenever(mapper.apply(any(), any())).thenReturn(gPayDetails)
//            // act
//            val viewModel = DojoGPayViewModel(
//                repository,
//                dojo3DSRepository,
//                mapper,
//                cardinalConfigurator
//            )
//            viewModel.sendGPayDataToServer("", dojoGPayParams)
//            // assert
//            Assert.assertEquals(result, viewModel.paymentResult.value)
//            Assert.assertTrue(viewModel.canExit)
//        }
//
//    @Test
//    fun `WHEN 3DS page is fetched THEN html is loaded`() = runTest {
//        // arrange
//        val threeDsHtml = "html"
//        whenever(dojo3DSRepository.fetch3dsPage(any())).thenReturn(threeDsHtml)
//        // act
//        val viewModel = DojoGPayViewModel(
//            repository,
//            dojo3DSRepository,
//            mapper,
//            cardinalConfigurator
//        )
//        val params = ThreeDSParams("url", "jwt", "md")
//        viewModel.fetchThreeDsPage(params)
//        // assert
//        Assert.assertEquals(threeDsHtml, viewModel.threeDsPage.value)
//    }
}
