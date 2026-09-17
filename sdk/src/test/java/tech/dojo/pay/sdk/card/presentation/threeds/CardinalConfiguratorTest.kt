package tech.dojo.pay.sdk.card.presentation.threeds

import com.cardinalcommerce.cardinalmobilesdk.enums.CCADatabase
import org.junit.Assert.assertEquals
import org.junit.Test

internal class CardinalConfiguratorTest {

    @Test
    fun `sandbox uses the Visa data center before production cutover`() {
        assertEquals(
            CCADatabase.CGKURLS,
            CardinalConfigurator.dataCenterFor(
                isSandbox = true,
                currentTimeMillis = CardinalConfigurator.VISA_PRODUCTION_CUTOVER_MILLIS - 1,
            ),
        )
    }

    @Test
    fun `production uses the Cardinal data center before cutover`() {
        assertEquals(
            CCADatabase.CCAURLS,
            CardinalConfigurator.dataCenterFor(
                isSandbox = false,
                currentTimeMillis = CardinalConfigurator.VISA_PRODUCTION_CUTOVER_MILLIS - 1,
            ),
        )
    }

    @Test
    fun `production uses the Visa data center at cutover`() {
        assertEquals(
            CCADatabase.CGKURLS,
            CardinalConfigurator.dataCenterFor(
                isSandbox = false,
                currentTimeMillis = CardinalConfigurator.VISA_PRODUCTION_CUTOVER_MILLIS,
            ),
        )
    }
}
