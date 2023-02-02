package tech.dojo.pay.sdk.card.presentation.gpay.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.DeviceDataRepository
import tech.dojo.pay.sdk.card.data.Dojo3DSRepository
import tech.dojo.pay.sdk.card.data.GPayRepository
import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.data.mappers.GPayTokenDecryptionRequestMapper
import tech.dojo.pay.sdk.card.data.mappers.GpayPaymentRequestMapper
import tech.dojo.pay.sdk.card.entities.*
import tech.dojo.pay.sdk.card.entities.AuthMethod
import tech.dojo.pay.sdk.card.entities.DojoGPayParams
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.presentation.threeds.Dojo3DSBaseViewModel

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal class DojoGPayViewModel(
    private val gPayRepository: GPayRepository,
    private val deviceDataRepository: DeviceDataRepository,
    private val dojo3DSRepository: Dojo3DSRepository,
    private val gPayTokenDecryptionRequestMapper: GPayTokenDecryptionRequestMapper,
    private val gpayPaymentRequestMapper: GpayPaymentRequestMapper,
    configuredCardinalInstance: Cardinal
) : Dojo3DSBaseViewModel(configuredCardinalInstance) {
    private var googlePayData: String? = null
    private var dojoGooglePayParams: DojoGPayParams? = null
    val paymentResult = MutableLiveData<PaymentResult>()
    val deviceData = MutableLiveData<DeviceData>()
    var canExit: Boolean = false // User should not be able to leave while request is not completed

    fun handlePaymentSuccessFromGpay(gPayData: String, dojoGPayParams: DojoGPayParams) {
        viewModelScope.launch {
            try {
                googlePayData = gPayData
                dojoGooglePayParams = dojoGPayParams
                val decryptGPayTokenBody = gPayTokenDecryptionRequestMapper.apply(gPayData)
                if (decryptGPayTokenBody != null) {
                    val decryptedGPayTokenParams =
                        gPayRepository.decryptGPayToken(decryptGPayTokenBody)
                    handleDecryptedGPayTokenParams(
                        decryptedGPayTokenParams,
                        gPayData,
                        dojoGPayParams
                    )
                } else {
                    postPaymentFieldToUI()
                }
                canExit = true
            } catch (throwable: Throwable) {
                postPaymentFieldToUI()
            }
        }
    }

    private suspend fun handleDecryptedGPayTokenParams(
        decryptedGPayTokenParams: DecryptGPayTokenParams,
        googlePayData: String,
        dojoGPayParams: DojoGPayParams
    ) {
        when (decryptedGPayTokenParams.authMethod) {
            AuthMethod.CRYPTOGRAM_3DS -> handleCryptogram3ds(googlePayData, dojoGPayParams)
            AuthMethod.PAN_ONLY -> handlePanOnly(decryptedGPayTokenParams)
            else -> postPaymentFieldToUI()
        }
    }

    private suspend fun handleCryptogram3ds(
        gPayData: String,
        dojoGPayParams: DojoGPayParams
    ) {
        processPayment(gPayData, dojoGPayParams)
    }


    private suspend fun handlePanOnly(decryptedGPayTokenParams: DecryptGPayTokenParams) {
        deviceData.value = deviceDataRepository.collectDeviceData(
            DojoCardPaymentPayLoad.FullCardPaymentPayload(
                cardDetails = DojoCardDetails(
                    cardNumber = decryptedGPayTokenParams.pan,
                    expiryMonth = decryptedGPayTokenParams.expirationMonth,
                    expiryYear = decryptedGPayTokenParams.expirationYear
                )
            )
        )
    }

    fun initCardinal() {
        configureDCardinalInstance.init(deviceData.value?.token, this)
    }

    override fun onSetupCompleted(consumerSessionId: String?) {
        viewModelScope.launch {
            try {
                if (googlePayData != null && dojoGooglePayParams != null) {
                    dojoGooglePayParams?.let { processPayment(googlePayData ?: "", it) }
                } else {
                    postPaymentFieldToUI()
                }
            } catch (throwable: Throwable) {
                postPaymentFieldToUI()
            }
        }
    }

    override fun onValidated(p0: ValidateResponse?, p1: String?) {
        postPaymentFieldToUI()
    }

    fun on3dsCompleted(
        serverJWT: String? = null,
        transactionId: String? = null,
        validateResponse: ValidateResponse? = null
    ) {
        viewModelScope.launch {
            try {
                paymentResult.value =
                    dojo3DSRepository.processAuthorization(
                        serverJWT ?: "",
                        transactionId ?: "",
                        validateResponse
                    )
                canExit = true
            } catch (throwable: Throwable) {
                postPaymentFieldToUI()
            }
        }
    }

    private suspend fun processPayment(
        gPayData: String,
        dojoGPayParams: DojoGPayParams
    ) {
        val result = gPayRepository.processPayment(
            gpayPaymentRequestMapper.apply(
                gPayData,
                dojoGPayParams
            )
        )
        paymentResult.value = result
    }

    private fun postPaymentFieldToUI() {
        paymentResult.value = PaymentResult.Completed(DojoPaymentResult.FAILED)
    }
}
