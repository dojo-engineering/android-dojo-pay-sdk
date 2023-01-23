package tech.dojo.pay.sdk.card.presentation.threeds

import androidx.lifecycle.ViewModel
import com.cardinalcommerce.cardinalmobilesdk.services.CardinalInitService


abstract class Dojo3DSBaseViewModel(
    cardinalConfigurator: CardinalConfigurator
) : ViewModel(), CardinalInitService {
    val configureDCardinalInstance = cardinalConfigurator.getConfiguredCardinalInstance()
}
