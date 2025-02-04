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
import com.dev0029.ahrarwood.constants.Constants
import com.dev0029.ahrarwood.constants.ImagePaths
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onTouchEnd
import com.varabyte.kobweb.compose.ui.modifiers.onTouchStart
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.delay
import org.w3c.dom.get
import kotlin.math.abs
import kotlin.time.Duration.Companion.seconds

@Composable
fun HomeBanner(
    modifier: Modifier
) {
    val breakpoint = rememberBreakpoint()
    val banners = listOf(ImagePaths.BANNER1, ImagePaths.BANNER2, ImagePaths.BANNER3, ImagePaths.BANNER4)
    val currentIndex = remember { mutableStateOf(0) }
    val containerId = "carouselContainer"
    val isHovered = remember { mutableStateOf(false) }
    
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
            .height(if (breakpoint >= Breakpoint.MD) 420.px else 300.px)
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
        ){
            banners.forEach {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(100.percent)
                        .flex(0, 0, 100.percent)
                ) {
                    Image(
                        src = it,
                        modifier = Modifier
                            .fillMaxSize()
                            .objectFit(if (breakpoint >= Breakpoint.MD) ObjectFit.Cover else ObjectFit.Contain),
                        alt = Constants.BANNER_ALT
                    )
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
                Div(
                    attrs = Modifier
                        .size(if (breakpoint >= Breakpoint.MD) 12.px else 8.px)
                        .backgroundColor(if (index == currentIndex.value) Colors.White else Colors.LightGray)
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