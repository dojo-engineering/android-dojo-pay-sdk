package tech.dojo.pay.sdk.card

import android.util.Base64
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentParams
import tech.dojo.pay.sdk.card.entities.DojoCardPaymentResult

internal class DojoCardPaymentViewModel(
    private val params: DojoCardPaymentParams
) : ViewModel() {

    val result = MutableLiveData<DojoCardPaymentResult>()
    val threeDsNavigationEvent = MutableLiveData<Unit>()
    var canExit: Boolean = false //User should not be able to leave while request is not completed

    init {
        viewModelScope.launch {
            delay(3000) //Make requests
            canExit = true
            threeDsNavigationEvent.value = Unit
        }
    }

    fun on3DSCompleted() {
        result.value = DojoCardPaymentResult.SUCCESSFUL
    }

    private fun fetch3dsPage(stepUpUrl: String, jwtToken: String, md: String): String {
        val securePageHtml = """
        <!DOCTYPE html>
        <html>
        <head>
          <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, viewport-fit=cover">
        </head>
        <body>
        <form id="threeDs20Form" target="_self" method="post" action="$stepUpUrl">
            <input name="JWT" type="hidden" value="$jwtToken"/>
            <input name="MD" type="hidden" value="$md"/>
        </form>
        </div>
        </body>
        </html>
        """

        return Base64.encodeToString(securePageHtml.toByteArray(), Base64.NO_PADDING)
    }


}