package com.dev0029.ahrarwood.components.sections.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.bottom
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flex
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.left
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import kotlinx.browser.document
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import com.dev0029.ahrarwood.SitePalettes
import com.dev0029.ahrarwood.constants.Constants
import com.dev0029.ahrarwood.constants.ImagePaths
import com.dev0029.ahrarwood.constants.PageRoutes
import com.dev0029.ahrarwood.extensions.isMobileCompatible
import com.dev0029.ahrarwood.extensions.primaryColor
import com.dev0029.ahrarwood.extensions.secondaryColor
import com.dev0029.ahrarwood.models.Section
import com.dev0029.ahrarwood.network.firebase.Analytics
import com.dev0029.ahrarwood.utils.Utils
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.functions.Gradient
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.modifiers.backgroundImage
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.onTouchEnd
import com.varabyte.kobweb.compose.ui.modifiers.onTouchStart
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.core.PageContext
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.window
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import kotlin.math.abs
import kotlin.time.Duration.Companion.seconds


@Composable
fun HomeBanner(
    modifier: Modifier,
    ctx: PageContext,
    isDark: Boolean
) {
    val breakpoint = rememberBreakpoint()
    val banners = listOf<Section>(
        Section(
            Res.string.section1_title,
            Res.string.section1_desc,
            Res.string.section1_button,
            navPath = PageRoutes.BOOK_STANDS,
            imagePath = ImagePaths.BRUSH
        ),
        Section(
            Res.string.section4_title,
            Res.string.section4_desc,
            Res.string.section4_button,
            navPath = PageRoutes.CREATE_MINIATURE_LIBRARY,
            imagePath = ImagePaths.LIBRARY
        ),
    )
    val currentIndex = remember { mutableStateOf(0) }
    val containerId = "carouselContainer"
    val isHovered = remember { mutableStateOf(false) }
    val bgColor = if (isDark) SitePalettes.dark.nearBackground else SitePalettes.light.nearBackground

    LaunchedEffect(Unit) {

        while (true) {
            delay(if (breakpoint >= Breakpoint.MD) 5.seconds else 15.seconds)
            if (!isHovered.value) {
                currentIndex.value = (currentIndex.value + 1) % banners.size
                val container = document.getElementById(containerId) as? HTMLDivElement
                container?.scrollTo(
                    x = (container.clientWidth * currentIndex.value).toDouble(),
                    y = 0.0
                )
            }
        }

    }

    DisposableEffect(Unit) {
        onDispose { }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .position(Position.Relative)
            .overflow(Overflow.Hidden)
            .onMouseEnter { isHovered.value = true }
            .onMouseLeave { isHovered.value = false }
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
        ) {
            banners.forEach { banner ->
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(100.percent)
                        .flex(0, 0, 100.percent)
                        .backgroundColor(bgColor)
                        .padding(leftRight = if (!breakpoint.isMobileCompatible()) 96.px else 16.px)
                ) {

                    Column(
                        modifier = modifier.align(Alignment.CenterStart)
                    ) {
                        SpanText(
                            text = banner.title,
                            modifier = modifier.fontSize(FontSize.XXLarge)
                        )
                        SpanText(
                            text = banner.description,
                            modifier = modifier.fontSize(FontSize.Medium)
                        )
                        Button(
                            onClick = {
                                if (banner.navPath.isNullOrEmpty())
                                    return@Button
                                ctx.router.navigateTo(banner.navPath)
                            },
                            modifier = Modifier.backgroundColor(secondaryColor)
                                .padding(topBottom = 12.px, leftRight = 24.px)
                                .margin(topBottom = if (breakpoint.isMobileCompatible()) 32.px else 64.px)
                                .borderRadius(2.px)
                                .cursor(Cursor.Pointer)
                                // Hover effect
                                .styleModifier {
                                    property("transition", "all 0.3s ease")
                                    property("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.1)")
                                    property("transform", "scale(1)")
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
                                },
                            enabled = true
                        ) {
                            SpanText(
                                text = banner.buttonTitle,
                                modifier = Modifier
                                    .color(Colors.SaddleBrown)
                                    .fontSize(if (!breakpoint.isMobileCompatible()) 18.px else 12.px)
                                    .styleModifier {
                                        property("font-weight", "600")
                                    }
                            )
                        }
                    }
                    banner.imagePath?.let { path ->
                        val width = if (breakpoint.isMobileCompatible()) 220.px else 500.px
                        val height = if (breakpoint.isMobileCompatible()) 220.px else 500.px
                        Image(
                            path,
                            modifier = modifier.align(Alignment.CenterEnd)
                                .width(width)
                                .height(height)
                        )
                    }
                }
            }
        }

        // Navigation dots
        Row(
            modifier = Modifier
                .position(Position.Absolute)
                .bottom(if (breakpoint >= Breakpoint.MD) 16.px else 8.px)
                .left(if (breakpoint >= Breakpoint.MD) 24.px else 12.px)
                .gap(if (breakpoint >= Breakpoint.MD) 12.px else 8.px)
        ) {
            repeat(banners.size) { index ->
                val selectedBgColor = if (isDark) Colors.White else Colors.Black
                val unselectedBgColor = if (isDark) Colors.LightSlateGray else Colors.DarkGray
                Div(
                    attrs = Modifier
                        .size(if (breakpoint >= Breakpoint.MD) 12.px else 8.px)
                        .backgroundColor(if (currentIndex.value == index) selectedBgColor else unselectedBgColor)
                        .borderRadius(50.percent)
                        .cursor(Cursor.Pointer)
                        .toAttrs({
                            onClick {
                                currentIndex.value = index
                                val container = document.getElementById(containerId) as? HTMLDivElement
                                container?.scrollTo(
                                    x = (container.clientWidth * index).toDouble(),
                                    y = 0.0
                                )
                            }
                        })
                )
            }
        }

        // Optional: Add touch swipe support for mobile
        if (breakpoint < Breakpoint.MD) {
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
}