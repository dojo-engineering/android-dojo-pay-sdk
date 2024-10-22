package tech.dojo.pay.sdksample

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.DojoSDKDropInUI

class SampleActivity : ComponentActivity() {

    private val DarkColorPalette = darkColors()

    private val LightColorPalette = lightColors()

    @Composable
    fun Theme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
        val colors = if (darkTheme) {
            DarkColorPalette
        } else {
            LightColorPalette
        }

        MaterialTheme(colors = colors, content = content)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Theme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    var language by remember { mutableStateOf("en") }
                    SdkButton("core sdk demo") {
                        startActivity(Intent(this@SampleActivity, CardPaymentActivity::class.java))
                    }
                    SdkButton("ui sdk demo") {
                        startActivity(Intent(this@SampleActivity, UiSdkSampleActivity::class.java))
                    }
                    SdkButton("sample app for ui kotlin") {
                        startActivity(Intent(this@SampleActivity, ExampleKotlin::class.java))
                    }
                    SdkButton("sample app for ui java") {
                        startActivity(Intent(this@SampleActivity, ExampleJava::class.java))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextField(
                            modifier = Modifier.padding(horizontal = 16.dp).width(100.dp),
                            value = language,
                            onValueChange = { s -> language = s }
                        )
                        SdkButton("Change Language") {
                            DojoSDKDropInUI.setSdkLocale(language)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SdkButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text)
    }
}
