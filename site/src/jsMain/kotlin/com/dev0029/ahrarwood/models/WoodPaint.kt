package com.dev0029.ahrarwood.models

import com.varabyte.kobweb.compose.ui.graphics.Colors
import org.jetbrains.compose.web.css.CSSColorValue

data class WoodPaint(
    val name : String,
    val color: CSSColorValue,
    val secondColor: CSSColorValue = Colors.White
)
