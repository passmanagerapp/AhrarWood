package com.dev0029.ahrarwood.components.widgets

import androidx.compose.runtime.Composable
import com.dev0029.ahrarwood.extensions.isMobileCompatible
import com.dev0029.ahrarwood.models.WoodPaint
import com.varabyte.kobweb.browser.dom.ElementTarget
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.overlay.PopupPlacement
import com.varabyte.kobweb.silk.components.overlay.Tooltip
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLElement

@Composable
fun BoxColor(
    modifier: Modifier,
    paint: WoodPaint,
    breakpoint: Breakpoint,
    index: Int,
    selectedIndex: Int,
    onClick: (index:Int) -> Unit
) {
    val border = if (index == selectedIndex) 2.px else 1.px
    val isDarkMode = ColorMode.current.isDark
    val borderColor = if (isDarkMode) Colors.White else Colors.Black
    Box(
        modifier = modifier
            .cursor(Cursor.Pointer)
            .backgroundColor(paint.color)
            .borderRadius(5.px)
            .margin(left = if (breakpoint.isMobileCompatible()) 4.px else 8.px)
            .styleModifier {
                property("width", if (breakpoint.isMobileCompatible()) "32px" else "40px")
                property("height", if (breakpoint.isMobileCompatible()) "32px" else "40px")
            }
            .border(border, LineStyle.Solid, if (index == selectedIndex) borderColor else Colors.Transparent)
            .onClick {
                //onClick(index)
            }
            .onMouseOver {
                (it.target as? HTMLElement)?.style?.apply {
                    transform = "scale(1.05)"
                    boxShadow = "0 6px 12px rgba(0, 0, 0, 0.15)"
                }
            }
            .onMouseLeave {
                (it.target as? HTMLElement)?.style?.apply {
                    transform = "scale(1)"
                    boxShadow = "0 4px 8px rgba(0, 0, 0, 0.1)"
                }
            }
    )
    val textColor = if (paint.name == Res.string.white) Color.black else Color.white
    Tooltip(
        target = ElementTarget.PreviousSibling,
        text = paint.name,
        placement = PopupPlacement.BottomRight,
        modifier = modifier.backgroundColor(paint.color)
            .color(textColor)
    )

}