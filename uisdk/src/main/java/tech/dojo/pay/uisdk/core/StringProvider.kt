package tech.dojo.pay.uisdk.core

import android.content.Context
import androidx.annotation.StringRes

class StringProvider(private val context: Context) {

    fun getString(
        @StringRes stringId: Int,
    ): String {
        return context.getString(stringId)
    }
}
