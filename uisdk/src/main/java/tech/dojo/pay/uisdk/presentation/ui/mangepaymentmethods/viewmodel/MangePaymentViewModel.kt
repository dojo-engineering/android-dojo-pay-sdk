package tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.domain.ObservePaymentMethods
import tech.dojo.pay.uisdk.domain.ObserveWalletState
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.mapper.PaymentMethodItemViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.MangePaymentMethodsState
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntity

internal class MangePaymentViewModel(
    private val observeWalletState: ObserveWalletState,
    private val observePaymentMethods: ObservePaymentMethods,
    private val mapper: PaymentMethodItemViewEntityMapper
) : ViewModel() {

    private val mutableState = MutableLiveData<MangePaymentMethodsState>()
    val state: LiveData<MangePaymentMethodsState>
        get() = mutableState
    private var currentState: MangePaymentMethodsState
    private var isWalletEnabled: Boolean = false
    private var currentSelectedMethod: PaymentMethodItemViewEntity? = null

    init {
        currentState = MangePaymentMethodsState(
            appBarIcon = R.drawable.ic_close_green_24px,
            paymentMethodItems = PaymentMethodItemViewEntity(emptyList()),
            isUsePaymentMethodButtonEnabled = true
        )
        viewModelScope.launch {
            observeWalletState.observe().collect {
                isWalletEnabled = it ?: false
                observePaymentMethods.observe().collect { result ->
                    currentState = currentState.copy(
                        paymentMethodItems = mapper.apply(result, isWalletEnabled),
                        isUsePaymentMethodButtonEnabled = isWalletEnabled
                    )
                }
            }
        }
        postStateToUI()
    }

    private fun postStateToUI() {
        mutableState.postValue(currentState)
    }
}