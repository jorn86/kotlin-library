package org.hertsig.compose.component.richtext

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider

object TooltipPositionProvider : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val x = if (anchorBounds.center.x > windowSize.width / 2) anchorBounds.left - popupContentSize.width
                else anchorBounds.right
        return IntOffset(x, (windowSize.height - popupContentSize.height) / 2)
    }
}
