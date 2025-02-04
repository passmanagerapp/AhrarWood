package com.dev0029.ahrarwood.components.layouts


import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.*
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.silk.components.graphics.Image
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLImageElement

@Composable
fun Dialog(
    isOpen: Boolean,
    onClose: () -> Unit,
    title: String,
    coverUrl: String,
    id: String,
    onRemove:(id:String) -> Unit
) {
    if (!isOpen) return
    val isHovered = remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var rotation by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while(true) {
            delay(8)
            rotation = (rotation + 12) % 360
        }
    }
    LaunchedEffect(coverUrl) {
        val img = (document.createElement("img") as HTMLImageElement).apply {
            src = coverUrl
            onload = { isLoading = false }
            onerror = {a,b,c,d,e -> isLoading = false}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .position(Position.Fixed)
            .top(0.px)
            .left(0.px)
            .backgroundColor(rgba(0, 0, 0, 0.5))
            .zIndex(999)
            .onClick { onClose() },
        contentAlignment = Alignment.Center
    ) {
        // Dialog kutusu
        Box(
            modifier = Modifier
                .padding(topBottom = 20.px, leftRight = 80.px)
                .backgroundColor(Colors.White)
                .borderRadius(8.px)
                .maxWidth(400.px)
                .onClick { it.stopPropagation() } // Overlay tıklamasını engelle
        ) {
            Column(
                modifier = Modifier.padding(16.px).gap(16.px),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SpanText(
                    text = title,
                    modifier = Modifier
                        .fontSize(20.px)
                        .fontWeight(FontWeight.Bold)
                        .textAlign(TextAlign.Center)
                )

                Box(
                    modifier = Modifier.size(200.px),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .size(40.px)
                                .borderRadius(50.percent)
                                .border(2.px, style = LineStyle.Solid, color = rgb(59, 130, 246))
                                .borderLeft(2.px, style = LineStyle.Solid, color = Colors.Transparent)
                                .transform { rotate(rotation.deg) }
                        )
                    } else {
                        Image(
                            src = coverUrl,
                            modifier = Modifier
                                .size(200.px)
                                .objectFit(ObjectFit.Contain)
                                .margin(topBottom = 16.px)
                                .opacity(if (isLoading) 0 else 1)
                        )
                    }
                }


                // Butonlar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { onRemove(id) },
                        modifier = Modifier
                            .padding(8.px)
                            .backgroundColor(if (isHovered.value) rgb(29, 78, 216) else rgb(59, 130, 246))
                            .color(Colors.White)
                            .borderRadius(4.px)
                            .padding(leftRight = 16.px, topBottom = 8.px)
                            .onMouseEnter {
                                isHovered.value = true
                            }
                            .onMouseLeave {
                                isHovered.value = false
                            }
                            .margin(right = 8.px)
                    ) {
                        SpanText(Res.string.remove)
                    }
                    Button(
                        onClick = { onClose() },
                        modifier = Modifier
                            .padding(8.px)
                            .backgroundColor(if (isHovered.value) rgb(29, 78, 216) else rgb(59, 130, 246))
                            .color(Colors.White)
                            .borderRadius(4.px)
                            .padding(leftRight = 16.px, topBottom = 8.px)
                            .onMouseEnter {
                                isHovered.value = true
                            }
                            .onMouseLeave {
                                isHovered.value = false
                            }
                            .margin(left = 8.px)
                    ) {
                        SpanText(Res.string.ok)
                    }
                }
            }
        }
    }
}