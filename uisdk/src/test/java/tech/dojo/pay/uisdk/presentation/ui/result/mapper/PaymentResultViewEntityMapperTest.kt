package tech.dojo.pay.uisdk.presentation.ui.result.mapper

import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.core.StringProvider
import tech.dojo.pay.uisdk.entities.DojoPaymentType
import tech.dojo.pay.uisdk.presentation.ui.CustomStringProvider
import tech.dojo.pay.uisdk.presentation.ui.result.state.PaymentResultState

class PaymentResultViewEntityMapperTest {

    private var mockStringProvider: StringProvider = mock()
    private val customStringProvider: CustomStringProvider = mock()
    private lateinit var mapper: PaymentResultViewEntityMapper

    private fun buildMapper(type: DojoPaymentType) = PaymentResultViewEntityMapper(
        stringProvider = mockStringProvider,
        paymentType = type,
        isDarkModeEnabled = false,
        customStringProvider = customStringProvider
    )

    @Test
    fun `when calling mapTpResultState with successful result with for PAYMENT_CARD payment type should return successfulResult with correct fields`() {
        // arrange
        given(mockStringProvider.getString(R.string.dojo_ui_sdk_payment_result_title_success))
            .willReturn("Successful Payment")
        val expected = PaymentResultState.SuccessfulResult(
            appBarTitle = "Successful Payment",
            imageId = 2131230888,
            status = "Successful Payment",
            orderInfo = "",
            description = "",
        )

        // act
        mapper = buildMapper(type = DojoPaymentType.PAYMENT_CARD)
        val result = mapper.mapTpResultState(DojoPaymentResult.SUCCESSFUL)

        // Assert
        assert(result is PaymentResultState.SuccessfulResult)
        val actual = result as PaymentResultState.SuccessfulResult
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when calling mapTpResultState with successful result with for SETUP_INTENT payment type should return successfulResult with correct fields`() {
        // arrange
        given(mockStringProvider.getString(R.string.dojo_ui_sdk_payment_result_main_title_setup_intent_success))
            .willReturn("Successful Setup Intent")
        given(mockStringProvider.getString(R.string.dojo_ui_sdk_payment_result_title_setup_intent_success))
            .willReturn("Successful Setup Intent")
        val expected = PaymentResultState.SuccessfulResult(
            appBarTitle = "Successful Setup Intent",
            imageId = 2131230888,
            status = "Successful Setup Intent",
            orderInfo = "",
            description = "",
        )

        // act
        mapper = buildMapper(type = DojoPaymentType.SETUP_INTENT)
        val result = mapper.mapTpResultState(DojoPaymentResult.SUCCESSFUL)

        // Assert
        assert(result is PaymentResultState.SuccessfulResult)
        val actual = result as PaymentResultState.SuccessfulResult
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when calling mapTpResultState with FAILED result with for SETUP_INTENT payment type should return FailedResult with correct fields`() {
        // arrange
        given(mockStringProvider.getString(R.string.dojo_ui_sdk_payment_result_main_message_setup_intent_fail))
            .willReturn("Failed Setup Intent")
        given(mockStringProvider.getString(R.string.dojo_ui_sdk_payment_result_main_title_setup_intent_fail))
            .willReturn("Failed Setup Intent")
        given(mockStringProvider.getString(R.string.dojo_ui_sdk_payment_result_title_setup_intent_fail))
            .willReturn("Failed Setup Intent")
        val expected = PaymentResultState.FailedResult(
            appBarTitle = "Failed Setup Intent",
            imageId = 2131230871,
            status = "Failed Setup Intent",
            details = "Failed Setup Intent",
            shouldNavigateToPreviousScreen = false,
        )

        // act
        mapper = buildMapper(type = DojoPaymentType.SETUP_INTENT)
        val result = mapper.mapTpResultState(DojoPaymentResult.FAILED)

        // Assert
        assert(result is PaymentResultState.FailedResult)
        val actual = result as PaymentResultState.FailedResult
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when calling mapTpResultState with FAILED result with for PAYMENT_CARD payment type should return FailedResult with correct fields`() {
        // arrange
        given(mockStringProvider.getString(R.string.dojo_ui_sdk_payment_result_title_fail))
            .willReturn("Failed Setup Intent")
        given(mockStringProvider.getString(R.string.dojo_ui_sdk_payment_result_title_fail))
            .willReturn("Failed Setup Intent")
        given(mockStringProvider.getString(R.string.dojo_ui_sdk_payment_result_failed_description))
            .willReturn("Failed Setup Intent")
        val expected = PaymentResultState.FailedResult(
            appBarTitle = "Failed Setup Intent",
            imageId = 2131230871,
            status = "Failed Setup Intent",
            details = "Failed Setup Intent",
            shouldNavigateToPreviousScreen = false,
        )

        // act
        mapper = buildMapper(type = DojoPaymentType.PAYMENT_CARD)
        val result = mapper.mapTpResultState(DojoPaymentResult.FAILED)

        // Assert
        assert(result is PaymentResultState.FailedResult)
        val actual = result as PaymentResultState.FailedResult
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `when calling mapToOrderIdField should return formatted order id field `() {
        // arrange
        given(mockStringProvider.getString(R.string.dojo_ui_sdk_order_info))
            .willReturn("Order ID:")

        val orderId = "123456"
        val expectedResult = "Order ID: 123456"
        // act
        mapper = buildMapper(type = DojoPaymentType.PAYMENT_CARD)
        val mappedOrderId = mapper.mapToOrderIdField(orderId)
        Assert.assertEquals(expectedResult, mappedOrderId)
    }
}
