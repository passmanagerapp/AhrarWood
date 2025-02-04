package com.dev0029.ahrarwood.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.dev0029.ahrarwood.extensions.brownColor
import com.dev0029.ahrarwood.extensions.secondaryColor
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.px


@Composable
fun HomeSectionButton(
    modifier: Modifier,
    title: String,
    path: String,
    currentPath: String,
    onClick : (path: String) -> Unit
) {
    val isHovered =remember { mutableStateOf(false) }
    val isDark = ColorMode.current.isDark
    val hoveredColor = if (isDark) secondaryColor else brownColor
    val defaultColor = if (isDark) Colors.White else Colors.Black
    SpanText(title,modifier
        .cursor(Cursor.Pointer)
        .color(if (isHovered.value) hoveredColor else if (currentPath == path) hoveredColor else defaultColor)
        .onClick {
            if (isHovered.value)
                onClick(path)
        }
        .margin(right = 8.px)
        .onMouseEnter { isHovered.value = true }
        .onMouseLeave { isHovered.value = false }
        .apply {

        }
    )
}