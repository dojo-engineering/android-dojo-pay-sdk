package tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.dojo.pay.uisdk.domain.DeletePaymentMethodsUseCase
import tech.dojo.pay.uisdk.domain.ObservePaymentMethods
import tech.dojo.pay.uisdk.domain.ObserveWalletState
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.mapper.PaymentMethodItemViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.AppBarIconType
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.MangePaymentMethodsState
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntity
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem

internal class MangePaymentViewModel(
    private val deletePaymentMethodsUseCase: DeletePaymentMethodsUseCase,
    private val observeWalletState: ObserveWalletState,
    private val observePaymentMethods: ObservePaymentMethods,
    private val mapper: PaymentMethodItemViewEntityMapper
) : ViewModel() {

    private val mutableState = MutableLiveData<MangePaymentMethodsState>()
    val state: LiveData<MangePaymentMethodsState>
        get() = mutableState
    private var currentState: MangePaymentMethodsState
    private var isWalletEnabled: Boolean = false
    private var currentSelectedMethod: PaymentMethodItemViewEntityItem? = null
    private var currentDeletedMethod: PaymentMethodItemViewEntityItem.CardItemItem? = null

    init {
        currentState = MangePaymentMethodsState(
            appBarIconType = AppBarIconType.CLOSE,
            paymentMethodItems = PaymentMethodItemViewEntity(emptyList()),
            isUsePaymentMethodButtonEnabled = true,
            showDialog = false,
            isInEditMode = false
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

    fun onPaymentMethodChanged(currentSelectedMethod: PaymentMethodItemViewEntityItem?) {
        this.currentSelectedMethod = currentSelectedMethod
        currentState = currentState
            .copy(isUsePaymentMethodButtonEnabled = isWalletEnabled || currentSelectedMethod is PaymentMethodItemViewEntityItem.CardItemItem)
        postStateToUI()
    }

    fun onPaymentMethodLongCLick(currentSelectedMethod: PaymentMethodItemViewEntityItem?) {
        this.currentDeletedMethod =
            currentSelectedMethod as PaymentMethodItemViewEntityItem.CardItemItem
        currentState = currentState.copy(
            appBarIconType = AppBarIconType.DELETE,
            isInEditMode = true
        )
        postStateToUI()
    }

    fun onDeleteClicked() {
        currentState = currentState.copy(showDialog = true)
        postStateToUI()
    }

    fun onCancelDialogClicked() {
        currentState = currentState.copy(
            showDialog = false,
            appBarIconType = AppBarIconType.CLOSE,
            isInEditMode = false
        )
        postStateToUI()
    }

    fun onDeletePaymentMethodClicked() {
        val newList = currentState.paymentMethodItems.items.filter { it != currentDeletedMethod }
//        deletePaymentMethodsUseCase.deletePaymentMethods(
//            paymentMethodId = currentDeletedMethod?.id ?: "",
//            onDeletePaymentMethodsSuccess = {
//                currentState = currentState.copy(
//                    paymentMethodItems = PaymentMethodItemViewEntity(newList),
//                    showDialog = false,
//                    appBarIconType = AppBarIconType.CLOSE,
//                    isInEditMode = false
//                )
//            },
//            onDeletePaymentMethodsFailed = {
//                currentState = currentState.copy(
//                    paymentMethodItems = PaymentMethodItemViewEntity(newList),
//                    showDialog = false,
//                    appBarIconType = AppBarIconType.CLOSE,
//                    isInEditMode = false
//                )
//            }
//        )

        currentState = currentState.copy(
            paymentMethodItems = PaymentMethodItemViewEntity(newList),
            isUsePaymentMethodButtonEnabled= false,
            showDialog = false,
            appBarIconType = AppBarIconType.CLOSE,
            isInEditMode = false
        )
        postStateToUI()
    }

    private fun postStateToUI() {
        mutableState.postValue(currentState)
    }
}
