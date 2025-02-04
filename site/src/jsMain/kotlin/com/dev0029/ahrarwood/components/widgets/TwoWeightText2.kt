package com.dev0029.ahrarwood.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.px

@Composable
fun TwoWeightText2(
    modifier: Modifier,
    textBold: String,
    textNormal: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        SpanText(
            text = textBold,
            modifier = modifier
                .color(Colors.Black)
                .fontWeight(FontWeight.Bold)
                .margin(left = 1.px, right = 6.px)
        )
        SpanText(
            text = textNormal,
            modifier = modifier
                .color(Colors.Black)
                .fontWeight(FontWeight.Normal)
        )
    }
}