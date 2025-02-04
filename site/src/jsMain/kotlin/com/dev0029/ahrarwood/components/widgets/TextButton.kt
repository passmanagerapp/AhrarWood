package com.dev0029.ahrarwood.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.text.SpanText

@Composable
fun UnderlinedTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .cursor(Cursor.Pointer)
            .onClick { onClick() }
    ) {
        SpanText(
            text = text,
            modifier = Modifier
                .styleModifier {
                    property("text-decoration", "underline")
                }
        )
    }
}