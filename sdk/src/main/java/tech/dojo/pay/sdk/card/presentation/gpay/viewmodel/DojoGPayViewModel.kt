package tech.dojo.pay.sdk.card.presentation.gpay.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.Dojo3DSRepository
import tech.dojo.pay.sdk.card.data.GPayRepository
import tech.dojo.pay.sdk.card.data.GpayPaymentRequestMapper
import tech.dojo.pay.sdk.card.entities.DojoGPayParams
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.presentation.threeds.Dojo3DSBaseViewModel

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal class DojoGPayViewModel(
    private val repository: GPayRepository,
    private val dojo3DSRepository: Dojo3DSRepository,
    private val gpayPaymentRequestMapper: GpayPaymentRequestMapper,
    configuredCardinalInstance: Cardinal
) : Dojo3DSBaseViewModel(configuredCardinalInstance) {
    val paymentResult = MutableLiveData<PaymentResult>()
    var canExit: Boolean = false // User should not be able to leave while request is not completed

    fun sendGPayDataToServer(gPayData: String, dojoGPayParams: DojoGPayParams) {
        viewModelScope.launch {
            try {
                val result = repository.processPayment(
                    gpayPaymentRequestMapper.apply(
                        gPayData,
                        dojoGPayParams
                    )
                )
                paymentResult.value = result
                canExit = true
            } catch (throwable: Throwable) {
                paymentResult.value = PaymentResult.Completed(DojoPaymentResult.SDK_INTERNAL_ERROR)
            }
        }
    }


    override fun onSetupCompleted(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onValidated(p0: ValidateResponse?, p1: String?) {
        TODO("Not yet implemented")
    }
}
