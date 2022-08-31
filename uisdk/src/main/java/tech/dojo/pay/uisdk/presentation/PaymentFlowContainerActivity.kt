package tech.dojo.pay.uisdk.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
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
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import tech.dojo.pay.sdk.DojoPaymentResult
import tech.dojo.pay.sdk.DojoSdk
import tech.dojo.pay.sdk.card.presentation.card.handler.DojoCardPaymentHandler
import tech.dojo.pay.sdk.card.presentation.gpay.handler.DojoGPayHandler
import tech.dojo.pay.uisdk.DojoSDKDropInUI
import tech.dojo.pay.uisdk.domain.ObservePaymentIntent
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.LocalDojoColors
import tech.dojo.pay.uisdk.presentation.components.theme.lightColorPalette
import tech.dojo.pay.uisdk.presentation.contract.DojoPaymentFlowHandlerResultContract
import tech.dojo.pay.uisdk.presentation.navigation.PaymentFlowNavigationEvents
import tech.dojo.pay.uisdk.presentation.navigation.PaymentFlowScreens
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.CardDetailsCheckoutScreen
import tech.dojo.pay.uisdk.presentation.ui.carddetailscheckout.viewmodel.CardDetailsCheckoutViewModel
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.ManagePaymentMethods
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.PaymentMethodsCheckOutScreen
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel.PaymentMethodCheckoutViewModel
import tech.dojo.pay.uisdk.presentation.ui.paymentmethodcheckout.viewmodel.PaymentMethodCheckoutViewModelFactory
import tech.dojo.pay.uisdk.presentation.ui.result.ShowResultSheetScreen
import tech.dojo.pay.uisdk.presentation.ui.result.viewmodel.PaymentResultViewModel

class PaymentFlowContainerActivity : AppCompatActivity() {
    private val arguments: Bundle? by lazy { intent.extras }
    private lateinit var gpayPaymentHandler: DojoGPayHandler
    private lateinit var cardPaymentHandler: DojoCardPaymentHandler
    private val viewModel: PaymentFlowViewModel by viewModels { PaymentFlowViewModelFactory(arguments) }

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureDojoPayCore()
        setContent {
            DojoTheme {
                val customColorPalette = lightColorPalette(DojoSDKDropInUI.dojoThemeSettings)
                CompositionLocalProvider(LocalDojoColors provides customColorPalette) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Black.copy(alpha = 0.2f)
                    ) {
                        val navController = rememberAnimatedNavController()
                        // Listen for navigation event
                        val viewLifecycleOwner = LocalLifecycleOwner.current
                        LaunchedEffect(Unit) {
                            viewModel.navigationEvent.observe(viewLifecycleOwner) {
                                onNavigationEvent(it, navController)
                            }
                        }
                        PaymentFlowNavHost(navController, viewModel)
                    }
                }
            }
        }
    }

    private fun configureDojoPayCore() {
        DojoSdk.cardSandbox = false
        DojoSdk.walletSandBox = DojoSDKDropInUI.sandbox
        gpayPaymentHandler = DojoSdk.createGPayHandler(this) {
            viewModel.navigateToPaymentResult(it)
        }
        cardPaymentHandler = DojoSdk.createCardPaymentHandler(this) {
            viewModel.navigateToPaymentResult(it)
        }
    }

    private fun onNavigationEvent(
        event: PaymentFlowNavigationEvents?,
        navController: NavHostController
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
                navController.navigate(PaymentFlowScreens.ManagePaymentMethods.rout)
            }
            is PaymentFlowNavigationEvents.CardDetailsCheckout -> {
                navController.navigate(PaymentFlowScreens.CardDetailsCheckout.rout)
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun PaymentFlowNavHost(
        navController: NavHostController,
        viewModel: PaymentFlowViewModel
    ) {
        AnimatedNavHost(
            navController = navController,
            startDestination = PaymentFlowScreens.PaymentMethodCheckout.rout,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable(
                route = PaymentFlowScreens.PaymentMethodCheckout.rout,
            ) {
                val paymentMethodCheckoutViewModel: PaymentMethodCheckoutViewModel by viewModels {
                    PaymentMethodCheckoutViewModelFactory(gpayPaymentHandler)
                }
                PaymentMethodsCheckOutScreen(
                    paymentMethodCheckoutViewModel,
                    {
                        returnResult(DojoPaymentResult.DECLINED)
                        viewModel.onCloseFlowClicked()
                    },
                    viewModel::navigateToManagePaymentMethods
                )
            }
            composable(
                route = PaymentFlowScreens.PaymentResult.rout,
                arguments = listOf(
                    navArgument(name = "dojoPaymentResult") {
                        type = NavType.EnumType(DojoPaymentResult::class.java)
                        defaultValue = DojoPaymentResult.DECLINED
                        nullable = false
                    }
                ),
            ) {
                val result = it.arguments?.get("dojoPaymentResult") as DojoPaymentResult
                val paymentResultViewModel = PaymentResultViewModel(result)
                AnimatedVisibility(
                    visible = true,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    ShowResultSheetScreen(
                        viewModel::onCloseFlowClicked,
                        viewModel::onBackClicked,
                        paymentResultViewModel
                    )
                }
            }
            composable(route = PaymentFlowScreens.ManagePaymentMethods.rout) {
                AnimatedVisibility(
                    visible = true,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    ManagePaymentMethods(
                        {
                            returnResult(DojoPaymentResult.DECLINED)
                            viewModel.onCloseFlowClicked()
                        },
                        viewModel::onBackClicked,
                        viewModel::navigateToCardDetailsCheckoutScreen
                    )
                }
            }

            composable(route = PaymentFlowScreens.CardDetailsCheckout.rout) {
                val cardDetailsCheckoutViewModel = CardDetailsCheckoutViewModel(
                    ObservePaymentIntent(PaymentFlowViewModelFactory.paymentIntentRepository),
                    cardPaymentHandler
                )

                AnimatedVisibility(
                    visible = true,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    CardDetailsCheckoutScreen(
                        cardDetailsCheckoutViewModel,
                        {
                            returnResult(DojoPaymentResult.DECLINED)
                            viewModel.onCloseFlowClicked()
                        },
                        viewModel::onBackClicked
                    )
                }
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
