package tech.dojo.pay.sdk.card.presentation.threeds

import androidx.lifecycle.ViewModel
import com.cardinalcommerce.cardinalmobilesdk.services.CardinalInitService
import com.cardinalcommerce.cardinalmobilesdk.services.CardinalValidateReceiver


abstract class Dojo3DSBaseViewModel(
    cardinalConfigurator: CardinalConfigurator
) : ViewModel(), CardinalInitService, CardinalValidateReceiver {
    val configureDCardinalInstance = cardinalConfigurator.getConfiguredCardinalInstance()
}
