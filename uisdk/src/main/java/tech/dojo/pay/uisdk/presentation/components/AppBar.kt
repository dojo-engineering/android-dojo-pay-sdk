package tech.dojo.pay.uisdk.presentation.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.components.theme.medium

@Composable
internal fun DojoAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    titleColor: Color? = null,
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
                Title(title, titleColor, titleGravity, onTitleClick)
            }

            AppBarIconButton(actionIcon)
        }
        HorizontalDivider()
    }
}

@Composable
private fun BoxScope.Title(
    text: String,
    titleColor: Color? = null,
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
            style = DojoTheme.typography.h5.medium,
            color = titleColor ?: LocalContentColor.current.copy(alpha = ContentAlpha.high)
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
                tint = icon.tintColor ?: DojoTheme.colors.onBackground.copy(alpha = ContentAlpha.medium)
            )
        }
    }
}

/**
 * @resId icon resource id
 * @tintColor add the tint color for the icon
 * @onClick will be called when user clicks on the element
 */
@Stable
internal data class AppBarIcon(
    @DrawableRes val resId: Int,
    val tintColor: Color? = null,
    val onClick: () -> Unit
) {

    companion object {
        fun close(
            tintColor: Color? = null,
            onClick: () -> Unit
        ) = AppBarIcon(
            R.drawable.ic_close_green_24px,
            tintColor,
            onClick
        )

        fun back(
            tintColor: Color? = null,
            onClick: () -> Unit
        ) = AppBarIcon(
            R.drawable.ic_arrow_back_24_px,
            tintColor,
            onClick
        )
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
        actionIcon = AppBarIcon.close() {}
    )
}

@Preview("App bar with title aligned to left", group = "AppBar")
@Composable
internal fun PreviewDojoAppBarOneIconLeftGravity() = DojoPreview {
    DojoAppBar(
        title = "Title",
        titleGravity = TitleGravity.LEFT,
        actionIcon = AppBarIcon.close() {}
    )
}
