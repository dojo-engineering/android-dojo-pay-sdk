package tech.dojo.pay.uisdk.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.dojo.pay.uisdk.R
import tech.dojo.pay.uisdk.presentation.components.theme.DojoTheme
import tech.dojo.pay.uisdk.presentation.ui.mangepaymentmethods.state.PaymentMethodItemViewEntityItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CardItemWithRadioButton(
    modifier: Modifier = Modifier,
    cardItem: PaymentMethodItemViewEntityItem.CardItemItem,
    isSelected: Boolean,
    inEditeMode: Boolean,
    onClick: ((PaymentMethodItemViewEntityItem.CardItemItem) -> Unit),
    onLongClick: ((PaymentMethodItemViewEntityItem.CardItemItem) -> Unit)
) {
    val haptic = LocalHapticFeedback.current

    Row(
        modifier = modifier
            .combinedClickable(
                onClick = {
                    if (inEditeMode) onLongClick.invoke(cardItem) else onClick.invoke(cardItem)
                },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onLongClick.invoke(cardItem)
                }
            )
            .background(color = if (inEditeMode && isSelected) DojoTheme.colors.primaryLabelTextColor.copy(alpha = .3f) else DojoTheme.colors.primarySurfaceBackgroundColor)
            .padding(start = 8.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DojoSpacer(width = 16.dp)
        Icon(
            painter = painterResource(id = cardItem.icon),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(top = 2.dp)
                .width(30.dp)
                .heightIn(20.dp)
        )

        DojoSpacer(width = 16.dp)
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f)
        ) {
            Text(
                text = cardItem.scheme,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = DojoTheme.typography.body2,
                color = DojoTheme.colors.secondaryLabelTextColor
            )
            DojoSpacer(height = 8.dp)
            Text(
                text = cardItem.pan,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = DojoTheme.typography.subtitle1,
                color = DojoTheme.colors.primaryLabelTextColor
            )
        }

        if (inEditeMode) {
            Box(
                modifier = Modifier
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        painter = painterResource(id = R.drawable.check_circle_24px),
                        contentDescription = "",
                        tint = DojoTheme.colors.inputElementActiveTintColor,
                        modifier = Modifier.background(
                            shape = CircleShape,
                            color = Color.Transparent
                        )
                    )
                }
            }
        } else {
            RadioButton(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterVertically),
                selected = isSelected,
                onClick = { onClick.invoke(cardItem) },
                colors = RadioButtonDefaults.colors(
                    selectedColor = DojoTheme.colors.inputElementActiveTintColor,
                    unselectedColor = DojoTheme.colors.inputElementDefaultTintColor,
                    disabledColor = Color.LightGray
                )
            )
        }
    }
}

@SuppressLint("CardItemWithRadioButton")
@Preview("CardItemWithRadioButton", group = "CardItemWithRadioButton")
@Composable
internal fun PreviewCardItemWithRadioButton() {
    DojoPreview {
        CardItemWithRadioButton(
            cardItem = PaymentMethodItemViewEntityItem.CardItemItem(
                id = "",
                icon = R.drawable.ic_visa,
                scheme = "Visa",
                pan = "****9560"
            ),
            onClick = {},
            onLongClick = {},
            isSelected = true,
            inEditeMode = false,
        )
    }
}
