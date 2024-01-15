package tech.dojo.pay.uisdk.presentation.components

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView

interface KeyboardController {
    fun show()
    fun hide()
}

@Composable
fun rememberKeyboardController(): KeyboardController {
    val view = LocalView.current
    val imm =
        LocalContext.current.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    return remember {
        object : KeyboardController {
            override fun show() { imm?.showSoftInput(view, 0) }

            override fun hide() { imm?.hideSoftInputFromWindow(view.windowToken, 0) }
        }
    }
}
