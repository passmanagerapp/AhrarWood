package com.dev0029.ahrarwood.components.sections.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.dev0029.ahrarwood.constants.ImagePaths
import com.dev0029.ahrarwood.constants.PageRoutes
import com.dev0029.ahrarwood.extensions.isMobileCompatible
import com.dev0029.ahrarwood.models.Section
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.bottom
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flex
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.left
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.onTouchEnd
import com.varabyte.kobweb.compose.ui.modifiers.onTouchStart
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import kotlinx.browser.document
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import kotlin.math.abs

@Composable
fun HomeSection(
    breakpoint: Breakpoint,
    modifier: Modifier
) {
    val ctx = rememberPageContext()
    val banners = listOf<Section>(
        Section(title = Res.string.section1_title, description = Res.string.section1_desc,
            buttonTitle = Res.string.section1_button, navPath = PageRoutes.PACKAGING, imagePath = ImagePaths.SECTION1),
        Section(title = Res.string.section2_title,Res.string.section2_desc,
            buttonTitle = Res.string.section2_button, navPath = null, imagePath = ImagePaths.SECTION2),
        Section(title = Res.string.section3_title,Res.string.section3_desc,
            buttonTitle = Res.string.section3_button, navPath = PageRoutes.CREATE_MINIATURE_LIBRARY, imagePath = ImagePaths.SECTION3),
    )
    val currentIndex = remember { mutableStateOf(0) }
    val containerId = "sectionContainer"
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(if (!breakpoint.isMobileCompatible()) 640.px else 320.px)
            .position(Position.Relative)
            .overflow(Overflow.Hidden)
            .margin(topBottom = if (!breakpoint.isMobileCompatible()) 64.px else 32.px)
    ) {
        Div(
            attrs = Modifier
                .id(containerId)
                .fillMaxSize()
                .display(DisplayStyle.Flex)
                .whiteSpace(WhiteSpace.NoWrap)
                .overflow(Overflow.Hidden)
                .scrollBehavior(ScrollBehavior.Smooth)
                .toAttrs()
        ){
            banners.forEach { section ->
                Row(
                    modifier  = modifier.backgroundColor(Color("#A96532"))
                        .color(Colors.White)
                        .fillMaxHeight()
                        .width(100.percent)
                        .flex(0,0,100.percent)
                        .padding(leftRight = if (!breakpoint.isMobileCompatible()) 96.px else 16.px),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        SpanText(section.title,
                            modifier = modifier
                                .fontSize(if (!breakpoint.isMobileCompatible()) 48.px else 24.px).margin(bottom = 12.px))
                        SpanText(
                            section.description,
                            modifier = modifier
                                .textOverflow(TextOverflow.Clip)
                                .whiteSpace(WhiteSpace.PreWrap)
                                .margin(bottom = 16.px)
                                .styleModifier {
                                    property("word-wrap", "break-word")
                                    property("line-height", "1.5")
                                }.apply {
                                    if (breakpoint.isMobileCompatible())
                                        fontSize(14.px)
                                }
                        )
                        Box(modifier = Modifier.height(24.px))

                        Box(
                            modifier = Modifier
                                .backgroundColor(Colors.White)
                                .padding(topBottom = 12.px, leftRight = 24.px)
                                .borderRadius(25.px)
                                .cursor(Cursor.Pointer)
                                // Hover effect
                                .styleModifier {
                                    property("transition", "all 0.3s ease")
                                    property("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.1)")
                                    property("transform", "scale(1)")
                                }
                                .onClick {
                                    section.navPath?.let { path ->
                                        ctx.router.navigateTo(path)
                                    }
                                 //   window.open(Constants.ETSY_SHOP_URL, target = "_blank")
                                }
                                // Hover states
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
                        ) {
                            SpanText(
                                text = section.buttonTitle,
                                modifier = Modifier
                                    .color(Colors.SaddleBrown)
                                    .fontSize(if (!breakpoint.isMobileCompatible()) 18.px else 12.px)
                                    .styleModifier {
                                        property("font-weight", "600")
                                    }
                            )
                        }
                    }
                    if (!breakpoint.isMobileCompatible())
                    Column(
                        modifier = modifier
                            .height(225.px)
                            .width(450.px),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = modifier
                                .fillMaxSize()
                                .backgroundColor(Colors.Transparent)
                                .borderRadius(32.px)
                                .overflow(Overflow.Hidden)
                                .styleModifier {
                                    property("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.1)")
                                }
                        ) {
                            Image(
                                src = section.imagePath,
                                modifier = Modifier
                                    .fillMaxSize(),
                                alt = "About Image"
                            )
                        }
                    }

                }

            }

        }

        Row(
            modifier = Modifier
                .position(Position.Absolute)
                .bottom(8.px)
                .left(16.px)
                .gap(8.px)
        ) {
            repeat(banners.size) { index ->
                Div(
                    attrs = Modifier
                        .size(12.px)
                        .backgroundColor(if (index == currentIndex.value) Colors.White else Colors.LightGray)
                        .borderRadius(50.percent)
                        .cursor(Cursor.Pointer)
                        .toAttrs({
                            onClick {
                                currentIndex.value = index
                                val container = document.getElementById(containerId) as? HTMLDivElement
                                container?.scrollTo(x = (container.clientWidth * index).toDouble(), y = 0.0)
                            }
                        })
                )
            }
        }
    }

    if (breakpoint.isMobileCompatible()) {
        var startX = remember { mutableStateOf(0f) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .position(Position.Absolute)
                .onTouchStart { e ->
                    startX.value = e.touches[0]?.clientX?.toFloat() ?: 0f
                }
                .onTouchEnd { e ->
                    val endX = e.changedTouches[0]?.clientX?.toFloat() ?: 0f
                    val diff = startX.value - endX

                    if (abs(diff) > 50) { // Minimum swipe distance
                        if (diff > 0 && currentIndex.value < banners.size - 1) {
                            // Swipe left
                            currentIndex.value++
                        } else if (diff < 0 && currentIndex.value > 0) {
                            // Swipe right
                            currentIndex.value--
                        }

                        val container = document.getElementById(containerId) as? HTMLDivElement
                        container?.scrollTo(
                            x = (container.clientWidth * currentIndex.value).toDouble(),
                            y = 0.0
                        )
                    }
                }
        ) {

        }
    }

}