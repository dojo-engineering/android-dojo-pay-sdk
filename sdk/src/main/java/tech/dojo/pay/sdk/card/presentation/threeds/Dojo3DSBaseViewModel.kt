package tech.dojo.pay.sdk.card.presentation.threeds

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams
import com.cardinalcommerce.cardinalmobilesdk.services.CardinalInitService
import com.cardinalcommerce.cardinalmobilesdk.services.CardinalValidateReceiver


abstract class Dojo3DSBaseViewModel(
    cardinalConfigurator: CardinalConfigurator
) : ViewModel(), CardinalInitService, CardinalValidateReceiver {

    val threeDsPage = MutableLiveData<String>()
    val configureDCardinalInstance = cardinalConfigurator.getConfiguredCardinalInstance()
    abstract fun on3DSCompleted(result: DojoPaymentResult)
    internal abstract fun fetchThreeDsPage(params: ThreeDSParams)
}
