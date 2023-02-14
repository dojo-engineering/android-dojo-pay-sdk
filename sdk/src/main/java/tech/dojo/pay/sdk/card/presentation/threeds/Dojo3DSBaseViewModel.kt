package tech.dojo.pay.sdk.card.presentation.threeds

import androidx.lifecycle.ViewModel
import com.cardinalcommerce.cardinalmobilesdk.Cardinal
import com.cardinalcommerce.cardinalmobilesdk.services.CardinalInitService

abstract class Dojo3DSBaseViewModel(
    configuredCardinalInstance: Cardinal
) : ViewModel(), CardinalInitService {
    val configureDCardinalInstance = configuredCardinalInstance
}
