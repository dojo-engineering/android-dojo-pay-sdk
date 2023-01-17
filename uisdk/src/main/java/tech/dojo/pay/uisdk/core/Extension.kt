package tech.dojo.pay.uisdk.core

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

inline fun <reified Activity : ComponentActivity> Context.getActivity(): Activity? {
    return when (this) {
        is Activity -> this
        else -> {
            var context = this
            while (context is ContextWrapper) {
                context = context.baseContext
                if (context is Activity) return context
            }
            null
        }
    }
}
