package tech.dojo.pay.uisdk.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.CardsSchemes
import tech.dojo.pay.sdk.card.entities.DojoGPayConfig
import tech.dojo.pay.sdk.card.entities.DojoSDKDebugConfig
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandler
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoSavedCardPaymentHandler
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoVirtualTerminalHandler
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.uisdk.DojoSDKDropInUI
import tech.dojo.pay.uisdk.core.StringProvider
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.entities.DojoPaymentFlowParams
import tech.dojo.pay.uisdk.entities.DojoPaymentType
import tech.dojo.pay.uisdk.presentation.components.WindowSize
import tech.dojo.pay.uisdk.presentation.components.rememberWindowSize
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.LocalDojoColors
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract
import tech.dojo.pay.uisdk.presentation.navigation.CUSTOMER_ID_PARAMS_KEY
import tech.dojo.pay.uisdk.presentation.navigation.DOJO_PAYMENT_RESULT_PARAMS_KEY
import tech.dojo.pay.uisdk.presentation.navigation.PaymentFlowNavigationEvents
import tech.dojo.pay.uisdk.presentation.navigation.PaymentFlowScreens
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.CardDetailsCheckoutScreen
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel.CardDetailsCheckoutViewModel
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel.CardDetailsCheckoutViewModelFactory
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.ManagePaymentMethods
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.viewmodel.MangePaymentViewModel
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.viewmodel.MangePaymentViewModelFactory
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.PaymentMethodsCheckOutScreen
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel.PaymentMethodCheckoutViewModel
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel.PaymentMethodCheckoutViewModelFactory
import tech.dojo.pay.uisdk.presentation.ui.result.ShowResultSheetScreen
import tech.dojo.pay.uisdk.presentation.ui.result.mapper.PaymentResultViewEntityMapper
import tech.dojo.pay.uisdk.presentation.ui.result.viewmodel.PaymentResultViewModel
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.VirtualTerminalCheckOutScreen
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.viewmodel.VirtualTerminalViewModel
import tech.dojo.pay.uisdk.presentation.ui.virtualterminalcheckout.viewmodel.VirtualTerminalViewModelFactory

class PaymentFlowContainerActivity : AppCompatActivity() {
    private val arguments: Bundle? by lazy { intent.extras }
    private lateinit var gpayPaymentHandler: DojoGPayHandler
    private lateinit var cardPaymentHandler: DojoCardPaymentHandler
    private lateinit var savedCardPaymentHandler: DojoSavedCardPaymentHandler
    private lateinit var virtualTerminalHandler: DojoVirtualTerminalHandler
    private var currentSelectedMethod: PaymentMethodItemViewEntityItem? = null
    private val paymentFlowViewModel: PaymentFlowViewModel by viewModels {
        PaymentFlowViewModelFactory(arguments)
    }
    private val flowStartDestination: PaymentFlowScreens by lazy { paymentFlowViewModel.getFlowStartDestination() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableScreenRecord()
        lockToPortrait()
        configureDojoSDKDebugConfig()
        configureDojoPayCore()
        setContent {
            DojoTheme {
                val forceLightMode = DojoSDKDropInUI.dojoThemeSettings?.forceLightMode ?: false
                val isDarkModeEnabled = isSystemInDarkTheme() && !forceLightMode
                val showDojoBrand = DojoSDKDropInUI.dojoThemeSettings?.showBranding ?: false
                val customColorPalette =
                    paymentFlowViewModel.getCustomColorPalette(isDarkModeEnabled)
                val windowSize = rememberWindowSize()
                CompositionLocalProvider(LocalDojoColors provides customColorPalette) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Black.copy(alpha = 0.2f),
                    ) {
                        val navController = rememberNavController()
                        // Listen for navigation event
                        val viewLifecycleOwner = LocalLifecycleOwner.current
                        LaunchedEffect(Unit) {
                            paymentFlowViewModel.navigationEvent.observe(viewLifecycleOwner) { onNavigationEvent(it, navController) }
                            paymentFlowViewModel.allowedCardsSchemes.observe(viewLifecycleOwner) { checkDeviceWalletState(it) }
                        }
                        PaymentFlowNavHost(
                            navController,
                            paymentFlowViewModel,
                            isDarkModeEnabled,
                            windowSize,
                            showDojoBrand,
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun lockToPortrait() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun disableScreenRecord() {
        if (!paymentFlowViewModel.isPaymentInSandBoxEnvironment()) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE,
            )
        }
    }

    private fun configureDojoPayCore() {
        configureDojoSDKDebugConfig()
        gpayPaymentHandler = DojoSdk.createGPayHandler(this) {
            paymentFlowViewModel.updateGpayPaymentState(false)
            paymentFlowViewModel.navigateToPaymentResult(it)
        }
        cardPaymentHandler = DojoSdk.createCardPaymentHandler(this) {
            paymentFlowViewModel.updatePaymentState(false)
            paymentFlowViewModel.navigateToPaymentResult(it)
        }
        savedCardPaymentHandler = DojoSdk.createSavedCardPaymentHandler(this) {
            paymentFlowViewModel.updatePaymentState(false)
            paymentFlowViewModel.navigateToPaymentResult(it)
        }
        virtualTerminalHandler = DojoSdk.createVirtualTerminalPaymentHandler(this) {
            paymentFlowViewModel.updatePaymentState(false)
            paymentFlowViewModel.navigateToPaymentResult(it)
        }
    }

    private fun configureDojoSDKDebugConfig() {
        if (DojoSDKDropInUI.dojoSDKDebugConfig != null) {
            DojoSDKDropInUI.dojoSDKDebugConfig?.let { DojoSdk.dojoSDKDebugConfig = it }
        } else {
            val dojoSDKDebugConfig = DojoSDKDebugConfig(
                isSandboxWallet = paymentFlowViewModel.isPaymentInSandBoxEnvironment(),
                isSandboxIntent = paymentFlowViewModel.isPaymentInSandBoxEnvironment(),
            )
            DojoSdk.dojoSDKDebugConfig = dojoSDKDebugConfig
        }
    }

    private fun checkDeviceWalletState(cardsSchemes: List<CardsSchemes>) {
        val gPayConfig = (arguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as? DojoPaymentFlowParams)?.GPayConfig
        if (gPayConfig != null) {
            DojoSdk.isGpayAvailable(
                activity = this,
                dojoGPayConfig = DojoGPayConfig(
                    merchantName = gPayConfig.merchantName,
                    merchantId = gPayConfig.merchantId,
                    gatewayMerchantId = gPayConfig.gatewayMerchantId,
                    allowedCardNetworks = cardsSchemes,
                ),
                { paymentFlowViewModel.updateDeviceWalletState(isAvailable = true) },
                { paymentFlowViewModel.updateDeviceWalletState(isAvailable = false) },
            )
        } else {
            paymentFlowViewModel.updateDeviceWalletState(isAvailable = false)
        }
    }

    private fun onNavigationEvent(
        event: PaymentFlowNavigationEvents?,
        navController: NavHostController,
    ) {
        when (event) {
            is PaymentFlowNavigationEvents.OnBack -> navController.popBackStack()
            is PaymentFlowNavigationEvents.OnCloseFlow -> this.finish()
            is PaymentFlowNavigationEvents.CLoseFlowWithInternalError -> {
                returnResult(DojoPaymentResult.SDK_INTERNAL_ERROR)
                this.finish()
            }

            is PaymentFlowNavigationEvents.PaymentResult -> {
                returnResult(event.dojoPaymentResult)
                navController.navigate(PaymentFlowScreens.PaymentResult.createRoute(event.dojoPaymentResult)) {
                    if (event.popBackStack) {
                        popUpTo(0)
                    }
                }
            }

            is PaymentFlowNavigationEvents.ManagePaymentMethods -> {
                navController.navigate(
                    PaymentFlowScreens.ManagePaymentMethods.createRoute(event.customerId),
                )
            }

            is PaymentFlowNavigationEvents.CardDetailsCheckout -> {
                navController.navigate(PaymentFlowScreens.CardDetailsCheckout.route)
            }

            is PaymentFlowNavigationEvents.PaymentMethodsCheckOutWithSelectedPaymentMethod -> {
                this.currentSelectedMethod = event.currentSelectedMethod
                navController.popBackStack()
            }

            is PaymentFlowNavigationEvents.CardDetailsCheckoutAsFirstScreen -> {
                navController.navigate(PaymentFlowScreens.CardDetailsCheckout.route) { popUpTo(0) }
            }

            is PaymentFlowNavigationEvents.VirtualTerminalCheckOutScreen -> {
                navController.navigate(PaymentFlowScreens.VirtualTerminalCheckOutScreen.route)
            }

            null -> {
                returnResult(DojoPaymentResult.SDK_INTERNAL_ERROR)
                this.finish()
            }
        }
    }

    @Suppress("LongMethod")
    @Composable
    internal fun PaymentFlowNavHost(
        navController: NavHostController,
        paymentFlowViewModel: PaymentFlowViewModel,
        isDarkModeEnabled: Boolean,
        windowSize: WindowSize,
        showDojoBrand: Boolean,
    ) {
        NavHost(
            navController = navController,
            startDestination = flowStartDestination.route,
        ) {
            paymentMethodCheckoutScreen(
                windowSize = windowSize,
                viewModel = paymentFlowViewModel,
                showDojoBrand = showDojoBrand,
            )
            managePaymentMethodsScreen(
                isDarkModeEnabled = isDarkModeEnabled,
                windowSize = windowSize,
                viewModel = paymentFlowViewModel,
                showDojoBrand = showDojoBrand,
            )
            paymentResultScreen(
                isDarkModeEnabled = isDarkModeEnabled,
                windowSize = windowSize,
                viewModel = paymentFlowViewModel,
                showDojoBrand = showDojoBrand,
            )
            cardDetailsCheckoutScreen(
                isDarkModeEnabled = isDarkModeEnabled,
                windowSize = windowSize,
                viewModel = paymentFlowViewModel,
                showDojoBrand = showDojoBrand,
            )
            virtualTerminalCheckOutScreen(
                isDarkModeEnabled = isDarkModeEnabled,
                windowSize = windowSize,
                viewModel = paymentFlowViewModel,
                showDojoBrand = showDojoBrand,
            )
        }
    }

    private fun NavGraphBuilder.paymentMethodCheckoutScreen(
        windowSize: WindowSize,
        viewModel: PaymentFlowViewModel,
        showDojoBrand: Boolean,
    ) {
        composable(
            route = PaymentFlowScreens.PaymentMethodCheckout.route,
        ) {
            val paymentMethodCheckoutViewModel: PaymentMethodCheckoutViewModel by viewModels {
                PaymentMethodCheckoutViewModelFactory(
                    savedCardPaymentHandler = savedCardPaymentHandler,
                    gpayPaymentHandler = gpayPaymentHandler,
                    arguments = arguments,
                )
            }
            // this is to  handle unregistered activity when screen orientation change
            paymentMethodCheckoutViewModel.updateSavedCardPaymentHandler(savedCardPaymentHandler)
            paymentMethodCheckoutViewModel.updateGpayHandler(gpayPaymentHandler)
            PaymentMethodsCheckOutScreen(
                windowSize = windowSize,
                currentSelectedMethod = currentSelectedMethod,
                viewModel = paymentMethodCheckoutViewModel,
                onAppBarIconClicked = {
                    returnResult(DojoPaymentResult.DECLINED)
                    viewModel.onCloseFlowClicked()
                },
                onManagePaymentClicked = viewModel::navigateToManagePaymentMethods,
                onPayByCard = viewModel::navigateToCardDetailsCheckoutScreen,
                showDojoBrand = showDojoBrand,
            )
        }
    }

    private fun NavGraphBuilder.managePaymentMethodsScreen(
        isDarkModeEnabled: Boolean,
        windowSize: WindowSize,
        viewModel: PaymentFlowViewModel,
        showDojoBrand: Boolean,
    ) {
        composable(
            route = PaymentFlowScreens.ManagePaymentMethods.route,
            arguments = listOf(
                navArgument(name = CUSTOMER_ID_PARAMS_KEY) {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
            ),
        ) {
            AnimatedVisibility(
                visible = true,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                val customerId = it.arguments?.getString(CUSTOMER_ID_PARAMS_KEY) ?: ""

                val mangePaymentViewModel: MangePaymentViewModel by viewModels {
                    MangePaymentViewModelFactory(
                        customerId = customerId,
                        arguments = arguments,
                        isDarkModeEnabled = isDarkModeEnabled,
                    )
                }
                ManagePaymentMethods(
                    windowSize = windowSize,
                    viewModel = mangePaymentViewModel,
                    onCloseClicked = {
                        returnResult(DojoPaymentResult.DECLINED)
                        viewModel.onCloseFlowClicked()
                    },
                    onBackClicked = viewModel::onBackClickedWithSavedPaymentMethod,
                    onNewCardButtonClicked = viewModel::navigateToCardDetailsCheckoutScreen,
                    showDojoBrand = showDojoBrand,
                )
            }
        }
    }

    private fun NavGraphBuilder.cardDetailsCheckoutScreen(
        isDarkModeEnabled: Boolean,
        windowSize: WindowSize,
        viewModel: PaymentFlowViewModel,
        showDojoBrand: Boolean,
    ) {
        composable(route = PaymentFlowScreens.CardDetailsCheckout.route) {
            val cardDetailsCheckoutViewModel: CardDetailsCheckoutViewModel by viewModels {
                CardDetailsCheckoutViewModelFactory(
                    dojoCardPaymentHandler = cardPaymentHandler,
                    isDarkModeEnabled = isDarkModeEnabled,
                    context = this@PaymentFlowContainerActivity,
                    isStartDestination = flowStartDestination == PaymentFlowScreens.CardDetailsCheckout,
                    arguments = arguments,
                ) { viewModel.navigateToPaymentResult(it) }
            }
            // this is to handle unregistered activity when screen orientation change
            cardDetailsCheckoutViewModel.updateCardPaymentHandler(cardPaymentHandler)
            AnimatedVisibility(
                visible = true,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                CardDetailsCheckoutScreen(
                    windowSize = windowSize,
                    viewModel = cardDetailsCheckoutViewModel,
                    onCloseClicked = {
                        returnResult(DojoPaymentResult.DECLINED)
                        viewModel.onCloseFlowClicked()
                    },
                    onBackClicked = {
                        if (flowStartDestination == PaymentFlowScreens.CardDetailsCheckout) {
                            returnResult(DojoPaymentResult.DECLINED)
                            viewModel.onCloseFlowClicked()
                        } else {
                            viewModel.onBackClicked()
                        }
                    },
                    isDarkModeEnabled = isDarkModeEnabled,
                    showDojoBrand = showDojoBrand,
                )
            }
        }
    }

    private fun NavGraphBuilder.virtualTerminalCheckOutScreen(
        isDarkModeEnabled: Boolean,
        windowSize: WindowSize,
        viewModel: PaymentFlowViewModel,
        showDojoBrand: Boolean,
    ) {
        composable(route = PaymentFlowScreens.VirtualTerminalCheckOutScreen.route) {
            val virtualMachineErrorViewModel: VirtualTerminalViewModel by viewModels {
                VirtualTerminalViewModelFactory(
                    isDarkModeEnabled,
                    virtualTerminalHandler,
                    this@PaymentFlowContainerActivity,
                    arguments,
                ) { viewModel.navigateToPaymentResult(it) }
            }
            VirtualTerminalCheckOutScreen(
                windowSize = windowSize,
                viewModel = virtualMachineErrorViewModel,
                onCloseClicked = {
                    returnResult(DojoPaymentResult.DECLINED)
                    viewModel.onCloseFlowClicked()
                },
                onBackClicked = {
                    returnResult(DojoPaymentResult.DECLINED)
                    viewModel.onCloseFlowClicked()
                },
                isDarkModeEnabled = isDarkModeEnabled,
                showDojoBrand = showDojoBrand,
            )
        }
    }

    private fun NavGraphBuilder.paymentResultScreen(
        isDarkModeEnabled: Boolean,
        windowSize: WindowSize,
        viewModel: PaymentFlowViewModel,
        showDojoBrand: Boolean,
    ) {
        composable(
            route = PaymentFlowScreens.PaymentResult.route,
            arguments = listOf(
                navArgument(name = DOJO_PAYMENT_RESULT_PARAMS_KEY) {
                    type = NavType.EnumType(DojoPaymentResult::class.java)
                    defaultValue = DojoPaymentResult.DECLINED
                    nullable = false
                },
            ),
        ) {
            val result = it.arguments?.get(DOJO_PAYMENT_RESULT_PARAMS_KEY) as DojoPaymentResult
            val observePaymentIntent =
                ObservePaymentIntent(PaymentFlowViewModelFactory.paymentIntentRepository)
            val paymentType =
                (
                    arguments?.getSerializable(DojoPaymentFlowHandlerResultContract.KEY_PARAMS) as?
                        DojoPaymentFlowParams
                    )?.paymentType ?: DojoPaymentType.PAYMENT_CARD
            val paymentResultViewEntityMapper = PaymentResultViewEntityMapper(
                stringProvider = StringProvider(this@PaymentFlowContainerActivity),
                paymentType = paymentType,
                isDarkModeEnabled = isDarkModeEnabled,
            )
            val paymentResultViewModel = PaymentResultViewModel(
                result = result,
                observePaymentIntent = observePaymentIntent,
                paymentResultViewEntityMapper = paymentResultViewEntityMapper,
            )
            AnimatedVisibility(
                visible = true,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                ShowResultSheetScreen(
                    windowSize = windowSize,
                    onCloseFlowClicked = viewModel::onCloseFlowClicked,
                    onTryAgainClicked = viewModel::onBackClicked,
                    viewModel = paymentResultViewModel,
                    showDojoBrand = showDojoBrand,
                )
            }
        }
    }

    private fun returnResult(result: DojoPaymentResult) {
        val data = Intent()
        data.putExtra(DojoPaymentFlowHandlerResultContract.KEY_RESULT, result)
        setResult(RESULT_OK, data)
        overridePendingTransition(200, tech.dojo.pay.sdk.R.anim.exit)
    }
}
