package tech.dojo.pay.sdk.card.presentation.threeds

import android.content.Context
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalEnvironment
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalRenderType
import com.cardinalcommerce.cardinalmobilesdk.enums.CardinalUiType
import com.cardinalcommerce.cardinalmobilesdk.models.CardinalConfigurationParameters
import com.cardinalcommerce.shared.userinterfaces.UiCustomization
import org.json.JSONArray
import tech.dojo.pay.sdk.DojoSdk

class CardinalConfigurator(private val context: Context) {

    fun getConfiguredCardinalInstance(): Cardinal {
        val cardinal: Cardinal = Cardinal.getInstance()
        val cardinalConfigurationParameters = CardinalConfigurationParameters()
        cardinalConfigurationParameters.environment = getEnvironment()

        cardinalConfigurationParameters.requestTimeout = 8000
        cardinalConfigurationParameters.challengeTimeout = 5

        cardinalConfigurationParameters.renderType = getRenderType()
        cardinalConfigurationParameters.uiType = CardinalUiType.BOTH

        val yourUICustomizationObject = UiCustomization()
        cardinalConfigurationParameters.uiCustomization = yourUICustomizationObject

        cardinal.configure(context, cardinalConfigurationParameters)
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
        return if (DojoSdk.isCardSandBox) {
            CardinalEnvironment.STAGING
        } else {
            CardinalEnvironment.PRODUCTION
        }
    }
}
