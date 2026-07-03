package tech.dojo.pay.sdk.card.presentation.card.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.CardPaymentRepository
import tech.dojo.pay.sdk.card.data.DeviceDataRepository
import tech.dojo.pay.sdk.card.data.Dojo3DSRepository
import tech.dojo.pay.sdk.card.data.entities.DeviceData
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentPayLoad
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.presentation.threeds.Dojo3DSBaseViewModel

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal class DojoCardPaymentViewModel(
    private val cardPaymentRepository: CardPaymentRepository,
    private val dojo3DSRepository: Dojo3DSRepository,
    private val deviceDataRepository: DeviceDataRepository,
    private val dojoCardPaymentPayLoad: DojoCardPaymentPayLoad,
    private val configuredCardinalInstance: Cardinal
) : Dojo3DSBaseViewModel(configuredCardinalInstance) {

    companion object {
        private const val TAG = "DojoCardPaymentVM"
    }

    val paymentResult = MutableLiveData<PaymentResult>()
    val deviceData = MutableLiveData<DeviceData>()
    var canExit: Boolean = false // User should not be able to leave while request is not completed

    init {
        viewModelScope.launch {
            try {
                val data = deviceDataRepository.collectDeviceData(dojoCardPaymentPayLoad)
                deviceData.value = data
            } catch (throwable: Throwable) {
                Log.e(TAG, "init: Failed to collect device data.", throwable)
                if (throwable is HttpException) {
                    val errorBody = throwable.response()?.errorBody()?.string()
                    Log.e(TAG, "init: HTTP ${throwable.code()} — $errorBody")
                }
                postPaymentFieldToUI()
            }
        }
    }

    fun initCardinal() {
        configureDCardinalInstance.init(deviceData.value?.token, this)
    }

    override fun onSetupCompleted(consumerSessionId: String?) {
        viewModelScope.launch {
            try {
                val result = cardPaymentRepository.processPayment()
                paymentResult.value = result
                canExit = true
            } catch (throwable: Throwable) {
                Log.e(TAG, "onSetupCompleted: processPayment() threw exception.", throwable)
                postPaymentFieldToUI()
            }
        }
    }

    override fun onValidated(validateResponse: ValidateResponse?, serverJwt: String?) {
        Log.w(TAG, "onValidated: Unexpected Cardinal onValidated path. errorNumber=${validateResponse?.errorNumber}, actionCode=${validateResponse?.actionCode}")
        postPaymentFieldToUI()
    }

    fun on3dsCompleted(
        serverJWT: String? = null,
        transactionId: String? = null,
        validateResponse: ValidateResponse? = null
    ) {
        viewModelScope.launch {
            try {
                val result = dojo3DSRepository.processAuthorization(
                    serverJWT ?: "",
                    transactionId ?: "",
                    validateResponse
                )
                paymentResult.value = result
                canExit = true
            } catch (throwable: Throwable) {
                Log.e(TAG, "on3dsCompleted: processAuthorization() threw exception.", throwable)
                postPaymentFieldToUI()
            }
        }
    }

    private fun postPaymentFieldToUI() {
        paymentResult.postValue(PaymentResult.Completed(DojoPaymentResult.FAILED))
    }
}
