package tech.dojo.pay.sdk.card.presentation.gpay.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.DeviceDataRepository
import tech.dojo.pay.sdk.card.data.Dojo3DSRepository
import tech.dojo.pay.sdk.card.data.GPayRepository
import tech.dojo.pay.sdk.card.data.entities.DecryptGPayTokenBody
import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.data.entities.GPayDetails
import tech.dojo.pay.sdk.card.data.mappers.GPayTokenDecryptionRequestMapper
import tech.dojo.pay.sdk.card.data.mappers.GpayPaymentRequestMapper
import tech.dojo.pay.sdk.card.entities.AuthMethod
import tech.dojo.pay.sdk.card.entities.DecryptGPayTokenParams
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.entities.DojoGPayParams
import tech.dojo.pay.sdk.card.entities.DojoGPayPayload
import tech.dojo.pay.sdk.card.entities.DojoPaymentIntent
import tech.dojo.pay.sdk.card.entities.DojoTotalAmount
import tech.dojo.pay.sdk.card.entities.PaymentResult

@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("LargeClass")
@RunWith(MockitoJUnitRunner::class)
internal class DojoGPayViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var gPayRepository: GPayRepository

    @Mock
    lateinit var deviceDataRepository: DeviceDataRepository

    @Mock
    lateinit var dojo3DSRepository: Dojo3DSRepository

    @Mock
    lateinit var gPayTokenDecryptionRequestMapper: GPayTokenDecryptionRequestMapper

    @Mock
    lateinit var gpayPaymentRequestMapper: GpayPaymentRequestMapper

    @Mock
    lateinit var configuredCardinalInstance: Cardinal

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `calling handlePaymentSuccessFromGpay should call apply from gPayTokenDecryptionRequestMapper first`() =
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

            val viewModel = DojoGPayViewModel(
                gPayRepository,
                deviceDataRepository,
                dojo3DSRepository,
                gPayTokenDecryptionRequestMapper,
                gpayPaymentRequestMapper,
                configuredCardinalInstance
            )
            // act
            viewModel.handlePaymentSuccessFromGpay("GpayDate", dojoGPayParams)
            // assert
            verify(gPayTokenDecryptionRequestMapper).apply("GpayDate")
        }

    @Test
    fun `calling handlePaymentSuccessFromGpay with exception thrown  from apply  then Field state post to UI `() =
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

            whenever(gPayTokenDecryptionRequestMapper.apply(any())).thenThrow(RuntimeException(""))

            val viewModel = DojoGPayViewModel(
                gPayRepository,
                deviceDataRepository,
                dojo3DSRepository,
                gPayTokenDecryptionRequestMapper,
                gpayPaymentRequestMapper,
                configuredCardinalInstance
            )
            // act
            viewModel.handlePaymentSuccessFromGpay("GpayDate", dojoGPayParams)
            // assert
            Assert.assertEquals(
                PaymentResult.Completed(DojoPaymentResult.FAILED),
                viewModel.paymentResult.value
            )
        }

    @Test
    fun `calling handlePaymentSuccessFromGpay with null returned then Field state post to UI `() =
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

            whenever(gPayTokenDecryptionRequestMapper.apply(any())).thenReturn(null)

            val viewModel = DojoGPayViewModel(
                gPayRepository,
                deviceDataRepository,
                dojo3DSRepository,
                gPayTokenDecryptionRequestMapper,
                gpayPaymentRequestMapper,
                configuredCardinalInstance
            )
            // act
            viewModel.handlePaymentSuccessFromGpay("GpayDate", dojoGPayParams)
            // assert
            Assert.assertEquals(
                PaymentResult.Completed(DojoPaymentResult.FAILED),
                viewModel.paymentResult.value
            )
        }

    @Test
    fun `calling handlePaymentSuccessFromGpay with success from mapping should call decryptGPayToken `() =
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

            whenever(gPayTokenDecryptionRequestMapper.apply(any())).thenReturn(
                DecryptGPayTokenBody(
                    ""
                )
            )

            val viewModel = DojoGPayViewModel(
                gPayRepository,
                deviceDataRepository,
                dojo3DSRepository,
                gPayTokenDecryptionRequestMapper,
                gpayPaymentRequestMapper,
                configuredCardinalInstance
            )
            // act
            viewModel.handlePaymentSuccessFromGpay("GpayDate", dojoGPayParams)
            // assert
            verify(gPayRepository).decryptGPayToken(DecryptGPayTokenBody(""))
        }

    @Test
    fun `calling handlePaymentSuccessFromGpay with success from decryptGPayToken and authMethod is CRYPTOGRAM_3DS should call process payment `() =
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

            whenever(gPayTokenDecryptionRequestMapper.apply(any())).thenReturn(
                DecryptGPayTokenBody(
                    ""
                )
            )
            whenever(gPayRepository.decryptGPayToken(any())).thenReturn(
                DecryptGPayTokenParams(
                    authMethod = AuthMethod.CRYPTOGRAM_3DS,
                    pan = "",
                    expirationMonth = "",
                    expirationYear = ""
                )
            )
            whenever(gpayPaymentRequestMapper.apply(any(), any())).thenReturn(
                GPayDetails("", "", "", null, null)
            )

            val gPayDetails = GPayDetails(
                token = "testToken",
                email = "testEmailFormDojoGPayPayload",
                phoneNumber = "testPhone",
                billingContact = null,
                shippingContact = null
            )

            val viewModel = DojoGPayViewModel(
                gPayRepository,
                deviceDataRepository,
                dojo3DSRepository,
                gPayTokenDecryptionRequestMapper,
                gpayPaymentRequestMapper,
                configuredCardinalInstance
            )
            // act
            viewModel.handlePaymentSuccessFromGpay(gPayDetails.toString(), dojoGPayParams)
            // assert
            verify(gPayRepository).processPayment(any())
        }

    @Test
    fun `calling handlePaymentSuccessFromGpay with success from decryptGPayToken and authMethod is CRYPTOGRAM_3DS with payment processing completes THEN payment result is returned AND user can exit`() =
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

            whenever(gPayTokenDecryptionRequestMapper.apply(any())).thenReturn(
                DecryptGPayTokenBody(
                    ""
                )
            )
            whenever(gPayRepository.decryptGPayToken(any())).thenReturn(
                DecryptGPayTokenParams(
                    authMethod = AuthMethod.CRYPTOGRAM_3DS,
                    pan = "",
                    expirationMonth = "",
                    expirationYear = ""
                )
            )
            whenever(gpayPaymentRequestMapper.apply(any(), any())).thenReturn(
                GPayDetails("", "", "", null, null)
            )
            val result = PaymentResult.Completed(DojoPaymentResult.SUCCESSFUL)
            whenever(gPayRepository.processPayment(any())).thenReturn(result)

            val gPayDetails = GPayDetails(
                token = "testToken",
                email = "testEmailFormDojoGPayPayload",
                phoneNumber = "testPhone",
                billingContact = null,
                shippingContact = null
            )

            val viewModel = DojoGPayViewModel(
                gPayRepository,
                deviceDataRepository,
                dojo3DSRepository,
                gPayTokenDecryptionRequestMapper,
                gpayPaymentRequestMapper,
                configuredCardinalInstance
            )
            // act
            viewModel.handlePaymentSuccessFromGpay(gPayDetails.toString(), dojoGPayParams)
            // assert
            Assert.assertEquals(result, viewModel.paymentResult.value)
            Assert.assertTrue(viewModel.canExit)
        }

    @Test
    fun `calling handlePaymentSuccessFromGpay with success from decryptGPayToken and authMethod is Pan_Only should call collect device data `() =
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

            whenever(gPayTokenDecryptionRequestMapper.apply(any())).thenReturn(
                DecryptGPayTokenBody(
                    ""
                )
            )
            whenever(gPayRepository.decryptGPayToken(any())).thenReturn(
                DecryptGPayTokenParams(
                    authMethod = AuthMethod.PAN_ONLY,
                    pan = "",
                    expirationMonth = "",
                    expirationYear = ""
                )
            )

            val gPayDetails = GPayDetails(
                token = "testToken",
                email = "testEmailFormDojoGPayPayload",
                phoneNumber = "testPhone",
                billingContact = null,
                shippingContact = null
            )

            val viewModel = DojoGPayViewModel(
                gPayRepository,
                deviceDataRepository,
                dojo3DSRepository,
                gPayTokenDecryptionRequestMapper,
                gpayPaymentRequestMapper,
                configuredCardinalInstance
            )
            // act
            viewModel.handlePaymentSuccessFromGpay(gPayDetails.toString(), dojoGPayParams)
            // assert
            verify(deviceDataRepository).collectDeviceData(any())
        }

    @Test
    fun `calling collect device data  should emits  deviceData flow`() = runTest {
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

        whenever(gPayTokenDecryptionRequestMapper.apply(any())).thenReturn(
            DecryptGPayTokenBody(
                ""
            )
        )
        whenever(gPayRepository.decryptGPayToken(any())).thenReturn(
            DecryptGPayTokenParams(
                authMethod = AuthMethod.PAN_ONLY,
                pan = "",
                expirationMonth = "",
                expirationYear = ""
            )
        )
        val result = DeviceData(
            formAction = "",
            token = ""
        )
        whenever(deviceDataRepository.collectDeviceData(any())).thenReturn(result)

        val gPayDetails = GPayDetails(
            token = "testToken",
            email = "testEmailFormDojoGPayPayload",
            phoneNumber = "testPhone",
            billingContact = null,
            shippingContact = null
        )

        val viewModel = DojoGPayViewModel(
            gPayRepository,
            deviceDataRepository,
            dojo3DSRepository,
            gPayTokenDecryptionRequestMapper,
            gpayPaymentRequestMapper,
            configuredCardinalInstance
        )
        // act
        viewModel.handlePaymentSuccessFromGpay(gPayDetails.toString(), dojoGPayParams)
        // assert
        Assert.assertEquals(result, viewModel.deviceData.value)
    }

    @Test
    fun `failed  from collect data should emits  failed state to ui and user can exit `() =
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

            whenever(gPayTokenDecryptionRequestMapper.apply(any())).thenReturn(
                DecryptGPayTokenBody(
                    ""
                )
            )
            whenever(gPayRepository.decryptGPayToken(any())).thenReturn(
                DecryptGPayTokenParams(
                    authMethod = AuthMethod.PAN_ONLY,
                    pan = "",
                    expirationMonth = "",
                    expirationYear = ""
                )
            )
            val result = PaymentResult.Completed(DojoPaymentResult.FAILED)
            whenever(deviceDataRepository.collectDeviceData(any())).thenThrow(RuntimeException(""))

            val gPayDetails = GPayDetails(
                token = "testToken",
                email = "testEmailFormDojoGPayPayload",
                phoneNumber = "testPhone",
                billingContact = null,
                shippingContact = null
            )

            val viewModel = DojoGPayViewModel(
                gPayRepository,
                deviceDataRepository,
                dojo3DSRepository,
                gPayTokenDecryptionRequestMapper,
                gpayPaymentRequestMapper,
                configuredCardinalInstance
            )
            // act
            viewModel.handlePaymentSuccessFromGpay(gPayDetails.toString(), dojoGPayParams)
            // assert
            Assert.assertEquals(result, viewModel.paymentResult.value)
        }

    @Test
    fun `calling initCardinal should init from cardinal `() = runTest {
        // arrange
        val viewModel = DojoGPayViewModel(
            gPayRepository,
            deviceDataRepository,
            dojo3DSRepository,
            gPayTokenDecryptionRequestMapper,
            gpayPaymentRequestMapper,
            configuredCardinalInstance
        )
        // act
        viewModel.initCardinal()
        // assert
        verify(configuredCardinalInstance).init(null, viewModel)
    }

    @Test
    fun `calling onSetupCompleted with valid data should call process payment`() = runTest {
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

        whenever(gPayTokenDecryptionRequestMapper.apply(any())).thenReturn(
            DecryptGPayTokenBody(
                ""
            )
        )
        whenever(gPayRepository.decryptGPayToken(any())).thenReturn(
            DecryptGPayTokenParams(
                authMethod = AuthMethod.PAN_ONLY,
                pan = "",
                expirationMonth = "",
                expirationYear = ""
            )
        )
        val deviceData = DeviceData(
            formAction = "",
            token = ""
        )
        whenever(deviceDataRepository.collectDeviceData(any())).thenReturn(deviceData)

        val gPayDetails = GPayDetails(
            token = "testToken",
            email = "testEmailFormDojoGPayPayload",
            phoneNumber = "testPhone",
            billingContact = null,
            shippingContact = null
        )

        whenever(gpayPaymentRequestMapper.apply(any(), any())).thenReturn(gPayDetails)
        val viewModel = DojoGPayViewModel(
            gPayRepository,
            deviceDataRepository,
            dojo3DSRepository,
            gPayTokenDecryptionRequestMapper,
            gpayPaymentRequestMapper,
            configuredCardinalInstance
        )
        // act
        viewModel.handlePaymentSuccessFromGpay(gPayDetails.toString(), dojoGPayParams)
        viewModel.onSetupCompleted("")
        // assert
        verify(gPayRepository).processPayment(gPayDetails)
    }

    @Test
    fun `calling onSetupCompleted with valid data should call process payment and emits success if process payment succeed `() =
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

            whenever(gPayTokenDecryptionRequestMapper.apply(any())).thenReturn(
                DecryptGPayTokenBody(
                    ""
                )
            )
            whenever(gPayRepository.decryptGPayToken(any())).thenReturn(
                DecryptGPayTokenParams(
                    authMethod = AuthMethod.PAN_ONLY,
                    pan = "",
                    expirationMonth = "",
                    expirationYear = ""
                )
            )
            val deviceData = DeviceData(
                formAction = "",
                token = ""
            )
            whenever(deviceDataRepository.collectDeviceData(any())).thenReturn(deviceData)

            val gPayDetails = GPayDetails(
                token = "testToken",
                email = "testEmailFormDojoGPayPayload",
                phoneNumber = "testPhone",
                billingContact = null,
                shippingContact = null
            )

            whenever(gpayPaymentRequestMapper.apply(any(), any())).thenReturn(gPayDetails)
            val result = PaymentResult.Completed(DojoPaymentResult.SUCCESSFUL)

            whenever(gPayRepository.processPayment(gPayDetails)).thenReturn(result)

            val viewModel = DojoGPayViewModel(
                gPayRepository,
                deviceDataRepository,
                dojo3DSRepository,
                gPayTokenDecryptionRequestMapper,
                gpayPaymentRequestMapper,
                configuredCardinalInstance
            )
            // act
            viewModel.handlePaymentSuccessFromGpay(gPayDetails.toString(), dojoGPayParams)
            viewModel.onSetupCompleted("")
            // assert
            Assert.assertEquals(result, viewModel.paymentResult.value)
        }

    @Test
    fun `calling onSetupCompleted with valid data and faild from  process payment should emit failed state to ui `() =
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

            whenever(gPayTokenDecryptionRequestMapper.apply(any())).thenReturn(
                DecryptGPayTokenBody(
                    ""
                )
            )
            whenever(gPayRepository.decryptGPayToken(any())).thenReturn(
                DecryptGPayTokenParams(
                    authMethod = AuthMethod.PAN_ONLY,
                    pan = "",
                    expirationMonth = "",
                    expirationYear = ""
                )
            )
            val deviceData = DeviceData(
                formAction = "",
                token = ""
            )
            whenever(deviceDataRepository.collectDeviceData(any())).thenReturn(deviceData)

            val gPayDetails = GPayDetails(
                token = "testToken",
                email = "testEmailFormDojoGPayPayload",
                phoneNumber = "testPhone",
                billingContact = null,
                shippingContact = null
            )

            whenever(gpayPaymentRequestMapper.apply(any(), any())).thenReturn(gPayDetails)
            whenever(gPayRepository.processPayment(any())).thenThrow(RuntimeException(""))

            val viewModel = DojoGPayViewModel(
                gPayRepository,
                deviceDataRepository,
                dojo3DSRepository,
                gPayTokenDecryptionRequestMapper,
                gpayPaymentRequestMapper,
                configuredCardinalInstance
            )
            // act
            viewModel.handlePaymentSuccessFromGpay(gPayDetails.toString(), dojoGPayParams)
            viewModel.onSetupCompleted("")
            // assert
            Assert.assertEquals(
                PaymentResult.Completed(DojoPaymentResult.FAILED),
                viewModel.paymentResult.value
            )
        }

    @Test
    fun `calling onSetupCompleted with invalid data should emits failed state to ui `() = runTest {
        // arrange
        val viewModel = DojoGPayViewModel(
            gPayRepository,
            deviceDataRepository,
            dojo3DSRepository,
            gPayTokenDecryptionRequestMapper,
            gpayPaymentRequestMapper,
            configuredCardinalInstance
        )
        // act
        viewModel.onSetupCompleted("")
        // assert
        Assert.assertEquals(
            PaymentResult.Completed(DojoPaymentResult.FAILED),
            viewModel.paymentResult.value
        )
    }

    @Test
    fun `calling onValidated should emits failed state to ui `() = runTest {
        // arrange
        val viewModel = DojoGPayViewModel(
            gPayRepository,
            deviceDataRepository,
            dojo3DSRepository,
            gPayTokenDecryptionRequestMapper,
            gpayPaymentRequestMapper,
            configuredCardinalInstance
        )
        // act
        viewModel.onValidated(null, null)

        // assert
        Assert.assertEquals(
            PaymentResult.Completed(DojoPaymentResult.FAILED),
            viewModel.paymentResult.value
        )
    }

    @Test
    fun `calling on3dsCompleted with a valid data should call processAuthorization and return payment result to ui `() = runTest {
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

        whenever(gPayTokenDecryptionRequestMapper.apply(any())).thenReturn(
            DecryptGPayTokenBody(
                ""
            )
        )
        whenever(gPayRepository.decryptGPayToken(any())).thenReturn(
            DecryptGPayTokenParams(
                authMethod = AuthMethod.PAN_ONLY,
                pan = "",
                expirationMonth = "",
                expirationYear = ""
            )
        )
        val deviceData = DeviceData(
            formAction = "",
            token = ""
        )
        whenever(deviceDataRepository.collectDeviceData(any())).thenReturn(deviceData)

        val gPayDetails = GPayDetails(
            token = "testToken",
            email = "testEmailFormDojoGPayPayload",
            phoneNumber = "testPhone",
            billingContact = null,
            shippingContact = null
        )

        val result = PaymentResult.Completed(DojoPaymentResult.SUCCESSFUL)

        whenever(dojo3DSRepository.processAuthorization("", "", null)).thenReturn(result)

        val viewModel = DojoGPayViewModel(
            gPayRepository,
            deviceDataRepository,
            dojo3DSRepository,
            gPayTokenDecryptionRequestMapper,
            gpayPaymentRequestMapper,
            configuredCardinalInstance
        )
        // act
        viewModel.handlePaymentSuccessFromGpay(gPayDetails.toString(), dojoGPayParams)
        viewModel.on3dsCompleted("", "", null)
        // assert
        Assert.assertEquals(result, viewModel.paymentResult.value)
    }
}
