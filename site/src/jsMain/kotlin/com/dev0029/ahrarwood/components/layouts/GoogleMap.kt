package com.dev0029.ahrarwood.components.layouts

import androidx.compose.runtime.Composable
import com.dev0029.ahrarwood.constants.Constants
import com.dev0029.ahrarwood.extensions.isMobileCompatible
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Iframe

@Composable
fun GoogleMap(breakpoint: Breakpoint,modifier: Modifier) {
    Box(
        modifier = modifier.width(if (breakpoint.isMobileCompatible()) 360.px else 480.px).height(if (!breakpoint.isMobileCompatible()) 360.px else 240.px)
            .margin(right = if (breakpoint.isMobileCompatible()) 0.px else 30.px),
        contentAlignment = Alignment.Center
    ) {
        Iframe(
            attrs = {
                attr("src", Constants.MAP_URL)
                attr("style", "border:0")
                attr("loading", "lazy")
                attr("referrerpolicy", "no-referrer-when-downgrade")
                attr("width", "100%")
                attr("height", "100%")
            }
        )
    }

}