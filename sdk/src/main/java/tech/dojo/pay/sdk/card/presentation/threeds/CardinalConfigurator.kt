package tech.dojo.pay.sdk.card.presentation.threeds

import android.content.Context
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.enums.CCADatabase
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
        cardinalConfigurationParameters.setCCAUrl(getDataCenter())

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

    private fun getEnvironment(): CardinalEnvironment =
        if (DojoSdk.dojoSDKDebugConfig.isSandboxIntent) {
            CardinalEnvironment.STAGING
        } else {
            CardinalEnvironment.PRODUCTION
        }

    private fun getDataCenter(): CCADatabase = dataCenterFor(
        isSandbox = DojoSdk.dojoSDKDebugConfig.isSandboxIntent,
        currentTimeMillis = System.currentTimeMillis(),
    )

    internal companion object {
        // 2026-10-29T00:00:00Z
        const val VISA_PRODUCTION_CUTOVER_MILLIS = 1793232000000L

        fun dataCenterFor(
            isSandbox: Boolean,
            currentTimeMillis: Long,
        ): CCADatabase = if (
            isSandbox || currentTimeMillis >= VISA_PRODUCTION_CUTOVER_MILLIS
        ) {
            CCADatabase.CGKURLS
        } else {
            CCADatabase.CCAURLS
        }
    }
}
