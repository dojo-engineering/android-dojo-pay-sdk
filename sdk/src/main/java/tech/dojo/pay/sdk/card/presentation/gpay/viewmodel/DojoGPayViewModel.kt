package tech.dojo.pay.sdk.card.presentation.gpay.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cardinalcommerce.cardinalmobilesdk.models.ValidateResponse
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.data.Dojo3DSRepository
import tech.dojo.pay.sdk.card.data.GPayRepository
import tech.dojo.pay.sdk.card.data.GpayPaymentRequestMapper
import tech.dojo.pay.sdk.card.entities.DojoGPayParams
import tech.dojo.pay.sdk.card.entities.PaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams
import tech.dojo.pay.sdk.card.presentation.threeds.CardinalConfigurator
import tech.dojo.pay.sdk.card.presentation.threeds.Dojo3DSBaseViewModel

@Suppress("TooGenericExceptionCaught", "SwallowedException")
internal class DojoGPayViewModel(
    private val repository: GPayRepository,
    private val dojo3DSRepository: Dojo3DSRepository,
    private val gpayPaymentRequestMapper: GpayPaymentRequestMapper,
    cardinalConfigurator: CardinalConfigurator
) : Dojo3DSBaseViewModel(cardinalConfigurator) {
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

    override fun on3DSCompleted(result: DojoPaymentResult) {
        paymentResult.postValue(PaymentResult.Completed(result))
    }

    override fun fetchThreeDsPage(params: ThreeDSParams) {
        viewModelScope.launch {
            try {
                threeDsPage.value = dojo3DSRepository.fetch3dsPage(params)
            } catch (e: Exception) {
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

    override fun onValidated(p0: Context?, p1: ValidateResponse?, p2: String?) {
        TODO("Not yet implemented")
    }
}
