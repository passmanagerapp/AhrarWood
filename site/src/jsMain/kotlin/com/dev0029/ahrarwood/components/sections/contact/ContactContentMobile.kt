package com.dev0029.ahrarwood.components.sections.contact

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.dev0029.ahrarwood.components.layouts.GoogleMap
import com.dev0029.ahrarwood.components.sections.home.HomeFooter
import com.dev0029.ahrarwood.components.sections.home.HomeHeader
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.forms.Input
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import kotlinx.browser.window
import com.dev0029.ahrarwood.constants.Constants
import com.dev0029.ahrarwood.constants.PageRoutes
import com.dev0029.ahrarwood.extensions.brownColor
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.marginBottom
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgb
import org.jetbrains.compose.web.css.rgba
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea

@Composable
fun ContactContentMobile(
    breakpoint: Breakpoint,
    modifier: Modifier
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val name = remember { mutableStateOf("") }
        var title = remember { mutableStateOf("") }
        val message = remember { mutableStateOf("") }

        HomeHeader(PageRoutes.CONTACT,modifier)
        Column(
            modifier =
            modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GoogleMap(breakpoint,modifier)
            Column(
                modifier = modifier
                    .fillMaxWidth(80.percent)
                    .margin( leftRight = 14.px)
                    .padding(all = 12.px)
                    .borderRadius(6.px)
                    .boxShadow(
                        offsetX = 0.px,
                        offsetY = 2.px,
                        blurRadius = 8.px,
                        color = rgba(0, 0, 0, 0.1)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SpanText(
                    Res.string.contact_title,
                    modifier = Modifier
                        .margin(bottom = 16.px)
                        .fontSize(16.px)
                        .color(rgb(100, 100, 100))
                )

                // Name Input
                Input(
                    type = InputType.Text,
                    value = name.value,
                    onValueChange = { name.value = it },
                    placeholder = Res.string.contact_email_name,
                    focusBorderColor = brownColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.px)
                        .margin(bottom = 8.px)
                        .padding(12.px)
                        .backgroundColor(Colors.White)
                        .borderRadius(4.px)
                        .border(1.px, LineStyle.Solid, rgb(200, 200, 200))
                )

                // Title Input
                Input(
                    type = InputType.Text,
                    value = title.value,
                    onValueChange = { title.value = it },
                    placeholder = Res.string.contact_email_subject,
                    focusBorderColor = brownColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.px)
                        .margin(bottom = 8.px)
                        .padding(12.px)
                        .backgroundColor(Colors.White)
                        .borderRadius(4.px)
                        .border(1.px, LineStyle.Solid, rgb(200, 200, 200))
                )

                // Replace the Message Input with TextArea
                TextArea(
                    value = message.value,
                    attrs = {
                        this.onInput {
                            message.value = it.value
                        }
                        attr("placeholder", Res.string.contact_email_subject)
                        style {
                            width(100.percent)
                            height(140.px)
                            marginBottom(24.px)
                            padding(12.px)
                            backgroundColor(Colors.White)
                            borderRadius(4.px)
                            border(1.px, LineStyle.Solid, rgb(200, 200, 200)
                            )
                            property("font-family", "inherit")
                            property("font-size", "inherit")
                            property("resize", "none")
                        }
                    },
                )

                Button(
                    onClick = {
                        val mailtoLink = "mailto:${Constants.EMAIL_ADDRESS}?subject=${title.value}&body=From: ${name.value}%0D%0A%0D%0A${message.value}"
                        window.location.href = mailtoLink
                    },
                    modifier = Modifier
                        .fillMaxWidth(100.percent)
                        .height(36.px)
                        .backgroundColor(if (title.value.isNotEmpty() && message.value.isNotEmpty()) rgb(67, 83, 52) else rgb(200, 200, 200))
                        .color(Colors.White)
                        .borderRadius(4.px)
                        .cursor(Cursor.Pointer)
                        .transition(Transition.of("background-color",300.ms)),
                    type = ButtonType.Submit,
                    enabled = title.value.isNotEmpty() && message.value.isNotEmpty()
                ) {
                    Text(Res.string.contact_email_send_message)
                }
            }
        }


        Box(
            modifier = modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            HomeFooter(breakpoint,modifier)
        }
    }
}