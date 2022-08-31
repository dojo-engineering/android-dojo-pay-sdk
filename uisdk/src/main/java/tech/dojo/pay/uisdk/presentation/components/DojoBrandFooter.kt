package tech.dojo.pay.uisdk.presentation.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.bold

@Composable
internal fun DojoBrandFooter(
    modifier: Modifier = Modifier,
    withTermsAndPrivacy: Boolean = false
) {
    val context = LocalContext.current

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center

        ) {
            Text(
                text = stringResource(id = R.string.dojo_ui_sdk_footer_powered_by_title),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = DojoTheme.typography.body1,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.high)
            )
            Icon(
                modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 2.dp, end = 12.dp),
                painter = painterResource(id = R.drawable.ic_dojo),
                contentDescription = null,
            )
            if (withTermsAndPrivacy) {
                Divider(
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
                    modifier = Modifier
                        .height(20.dp)
                        .width(1.dp)
                )
                Text(
                    text = stringResource(id = R.string.dojo_ui_sdk_footer_powered_by_terms),
                    modifier = Modifier
                        .padding(
                            start = 12.dp,
                            top = 4.dp,
                            bottom = 4.dp,
                            end = 12.dp
                        )
                        .clickable {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(TERMS_URL)
                                )
                            )
                        },
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = DojoTheme.typography.subtitle1,
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                )
                Text(
                    text = stringResource(id = R.string.dojo_ui_sdk_footer_powered_by_privacy),
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(PRIVACY_URL)
                                )
                            )
                        },
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = DojoTheme.typography.subtitle1,
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                )
            }
        }
    }
}

@Preview("DojoBrandFooter", group = "Footer")
@Composable
internal fun PreviewDojoBrandFooter() = DojoPreview {
    DojoBrandFooter()
}

@Preview("DojoBrandFooter With Terms and privacy", group = "Footer")
@Composable
internal fun PreviewDojoBrandFooterWithTermsAndPrivacy() = DojoPreview {
    DojoBrandFooter(withTermsAndPrivacy = true)
}

private const val TERMS_URL = "https://pay.dojo.tech/terms"
private const val PRIVACY_URL = "https://dojo.tech/legal/privacy/"
