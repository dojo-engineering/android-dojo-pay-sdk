package tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.core.MainCoroutineScopeRule
import tech.dojo.pay.uisdk.domain.DeletePaymentMethodsUseCase
import tech.dojo.pay.uisdk.domain.ObserveDeviceWalletState
import tech.dojo.pay.uisdk.domain.ObservePaymentMethods
import tech.dojo.pay.uisdk.domain.entities.FetchPaymentMethodsResult
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntity
import tech.dojo.pay.uisdk.domain.entities.PaymentMethodsDomainEntityItem
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.mapper.PaymentMethodItemViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.AppBarIconType
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.MangePaymentMethodsState
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntity
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class MangePaymentViewModelTest {

    @get:Rule
    @ExperimentalCoroutinesApi
    val coroutineScope = MainCoroutineScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val deletePaymentMethodsUseCase: DeletePaymentMethodsUseCase = mock()
    private val observeDeviceWalletState: ObserveDeviceWalletState = mock()
    private val observePaymentMethods: ObservePaymentMethods = mock()
    private val mapper: PaymentMethodItemViewEntityMapper = mock()

    @Test
    fun `test init state`() = runTest {
        // arrange
        val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
            MutableStateFlow(null)
        val isWalletEnabledStream: MutableStateFlow<Boolean?> = MutableStateFlow(null)
        whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
        whenever(observeDeviceWalletState.observe()).thenReturn(isWalletEnabledStream)
        fetchPaymentMethodsStream.tryEmit(FetchPaymentMethodsResult.Failure)
        isWalletEnabledStream.tryEmit(true)
        val expected = MangePaymentMethodsState(
            appBarIconType = AppBarIconType.CLOSE,
            paymentMethodItems = PaymentMethodItemViewEntity(emptyList()),
            isUsePaymentMethodButtonEnabled = true,
            currentSelectedMethod = null,
            showDialog = false,
            isInEditMode = false,
            isDeleteItemInProgress = false,
        )
        // act
        val viewMode = MangePaymentViewModel(
            deletePaymentMethodsUseCase,
            observeDeviceWalletState,
            observePaymentMethods,
            mapper,
        )
        val actual = viewMode.state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test state when wallet is enabled and saved payment methods exists`() = runTest {
        // arrange
        val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
            MutableStateFlow(null)
        val isWalletEnabledStream: MutableStateFlow<Boolean?> = MutableStateFlow(null)
        whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
        whenever(observeDeviceWalletState.observe()).thenReturn(isWalletEnabledStream)
        val paymentMethodsDomainEntity = PaymentMethodsDomainEntity(
            items = listOf(
                PaymentMethodsDomainEntityItem(
                    id = "id",
                    pan = "pan",
                    expiryDate = "expiryDate",
                    scheme = CardsSchemes.VISA,
                ),
            ),
        )
        val result = FetchPaymentMethodsResult.Success(result = paymentMethodsDomainEntity)
        val paymentMethodViewEntity = PaymentMethodItemViewEntity(
            items = listOf(
                PaymentMethodItemViewEntityItem.WalletItemItem,
                PaymentMethodItemViewEntityItem.CardItemItem(
                    id = "",
                    icon = R.drawable.ic_visa,
                    scheme = "Visa",
                    pan = "****9560",
                ),
            ),
        )
        whenever(mapper.apply(result, true)).thenReturn(paymentMethodViewEntity)
        fetchPaymentMethodsStream.tryEmit(result)
        isWalletEnabledStream.tryEmit(true)
        val expected = MangePaymentMethodsState(
            appBarIconType = AppBarIconType.CLOSE,
            paymentMethodItems = paymentMethodViewEntity,
            isUsePaymentMethodButtonEnabled = true,
            currentSelectedMethod = null,
            showDialog = false,
            isInEditMode = false,
            isDeleteItemInProgress = false,
        )
        // act
        val viewMode = MangePaymentViewModel(
            deletePaymentMethodsUseCase,
            observeDeviceWalletState,
            observePaymentMethods,
            mapper,
        )
        val actual = viewMode.state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test state when wallet is disabled and saved payment methods exists`() = runTest {
        // arrange
        val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
            MutableStateFlow(null)
        val isWalletEnabledStream: MutableStateFlow<Boolean?> = MutableStateFlow(null)
        whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
        whenever(observeDeviceWalletState.observe()).thenReturn(isWalletEnabledStream)
        val paymentMethodsDomainEntity = PaymentMethodsDomainEntity(
            items = listOf(
                PaymentMethodsDomainEntityItem(
                    id = "id",
                    pan = "pan",
                    expiryDate = "expiryDate",
                    scheme = CardsSchemes.VISA,
                ),
            ),
        )
        val result = FetchPaymentMethodsResult.Success(result = paymentMethodsDomainEntity)
        val paymentMethodViewEntity = PaymentMethodItemViewEntity(
            items = listOf(
                PaymentMethodItemViewEntityItem.CardItemItem(
                    id = "",
                    icon = R.drawable.ic_visa,
                    scheme = "Visa",
                    pan = "****9560",
                ),
            ),
        )
        whenever(mapper.apply(result, false)).thenReturn(paymentMethodViewEntity)
        fetchPaymentMethodsStream.tryEmit(result)
        isWalletEnabledStream.tryEmit(false)
        val expected = MangePaymentMethodsState(
            appBarIconType = AppBarIconType.CLOSE,
            paymentMethodItems = paymentMethodViewEntity,
            isUsePaymentMethodButtonEnabled = false,
            currentSelectedMethod = null,
            showDialog = false,
            isInEditMode = false,
            isDeleteItemInProgress = false,
        )
        // act
        val viewMode = MangePaymentViewModel(
            deletePaymentMethodsUseCase,
            observeDeviceWalletState,
            observePaymentMethods,
            mapper,
        )
        val actual = viewMode.state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test state when wallet is enabled and saved payment methods is empty`() = runTest {
        // arrange
        val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
            MutableStateFlow(null)
        val isWalletEnabledStream: MutableStateFlow<Boolean?> = MutableStateFlow(null)
        whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
        whenever(observeDeviceWalletState.observe()).thenReturn(isWalletEnabledStream)
        val paymentMethodsDomainEntity = PaymentMethodsDomainEntity(items = listOf())
        val result = FetchPaymentMethodsResult.Success(result = paymentMethodsDomainEntity)
        val paymentMethodViewEntity = PaymentMethodItemViewEntity(
            items = listOf(PaymentMethodItemViewEntityItem.WalletItemItem),
        )
        whenever(mapper.apply(result, true)).thenReturn(paymentMethodViewEntity)
        fetchPaymentMethodsStream.tryEmit(result)
        isWalletEnabledStream.tryEmit(true)
        val expected = MangePaymentMethodsState(
            appBarIconType = AppBarIconType.CLOSE,
            paymentMethodItems = paymentMethodViewEntity,
            isUsePaymentMethodButtonEnabled = true,
            currentSelectedMethod = null,
            showDialog = false,
            isInEditMode = false,
            isDeleteItemInProgress = false,
        )
        // act
        val viewMode = MangePaymentViewModel(
            deletePaymentMethodsUseCase,
            observeDeviceWalletState,
            observePaymentMethods,
            mapper,
        )
        val actual = viewMode.state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test state when wallet is disabled and saved payment methods is empty`() = runTest {
        // arrange
        val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
            MutableStateFlow(null)
        val isWalletEnabledStream: MutableStateFlow<Boolean?> = MutableStateFlow(null)
        whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
        whenever(observeDeviceWalletState.observe()).thenReturn(isWalletEnabledStream)
        val paymentMethodsDomainEntity = PaymentMethodsDomainEntity(items = listOf())
        val result = FetchPaymentMethodsResult.Success(result = paymentMethodsDomainEntity)
        val paymentMethodViewEntity = PaymentMethodItemViewEntity(items = listOf())
        whenever(mapper.apply(result, false)).thenReturn(paymentMethodViewEntity)
        fetchPaymentMethodsStream.tryEmit(result)
        isWalletEnabledStream.tryEmit(false)
        val expected = MangePaymentMethodsState(
            appBarIconType = AppBarIconType.CLOSE,
            paymentMethodItems = paymentMethodViewEntity,
            isUsePaymentMethodButtonEnabled = false,
            currentSelectedMethod = null,
            showDialog = false,
            isInEditMode = false,
            isDeleteItemInProgress = false,
        )
        // act
        val viewMode = MangePaymentViewModel(
            deletePaymentMethodsUseCase,
            observeDeviceWalletState,
            observePaymentMethods,
            mapper,
        )
        val actual = viewMode.state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test state when user select new payment method`() = runTest {
        // arrange
        val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
            MutableStateFlow(null)
        val isWalletEnabledStream: MutableStateFlow<Boolean?> = MutableStateFlow(null)
        whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
        whenever(observeDeviceWalletState.observe()).thenReturn(isWalletEnabledStream)
        val paymentMethodsDomainEntity = PaymentMethodsDomainEntity(
            items = listOf(
                PaymentMethodsDomainEntityItem(
                    id = "id",
                    pan = "pan",
                    expiryDate = "expiryDate",
                    scheme = CardsSchemes.VISA,
                ),
            ),
        )
        val result = FetchPaymentMethodsResult.Success(result = paymentMethodsDomainEntity)
        val paymentMethodViewEntity = PaymentMethodItemViewEntity(
            items = listOf(
                PaymentMethodItemViewEntityItem.WalletItemItem,
                PaymentMethodItemViewEntityItem.CardItemItem(
                    id = "",
                    icon = R.drawable.ic_visa,
                    scheme = "Visa",
                    pan = "****9560",
                ),
            ),
        )
        whenever(mapper.apply(result, true)).thenReturn(paymentMethodViewEntity)
        fetchPaymentMethodsStream.tryEmit(result)
        isWalletEnabledStream.tryEmit(true)
        val selectedPaymentMethods = PaymentMethodItemViewEntityItem.CardItemItem(
            id = "",
            icon = R.drawable.ic_visa,
            scheme = "Visa",
            pan = "****9560",
        )
        val expected = MangePaymentMethodsState(
            appBarIconType = AppBarIconType.CLOSE,
            paymentMethodItems = paymentMethodViewEntity,
            isUsePaymentMethodButtonEnabled = true,
            currentSelectedMethod = selectedPaymentMethods,
            showDialog = false,
            isInEditMode = false,
            isDeleteItemInProgress = false,
        )
        // act
        val viewMode = MangePaymentViewModel(
            deletePaymentMethodsUseCase,
            observeDeviceWalletState,
            observePaymentMethods,
            mapper,
        )
        viewMode.onPaymentMethodChanged(selectedPaymentMethods)
        val actual = viewMode.state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test state when user enter user long click on item `() = runTest {
        // arrange
        val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
            MutableStateFlow(null)
        val isWalletEnabledStream: MutableStateFlow<Boolean?> = MutableStateFlow(null)
        whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
        whenever(observeDeviceWalletState.observe()).thenReturn(isWalletEnabledStream)
        val paymentMethodsDomainEntity = PaymentMethodsDomainEntity(
            items = listOf(
                PaymentMethodsDomainEntityItem(
                    id = "id",
                    pan = "pan",
                    expiryDate = "expiryDate",
                    scheme = CardsSchemes.VISA,
                ),
            ),
        )
        val result = FetchPaymentMethodsResult.Success(result = paymentMethodsDomainEntity)
        val paymentMethodViewEntity = PaymentMethodItemViewEntity(
            items = listOf(
                PaymentMethodItemViewEntityItem.WalletItemItem,
                PaymentMethodItemViewEntityItem.CardItemItem(
                    id = "",
                    icon = R.drawable.ic_visa,
                    scheme = "Visa",
                    pan = "****9560",
                ),
            ),
        )
        whenever(mapper.apply(result, true)).thenReturn(paymentMethodViewEntity)
        fetchPaymentMethodsStream.tryEmit(result)
        isWalletEnabledStream.tryEmit(true)
        val selectedPaymentMethods = PaymentMethodItemViewEntityItem.CardItemItem(
            id = "",
            icon = R.drawable.ic_visa,
            scheme = "Visa",
            pan = "****9560",
        )
        val expected = MangePaymentMethodsState(
            appBarIconType = AppBarIconType.DELETE,
            paymentMethodItems = paymentMethodViewEntity,
            isUsePaymentMethodButtonEnabled = true,
            currentSelectedMethod = selectedPaymentMethods,
            showDialog = false,
            isInEditMode = true,
            isDeleteItemInProgress = false,
        )
        // act
        val viewMode = MangePaymentViewModel(
            deletePaymentMethodsUseCase,
            observeDeviceWalletState,
            observePaymentMethods,
            mapper,
        )
        viewMode.onPaymentMethodLongCLick(selectedPaymentMethods)
        val actual = viewMode.state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test state when user click on delete icon`() = runTest {
        // arrange
        val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
            MutableStateFlow(null)
        val isWalletEnabledStream: MutableStateFlow<Boolean?> = MutableStateFlow(null)
        whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
        whenever(observeDeviceWalletState.observe()).thenReturn(isWalletEnabledStream)
        val paymentMethodsDomainEntity = PaymentMethodsDomainEntity(
            items = listOf(
                PaymentMethodsDomainEntityItem(
                    id = "id",
                    pan = "pan",
                    expiryDate = "expiryDate",
                    scheme = CardsSchemes.VISA,
                ),
            ),
        )
        val result = FetchPaymentMethodsResult.Success(result = paymentMethodsDomainEntity)
        val paymentMethodViewEntity = PaymentMethodItemViewEntity(
            items = listOf(
                PaymentMethodItemViewEntityItem.WalletItemItem,
                PaymentMethodItemViewEntityItem.CardItemItem(
                    id = "",
                    icon = R.drawable.ic_visa,
                    scheme = "Visa",
                    pan = "****9560",
                ),
            ),
        )
        whenever(mapper.apply(result, true)).thenReturn(paymentMethodViewEntity)
        fetchPaymentMethodsStream.tryEmit(result)
        isWalletEnabledStream.tryEmit(true)
        val selectedPaymentMethods = PaymentMethodItemViewEntityItem.CardItemItem(
            id = "",
            icon = R.drawable.ic_visa,
            scheme = "Visa",
            pan = "****9560",
        )
        val expected = MangePaymentMethodsState(
            appBarIconType = AppBarIconType.DELETE,
            paymentMethodItems = paymentMethodViewEntity,
            isUsePaymentMethodButtonEnabled = true,
            currentSelectedMethod = selectedPaymentMethods,
            showDialog = true,
            isInEditMode = true,
            isDeleteItemInProgress = false,
        )
        // act
        val viewMode = MangePaymentViewModel(
            deletePaymentMethodsUseCase,
            observeDeviceWalletState,
            observePaymentMethods,
            mapper,
        )
        viewMode.onPaymentMethodLongCLick(selectedPaymentMethods)
        viewMode.onDeleteClicked()
        val actual = viewMode.state.value
        // assert
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test state when user click yes on the delete dialog`() = runTest {
        // arrange
        val fetchPaymentMethodsStream: MutableStateFlow<FetchPaymentMethodsResult?> =
            MutableStateFlow(null)
        val isWalletEnabledStream: MutableStateFlow<Boolean?> = MutableStateFlow(null)
        whenever(observePaymentMethods.observe()).thenReturn(fetchPaymentMethodsStream)
        whenever(observeDeviceWalletState.observe()).thenReturn(isWalletEnabledStream)
        val paymentMethodsDomainEntity = PaymentMethodsDomainEntity(
            items = listOf(
                PaymentMethodsDomainEntityItem(
                    id = "id",
                    pan = "pan",
                    expiryDate = "expiryDate",
                    scheme = CardsSchemes.VISA,
                ),
            ),
        )
        val result = FetchPaymentMethodsResult.Success(result = paymentMethodsDomainEntity)
        val paymentMethodViewEntity = PaymentMethodItemViewEntity(
            items = listOf(
                PaymentMethodItemViewEntityItem.WalletItemItem,
                PaymentMethodItemViewEntityItem.CardItemItem(
                    id = "id",
                    icon = R.drawable.ic_visa,
                    scheme = "Visa",
                    pan = "****9560",
                ),
            ),
        )
        whenever(mapper.apply(result, true)).thenReturn(paymentMethodViewEntity)
        fetchPaymentMethodsStream.tryEmit(result)
        isWalletEnabledStream.tryEmit(true)
        val selectedPaymentMethods = PaymentMethodItemViewEntityItem.CardItemItem(
            id = "id",
            icon = R.drawable.ic_visa,
            scheme = "Visa",
            pan = "****9560",
        )
        val expected = MangePaymentMethodsState(
            appBarIconType = AppBarIconType.DELETE,
            paymentMethodItems = paymentMethodViewEntity,
            isUsePaymentMethodButtonEnabled = true,
            currentSelectedMethod = selectedPaymentMethods,
            showDialog = true,
            isInEditMode = true,
            isDeleteItemInProgress = true,
        )
        // act
        val viewMode = MangePaymentViewModel(
            deletePaymentMethodsUseCase,
            observeDeviceWalletState,
            observePaymentMethods,
            mapper,
        )
        viewMode.onPaymentMethodLongCLick(selectedPaymentMethods)
        viewMode.onDeleteClicked()
        viewMode.onDeletePaymentMethodClicked()
        val actual = viewMode.state.value
        // assert
        Assert.assertEquals(expected, actual)
    }
}
