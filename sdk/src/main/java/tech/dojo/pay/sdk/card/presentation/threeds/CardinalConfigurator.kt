package tech.dojo.pay.sdk.card.presentation.threeds

import android.content.Context
import android.util.Log
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalEnvironment
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalRenderType
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalUiType
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalConfigurationParameters
import com.cardinalcommerce.shared.userinterfaces.UiCustomization
import org.json.JSONArray
import tech.dojo.pay.sdk.DojoSdk

class CardinalConfigurator(private val context: Context) {

    companion object {
        private const val TAG = "CardinalConfigurator"
    }

    fun getConfiguredCardinalInstance(): Cardinal {
        Log.d(TAG, "getConfiguredCardinalInstance: Configuring Cardinal SDK instance")
        val cardinal: Cardinal = Cardinal.getInstance()
        val cardinalConfigurationParameters = CardinalConfigurationParameters()
        val env = getEnvironment()
        cardinalConfigurationParameters.environment = env
        Log.d(TAG, "getConfiguredCardinalInstance: environment=$env, requestTimeout=8000, challengeTimeout=5")

        cardinalConfigurationParameters.requestTimeout = 8000
        cardinalConfigurationParameters.challengeTimeout = 5

        cardinalConfigurationParameters.renderType = getRenderType()
        cardinalConfigurationParameters.uiType = CardinalUiType.BOTH

        val yourUICustomizationObject = UiCustomization()
        cardinalConfigurationParameters.uiCustomization = yourUICustomizationObject

        cardinal.configure(context, cardinalConfigurationParameters)
        Log.d(TAG, "getConfiguredCardinalInstance: Cardinal SDK configured successfully")
        return cardinal
    }

    private fun getRenderType(): JSONArray {
        val rTYPE = JSONArray()
        rTYPE.put(CardinalRenderType.OTP)
        rTYPE.put(CardinalRenderType.SINGLE_SELECT)
        rTYPE.put(CardinalRenderType.MULTI_SELECT)
        rTYPE.put(CardinalRenderType.OOB)
        rTYPE.put(CardinalRenderType.HTML)
        return rTYPE
    }

    private fun getEnvironment(): CardinalEnvironment {
        return if (DojoSdk.dojoSDKDebugConfig.isSandboxIntent) {
            CardinalEnvironment.STAGING
        } else {
            CardinalEnvironment.PRODUCTION
        }
    }
}
