package tech.dojo.pay.uisdk.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.entities.DojoSDKDebugConfig
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandler
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoSavedCardPaymentHandler
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoVirtualTerminalHandler
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.uisdk.DojoSDKDropInUI
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.domain.RefreshPaymentIntentUseCase
import tech.dojo.pay.uisdk.entities.DarkColorPalette
import tech.dojo.pay.uisdk.entities.LightColorPalette
import tech.dojo.pay.uisdk.presentation.components.WindowSize
import tech.dojo.pay.uisdk.presentation.components.rememberWindowSize
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.LocalDojoColors
import tech.dojo.pay.uisdk.presentation.components.theme.darkColorPalette
import tech.dojo.pay.uisdk.presentation.components.theme.lightColorPalette
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract
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
    private val viewModel: PaymentFlowViewModel by viewModels {
        PaymentFlowViewModelFactory(
            arguments,
        )
    }
    private val flowStartDestination: PaymentFlowScreens by lazy { viewModel.getFlowStartDestination() }

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureDojoPayCore()
        setContent {
            DojoTheme() {
                val forceLightMode = DojoSDKDropInUI.dojoThemeSettings?.forceLightMode ?: false
                val isDarkModeEnabled = isSystemInDarkTheme() && !forceLightMode
                val showDojoBrand = DojoSDKDropInUI.dojoThemeSettings?.showBranding ?: false
                val customColorPalette =
                    if (isDarkModeEnabled) {
                        darkColorPalette(
                            DojoSDKDropInUI.dojoThemeSettings?.DarkColorPalette ?: DarkColorPalette(),
                        )
                    } else {
                        lightColorPalette(
                            DojoSDKDropInUI.dojoThemeSettings?.lightColorPalette ?: LightColorPalette(),
                        )
                    }
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
                            viewModel.navigationEvent.observe(viewLifecycleOwner) {
                                onNavigationEvent(it, navController)
                            }
                        }
                        PaymentFlowNavHost(navController, viewModel, isDarkModeEnabled, windowSize, showDojoBrand)
                    }
                }
            }
        }
    }

    private fun configureDojoPayCore() {
        configureDojoSDKDebugConfig()
        gpayPaymentHandler = DojoSdk.createGPayHandler(this) {
            viewModel.updateGpayPaymentState(false)
            viewModel.navigateToPaymentResult(it)
        }
        cardPaymentHandler = DojoSdk.createCardPaymentHandler(this) {
            viewModel.updatePaymentState(false)
            viewModel.navigateToPaymentResult(it)
        }
        savedCardPaymentHandler = DojoSdk.createSavedCardPaymentHandler(this) {
            viewModel.updatePaymentState(false)
            viewModel.navigateToPaymentResult(it)
        }
        virtualTerminalHandler = DojoSdk.createVirtualTerminalPaymentHandler(this) {
            viewModel.updatePaymentState(false)
            viewModel.navigateToPaymentResult(it)
        }
    }

    private fun configureDojoSDKDebugConfig() {
        if (DojoSDKDropInUI.dojoSDKDebugConfig != null) {
            DojoSDKDropInUI.dojoSDKDebugConfig?.let { DojoSdk.dojoSDKDebugConfig = it }
        } else {
            val dojoSDKDebugConfig = DojoSDKDebugConfig(
                isSandboxWallet = viewModel.isPaymentInSandBoxEnvironment(),
                isSandboxIntent = viewModel.isPaymentInSandBoxEnvironment()
            )
            DojoSdk.dojoSDKDebugConfig = dojoSDKDebugConfig
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
                navController.navigate(PaymentFlowScreens.PaymentResult.createRout(event.dojoPaymentResult)) {
                    if (event.popBackStack) {
                        popUpTo(0)
                    }
                }
            }
            is PaymentFlowNavigationEvents.ManagePaymentMethods -> {
                navController.navigate(
                    PaymentFlowScreens.ManagePaymentMethods.createRout(event.customerId),
                )
            }
            is PaymentFlowNavigationEvents.CardDetailsCheckout -> {
                navController.navigate(PaymentFlowScreens.CardDetailsCheckout.rout)
            }
            is PaymentFlowNavigationEvents.PaymentMethodsCheckOutWithSelectedPaymentMethod -> {
                this.currentSelectedMethod = event.currentSelectedMethod
                navController.popBackStack()
            }
            is PaymentFlowNavigationEvents.CardDetailsCheckoutAsFirstScreen -> {
                navController.navigate(PaymentFlowScreens.CardDetailsCheckout.rout) { popUpTo(0) }
            }
            is PaymentFlowNavigationEvents.VirtualTerminalCheckOutScreen -> {
                navController.navigate(PaymentFlowScreens.VirtualTerminalCheckOutScreen.rout)
            }
            null -> {
                returnResult(DojoPaymentResult.SDK_INTERNAL_ERROR)
                this.finish()
            }
        }
    }

    @Suppress("LongMethod")
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    internal fun PaymentFlowNavHost(
        navController: NavHostController,
        viewModel: PaymentFlowViewModel,
        isDarkModeEnabled: Boolean,
        windowSize: WindowSize,
        showDojoBrand: Boolean
    ) {
        NavHost(
            navController = navController,
            startDestination = flowStartDestination.rout,
        ) {
            composable(
                route = PaymentFlowScreens.PaymentMethodCheckout.rout,
            ) {
                val paymentMethodCheckoutViewModel: PaymentMethodCheckoutViewModel by viewModels {
                    PaymentMethodCheckoutViewModelFactory(
                        savedCardPaymentHandler,
                        gpayPaymentHandler,
                        arguments,
                    )
                }
                // this is to  handle unregistered activity when screen orientation change
                paymentMethodCheckoutViewModel.updateSavedCardPaymentHandler(savedCardPaymentHandler)
                paymentMethodCheckoutViewModel.updateGpayHandler(gpayPaymentHandler)
                PaymentMethodsCheckOutScreen(
                    windowSize,
                    currentSelectedMethod,
                    paymentMethodCheckoutViewModel,
                    {
                        returnResult(DojoPaymentResult.DECLINED)
                        viewModel.onCloseFlowClicked()
                    },
                    viewModel::navigateToManagePaymentMethods,
                    viewModel::navigateToCardDetailsCheckoutScreen,
                    showDojoBrand
                )
            }

            composable(
                route = PaymentFlowScreens.PaymentResult.rout,
                arguments = listOf(
                    navArgument(name = "dojoPaymentResult") {
                        type = NavType.EnumType(DojoPaymentResult::class.java)
                        defaultValue = DojoPaymentResult.DECLINED
                        nullable = false
                    },
                ),
            ) {
                val result = it.arguments?.get("dojoPaymentResult") as DojoPaymentResult
                val refreshPaymentIntent =
                    RefreshPaymentIntentUseCase(PaymentFlowViewModelFactory.paymentIntentRepository)
                val observePaymentIntent =
                    ObservePaymentIntent(PaymentFlowViewModelFactory.paymentIntentRepository)
                val paymentResultViewModel =
                    PaymentResultViewModel(
                        result,
                        observePaymentIntent,
                        refreshPaymentIntent,
                        isDarkModeEnabled,
                    )
                AnimatedVisibility(
                    visible = true,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                ) {
                    ShowResultSheetScreen(
                        windowSize,
                        viewModel::onCloseFlowClicked,
                        viewModel::onBackClicked,
                        paymentResultViewModel,
                        showDojoBrand
                    )
                }
            }

            composable(
                route = PaymentFlowScreens.ManagePaymentMethods.rout,
                arguments = listOf(
                    navArgument(name = "customerId") {
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
                    val customerId = it.arguments?.get("customerId") as String

                    val mangePaymentViewModel: MangePaymentViewModel by viewModels {
                        MangePaymentViewModelFactory(
                            customerId,
                            arguments,
                            isDarkModeEnabled,
                        )
                    }
                    ManagePaymentMethods(
                        windowSize,
                        mangePaymentViewModel,
                        {
                            returnResult(DojoPaymentResult.DECLINED)
                            viewModel.onCloseFlowClicked()
                        },
                        viewModel::onBackClickedWithSavedPaymentMethod,
                        viewModel::navigateToCardDetailsCheckoutScreen,
                        showDojoBrand
                    )
                }
            }

            composable(route = PaymentFlowScreens.CardDetailsCheckout.rout) {
                val cardDetailsCheckoutViewModel: CardDetailsCheckoutViewModel by viewModels {
                    CardDetailsCheckoutViewModelFactory(cardPaymentHandler, isDarkModeEnabled, virtualTerminalHandler)
                }
                // this is to  handle unregistered activity when screen orientation change
                cardDetailsCheckoutViewModel.updateCardPaymentHandler(cardPaymentHandler, virtualTerminalHandler)
                AnimatedVisibility(
                    visible = true,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                ) {
                    CardDetailsCheckoutScreen(
                        windowSize,
                        cardDetailsCheckoutViewModel,
                        {
                            returnResult(DojoPaymentResult.DECLINED)
                            viewModel.onCloseFlowClicked()
                        },
                        viewModel::onBackClicked,
                        isDarkModeEnabled,
                        showDojoBrand
                    )
                }
            }

            composable(route = PaymentFlowScreens.VirtualTerminalCheckOutScreen.rout) {
                val virtualMachineErrorViewModel: VirtualTerminalViewModel by viewModels {
                    VirtualTerminalViewModelFactory(isDarkModeEnabled, virtualTerminalHandler)
                }
                VirtualTerminalCheckOutScreen(
                    windowSize,
                    virtualMachineErrorViewModel,
                    {
                        returnResult(DojoPaymentResult.DECLINED)
                        viewModel.onCloseFlowClicked()
                    },
                    {
                        returnResult(DojoPaymentResult.DECLINED)
                        viewModel.onCloseFlowClicked()
                    },
                    isDarkModeEnabled,
                    showDojoBrand
                )
            }
        }
    }
    private fun returnResult(result: DojoPaymentResult) {
        val data = Intent()
        data.putExtra(DojoPaymentFlowHandlerResultContract.KEY_RESULT, result)
        setResult(RESULT_OK, data)
        overridePendingTransition(0, tech.dojo.pay.sdk.R.anim.exit)
    }
}
