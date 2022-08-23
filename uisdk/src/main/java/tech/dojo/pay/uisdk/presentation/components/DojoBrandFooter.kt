package tech.dojo.pay.uisdk.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    withTermsAndPrivacy: Boolean = false,
    onTermsClicked: () -> Unit = {},
    onPrivacyClicked: () -> Unit = {}
) {
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
                text = stringResource(id = R.string.dojo_brand_footer_text_powered_by),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = DojoTheme.typography.body2,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.high)
            )
            Text(
                text = stringResource(id = R.string.dojo_brand_footer_text_dojo),
                modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 4.dp, end = 12.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = DojoTheme.typography.body1.bold,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.high)
            )
            if (withTermsAndPrivacy) {
                Divider(
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.high),
                    modifier = Modifier
                        .height(20.dp)
                        .width(1.dp)
                )
                Text(
                    text = stringResource(id = R.string.dojo_brand_footer_text_terms),
                    modifier = Modifier
                        .padding(
                            start = 12.dp,
                            top = 4.dp,
                            bottom = 4.dp,
                            end = 12.dp
                        )
                        .clickable { onTermsClicked.invoke() },
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = DojoTheme.typography.subtitle1,
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.high)
                )
                Text(
                    text = stringResource(id = R.string.dojo_brand_footer_text_privacy),
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable { onPrivacyClicked.invoke() },
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = DojoTheme.typography.subtitle1,
                    color = LocalContentColor.current.copy(alpha = ContentAlpha.high)
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