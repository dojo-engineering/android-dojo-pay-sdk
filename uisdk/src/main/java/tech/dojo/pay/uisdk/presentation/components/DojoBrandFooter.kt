package tech.dojo.pay.uisdk.presentation.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.DojoSDKDropInUI
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.entities.color
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.medium

@Composable
internal fun DojoBrandFooter(modifier: Modifier = Modifier, mode: DojoBrandFooterModes = DojoBrandFooterModes.DOJO_BRAND_ONLY) {
    when (mode) {
        DojoBrandFooterModes.DOJO_BRAND_ONLY -> DojoBrandFooterOnly(modifier)
        DojoBrandFooterModes.TERMS_AND_PRIVACY_ONLY -> TermsAndPrivacyFooterOnly(modifier)
        DojoBrandFooterModes.DOJO_BRAND_WITH_TERMS_AND_PRIVACY -> DojoBrandAndTermsAndPrivacyFooter(
            modifier
        )
        DojoBrandFooterModes.NONE -> EmptyFooter(modifier)
    }
}

@Composable
private fun DojoBrandFooterOnly(modifier: Modifier = Modifier) {
    val forceLightMode = DojoSDKDropInUI.dojoThemeSettings?.forceLightMode ?: false

    val currentThemColorText = if (isSystemInDarkTheme() && !forceLightMode) {
        DARK_COLOR_HEXA.color
    } else {
        LIGHT_COLOR_TEXT_HEXA.color
    }

    val currentThemColorIcon = if (isSystemInDarkTheme() && !forceLightMode) {
        DARK_COLOR_HEXA.color
    } else {
        LIGHT_COLOR_LOGO_HEXA.color
    }
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center

        ) {
            DojoBrandText(currentThemColorText, currentThemColorIcon)
        }
    }
}

@Composable
private fun TermsAndPrivacyFooterOnly(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val forceLightMode = DojoSDKDropInUI.dojoThemeSettings?.forceLightMode ?: false

    val currentThemColor = if (isSystemInDarkTheme() && !forceLightMode) {
        DARK_COLOR_HEXA.color
    } else {
        LIGHT_COLOR_LOGO_HEXA.color
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center

        ) { TermsAndAndPrivacy(context, currentThemColor) }
    }
}

@Composable
private fun DojoBrandAndTermsAndPrivacyFooter(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val forceLightMode = DojoSDKDropInUI.dojoThemeSettings?.forceLightMode ?: false

    val currentThemColorText = if (isSystemInDarkTheme() && !forceLightMode) {
        DARK_COLOR_HEXA.color
    } else {
        LIGHT_COLOR_TEXT_HEXA.color
    }

    val currentThemColorIcon = if (isSystemInDarkTheme() && !forceLightMode) {
        DARK_COLOR_HEXA.color
    } else {
        LIGHT_COLOR_LOGO_HEXA.color
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center

        ) {
            DojoBrandText(currentThemColorText, currentThemColorIcon)
            Divider(currentThemColorIcon)
            TermsAndAndPrivacy(context, currentThemColorIcon)
        }
    }
}

@Composable
private fun EmptyFooter(modifier: Modifier = Modifier) { Spacer(modifier = modifier) }

@Composable
private fun DojoBrandText(currentThemColorText: Color, currentThemColorIcon: Color) {
    Text(
        text = stringResource(id = R.string.dojo_ui_sdk_footer_powered_by_title),
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = DojoTheme.typography.body1.medium,
        color = currentThemColorText.copy(alpha = ContentAlpha.high)
    )
    Icon(
        modifier = Modifier.size(width = 60.dp, height = 15.dp).then(Modifier.padding(start = 4.dp, top = 3.dp, bottom = 0.dp, end = 10.dp)),
        painter = painterResource(id = R.drawable.ic_dojo),
        tint = currentThemColorIcon.copy(alpha = ContentAlpha.high),
        contentDescription = null,
    )
}

@Composable
private fun Divider(currentThemColorText: Color) {
    Divider(
        color = currentThemColorText.copy(alpha = ContentAlpha.high),
        modifier = Modifier
            .height(20.dp)
            .width(1.dp)
    )
}

@Composable
private fun TermsAndAndPrivacy(context: Context, currentThemColor: Color) {
    Text(
        text = stringResource(id = R.string.dojo_ui_sdk_footer_powered_by_terms),
        modifier = Modifier
            .padding(
                start = 12.dp, top = 4.dp, bottom = 4.dp, end = 12.dp
            )
            .clickable {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse(TERMS_URL)
                    )
                )
            },
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = DojoTheme.typography.subtitle1,
        color = currentThemColor.copy(alpha = ContentAlpha.medium)
    )
    Text(
        text = stringResource(id = R.string.dojo_ui_sdk_footer_powered_by_privacy),
        modifier = Modifier
            .padding(4.dp)
            .clickable {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse(PRIVACY_URL)
                    )
                )
            },
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = DojoTheme.typography.subtitle1,
        color = currentThemColor.copy(alpha = ContentAlpha.medium)
    )
}

@Preview("DojoBrandOnlyFooter", group = "Footer")
@Composable
internal fun PreviewDojoBrandOnlyFooter() = DojoPreview {
    DojoBrandFooter(mode = DojoBrandFooterModes.DOJO_BRAND_ONLY)
}

@Preview("DojoBrandFooter With Terms and privacy", group = "Footer")
@Composable
internal fun PreviewDojoBrandFooterWithTermsAndPrivacy() = DojoPreview {
    DojoBrandFooter(mode = DojoBrandFooterModes.DOJO_BRAND_WITH_TERMS_AND_PRIVACY)
}

@Preview(" Terms and privacy only ", group = "Footer")
@Composable
internal fun PreviewTermsAndPrivacyOnlyFooter() = DojoPreview {
    DojoBrandFooter(mode = DojoBrandFooterModes.TERMS_AND_PRIVACY_ONLY)
}

@Preview(" Empty ", group = "Footer")
@Composable
internal fun PreviewEmptyFooter() = DojoPreview {
    DojoBrandFooter(mode = DojoBrandFooterModes.NONE)
}

private const val TERMS_URL = "https://pay.dojo.tech/terms"
private const val PRIVACY_URL = "https://dojo.tech/legal/privacy/"
internal const val LIGHT_COLOR_TEXT_HEXA = "#FF003F33"
internal const val LIGHT_COLOR_LOGO_HEXA = "#FF000000"
internal const val DARK_COLOR_HEXA = "#FFFFFFFF"
