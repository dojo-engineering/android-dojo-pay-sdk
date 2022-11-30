package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme

private val DojoButtonHeight = 44.dp

@Composable
private fun DojoButton(
    text: String,
    backgroundColor: Color,
    contentColor: Color,
    borderStroke: BorderStroke?,
    modifier: Modifier,
    enabled: Boolean,
    isLoading: Boolean,
    loadingColor: Color,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.height(DojoButtonHeight),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            disabledBackgroundColor = DojoTheme.colors.onBackground.copy(alpha = 0.1f)
        ),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
        shape = RoundedCornerShape(50),
        border = borderStroke,
        enabled = enabled
    ) {
        if (isLoading) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = stringResource(id = R.string.dojo_ui_sdk_card_details_checkout_button_processing),
                style = DojoTheme.typography.subtitle1,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            CircularProgressIndicator(
                color = loadingColor,
                strokeWidth = 2.dp,
                modifier = Modifier.size(15.dp)
            )
        } else {
            Text(
                text = text,
                style = DojoTheme.typography.subtitle1,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
internal fun GooglePayButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = DojoTheme.colors.secondarySurface,
    contentColor: Color = DojoTheme.colors.onPrimary,
    borderStroke: BorderStroke? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .height(DojoButtonHeight)
            .fillMaxWidth(),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            disabledBackgroundColor = DojoTheme.colors.onBackground.copy(alpha = 0.1f)
        ),
        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
        shape = RoundedCornerShape(50),
        border = borderStroke,
        enabled = enabled
    ) {
        Icon(
            painter = painterResource(id = if(isSystemInDarkTheme()){
                R.drawable.ic_google_pay_darck
            }else{
                R.drawable.ic_google_pay
            }
           ),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        )
    }
}

@Composable
internal fun DojoFullGroundButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color? = null,
    contentColor: Color? = null,
    isLoading: Boolean = false,
    loadingColor: Color? = null,
    onClick: () -> Unit
) {
    DojoButton(
        text = text,
        backgroundColor = backgroundColor ?: DojoTheme.colors.primaryCTAButtonActiveBackgroundColor,
        contentColor = contentColor ?: DojoTheme.colors.onPrimary,
        borderStroke = null,
        modifier = modifier,
        enabled = enabled,
        isLoading = isLoading,
        loadingColor = loadingColor ?: DojoTheme.colors.onPrimary,
        onClick = onClick
    )
}

@Composable
internal fun DojoOutlinedButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color? = null,
    contentColor: Color? = null,
    borderStrokeColor: Color? = null,
    isLoading: Boolean = false,
    loadingColor: Color? = null,
    onClick: () -> Unit
) {
    DojoButton(
        text = text,
        backgroundColor = backgroundColor ?: DojoTheme.colors.primarySurfaceBackgroundColor,
        contentColor = contentColor ?: DojoTheme.colors.primaryLabelTextColor,
        borderStroke = getBorderStroke(enabled, borderStrokeColor),
        modifier = modifier,
        enabled = enabled,
        isLoading = isLoading,
        loadingColor = loadingColor ?: DojoTheme.colors.primaryLabelTextColor,
        onClick = onClick
    )
}

@Composable
internal fun SingleButtonView(
    text: String,
    modifier: Modifier = Modifier,
    contentColor: Color? = null,
    backgroundColor: Color? = null,
    borderStroke: BorderStroke? = null,
    loadingColor: Color? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    scrollState: ScrollState? = null,
    onClick: () -> Unit
) {
    if (scrollState != null) {
        ScrollHintLayout(
            modifier = modifier,
            scrollState = scrollState
        ) {
            SingleButton(
                Modifier,
                text,
                enabled,
                isLoading,
                contentColor,
                backgroundColor,
                borderStroke,
                loadingColor,
                onClick
            )
        }
    } else {
        SingleButton(
            Modifier,
            text,
            enabled,
            isLoading,
            contentColor,
            backgroundColor,
            borderStroke,
            loadingColor,
            onClick
        )
    }
}

@Composable
private fun SingleButton(
    modifier: Modifier,
    text: String,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    contentColor: Color? = null,
    backgroundColor: Color? = null,
    borderStrokeColor: BorderStroke? = null,
    loadingColor: Color? = null,
    onClick: () -> Unit
) {
    Box(modifier = modifier.background(DojoTheme.colors.background)) {
        DojoButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            text = text,
            enabled = enabled,
            onClick = onClick,
            backgroundColor = backgroundColor ?: DojoTheme.colors.primaryCTAButtonActiveBackgroundColor,
            contentColor = contentColor ?: DojoTheme.colors.onPrimary,
            borderStroke = borderStrokeColor,
            isLoading = isLoading,
            loadingColor = loadingColor ?: DojoTheme.colors.onPrimary,
        )
    }
}

@Composable
private fun getBorderStroke(
    enabled: Boolean,
    borderStrokeColor: Color?
) =
    if (enabled) BorderStroke(
        1.dp,
        borderStrokeColor ?: DojoTheme.colors.primaryCTAButtonActiveBackgroundColor
    ) else null

@Preview("Button", group = "Buttons")
@Composable
fun PreviewButton() {
    DojoPreview() {
        DojoFullGroundButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            text = "Button"
        ) {}
    }
}

@Preview("Outlined button", group = "Buttons")
@Composable
fun PreviewOtlinedButton() {
    DojoPreview() {
        DojoOutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            text = "Button"
        ) {}
    }
}

@Preview("GooglePayButton", group = "GooglePayButton")
@Composable
fun PreviewGooglePayButton() {
    DojoPreview() {
        GooglePayButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            onClick = {}
        )
    }
}
