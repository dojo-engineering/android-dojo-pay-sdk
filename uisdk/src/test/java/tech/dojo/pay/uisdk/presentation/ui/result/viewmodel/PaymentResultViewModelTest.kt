import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.core.StringProvider
import tech.dojo.pay.uisdk.presentation.ui.result.viewmodel.PaymentResultViewModel

class PaymentResultViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: PaymentResultViewModel
    private lateinit var result: DojoPaymentResult
    private lateinit var stringProvider: StringProvider

    @Before
    fun setUp() {
        stringProvider = mockk()
        every { stringProvider.getString(any()) } returns ""
    }

//    @Test
//    fun `onTryAgainClicked should update state when result is FAILED`() {
//        // arrange
//        result = DojoPaymentResult.SUCCESSFUL
//        val observer = mockk<Observer<PaymentResultState>>(relaxUnitFun = true)
//        val initialState = PaymentResultState.FailedResult(
//            appBarTitle = "",
//            imageId = R.drawable.ic_error_dark,
//            isTryAgainLoading = false,
//            shouldNavigateToPreviousScreen = false,
//            status = "",
//            orderInfo = "",
//            details = R.string.dojo_ui_sdk_payment_result_failed_description,
//        )
//        viewModel = PaymentResultViewModel(result, true, stringProvider)
//        viewModel.state.observeForever(observer)
//
//        // act
//        viewModel.onTryAgainClicked()
//
//        // assert
//        verify { observer.onChanged(initialState.copy(shouldNavigateToPreviousScreen = true)) }
//    }
}
