package com.dev0029.ahrarwood.components.sections.home

import androidx.compose.runtime.Composable
import com.dev0029.ahrarwood.constants.ImagePaths
import com.dev0029.ahrarwood.utils.Utils
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.P

@Composable
fun HomeShipping(modifier: Modifier) {
    val imageSrc = if (Utils.isBrowserLanguageEn()) ImagePaths.SHIPPING1 else ImagePaths.SHIPPING2
    Column(modifier = modifier.fillMaxWidth()
        .height(460.px)
        .padding(leftRight = 96.px)) {
        SpanText(Res.string.shipping_title, modifier = modifier.fontSize(36.px).fontWeight(FontWeight.Bold))
        Row(modifier = modifier.fillMaxSize().margin(top = 24.px)) {
            Image(src = imageSrc, modifier = modifier.size(320.px).width(640.px))
            Column(modifier = modifier) {
                H4({
                    style {
                        property("margin-top", "8px")
                        property("margin-bottom", "8px")
                        property("font-size", "24px")
                    }
                }) {
                    SpanText(Res.string.shipping_desc1)
                }
                P({}) {
                    SpanText(Res.string.shipping_description)
                }
                P({
                    style {
                        property("margin-top", "4px")
                    }
                }) {
                    SpanText(Res.string.shipping_description2)
                }
            }
        }
    }
}