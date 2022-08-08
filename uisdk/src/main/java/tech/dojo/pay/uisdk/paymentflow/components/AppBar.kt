package tech.dojo.pay.uisdk.paymentflow.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.paymentflow.components.theme.DojoTheme

@Composable
internal fun DojoAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    titleGravity: TitleGravity = TitleGravity.CENTER,
    navigationIcon: AppBarIcon? = null,
    actionIcon: AppBarIcon? = null,
    onTitleClick: (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (titleGravity == TitleGravity.LEFT && navigationIcon == null) {
                DojoSpacer(width = 16.dp)
            } else {
                AppBarIconButton(navigationIcon)
            }

            Box(
                modifier = Modifier.weight(1f)
            ) {
                Title(title, titleGravity, onTitleClick)
            }

            AppBarIconButton(actionIcon)
        }
        HorizontalDivider()
    }
}

@Composable
private fun BoxScope.Title(
    text: String,
    gravity: TitleGravity,
    onClick: (() -> Unit)?
) {
    val alignment: Alignment = when (gravity) {
        TitleGravity.LEFT -> Alignment.CenterStart
        TitleGravity.CENTER -> Alignment.Center
    }

    Row(
        modifier = Modifier
            .defaultMinSize(minHeight = 48.dp)
            .align(alignment)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = DojoTheme.typography.h5,
            color = LocalContentColor.current.copy(alpha = ContentAlpha.high)
        )
    }
}

@Composable
private fun AppBarIconButton(
    icon: AppBarIcon?
) {
    IconButton(onClick = { icon?.onClick?.invoke() }) {
        if (icon != null) {
            Icon(
                painter = painterResource(id = icon.resId),
                contentDescription = null,
                tint = if (icon.isSecondary) {
                    DojoTheme.colors.onBackground.copy(alpha = ContentAlpha.medium)
                } else {
                    DojoTheme.colors.primary
                }
            )
        }
    }
}

/**
 * @resId icon resource id
 * @isSecondary indicates which style should be applied to icon.
 * If false, then icon color will be primary. Otherwise, onbackground with medium alpha will be used.
 * @onClick will be called when user clicks on the element
 */
@Stable
internal data class AppBarIcon(
    @DrawableRes val resId: Int,
    val isSecondary: Boolean = false,
    val onClick: () -> Unit
) {

    companion object {
        fun close(
            isSecondary: Boolean = false,
            onClick: () -> Unit
        ) = AppBarIcon(R.drawable.ic_close_green_24px, isSecondary, onClick)
    }
}

internal enum class TitleGravity {
    LEFT,
    CENTER
}

@Preview("App bar with no title", group = "AppBar")
@Composable
internal fun PreviewDojoAppBarNoTitle() = DojoPreview {
    DojoAppBar(
        actionIcon = AppBarIcon.close {}
    )
}

@Preview("App bar with two icons and title aligned to left", group = "AppBar")
@Composable
internal fun PreviewDojoAppBarLeftGravity() = DojoPreview {
    DojoAppBar(
        title = "Title",
        titleGravity = TitleGravity.LEFT,
        navigationIcon = AppBarIcon.close { },
        actionIcon = AppBarIcon.close(isSecondary = true) {}
    )
}

@Preview("App bar with title aligned to left", group = "AppBar")
@Composable
internal fun PreviewDojoAppBarOneIconLeftGravity() = DojoPreview {
    DojoAppBar(
        title = "Title",
        titleGravity = TitleGravity.LEFT,
        actionIcon = AppBarIcon.close(isSecondary = true) {}
    )
}
