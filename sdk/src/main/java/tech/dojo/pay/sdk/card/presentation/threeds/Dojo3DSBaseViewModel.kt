package tech.dojo.pay.sdk.card.presentation.threeds

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.card.entities.ThreeDSParams

abstract class Dojo3DSBaseViewModel : ViewModel() {

    val threeDsPage = MutableLiveData<String>()
    abstract fun on3DSCompleted(result: DojoPaymentResult)
    internal abstract fun fetchThreeDsPage(params: ThreeDSParams)
}