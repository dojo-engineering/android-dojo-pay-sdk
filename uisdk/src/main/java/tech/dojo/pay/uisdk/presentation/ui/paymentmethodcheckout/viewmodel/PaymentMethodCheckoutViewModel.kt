package tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.state.PaymentMethodCheckoutState

class PaymentMethodCheckoutViewModel : ViewModel() {
    private val mutableState = MutableLiveData<PaymentMethodCheckoutState>()
    val state: LiveData<PaymentMethodCheckoutState>
        get() = mutableState

    fun handleGooglePayAvailable() {
        mutableState.postValue(
            PaymentMethodCheckoutState(
                isGooglePayVisible = true,
                isBottomSheetVisible = true
            )
        )
    }
    fun handleGooglePayUnAvailable() {
        mutableState.postValue(
            PaymentMethodCheckoutState(
                isGooglePayVisible = false,
                isBottomSheetVisible = true
            )
        )
    }
}
