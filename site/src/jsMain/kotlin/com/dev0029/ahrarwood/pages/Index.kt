package com.dev0029.ahrarwood.pages

import androidx.compose.runtime.Composable
import com.dev0029.ahrarwood.components.sections.home.HomeBanner
import com.dev0029.ahrarwood.components.sections.home.HomeFooter
import com.dev0029.ahrarwood.components.sections.home.HomeHeader
import com.dev0029.ahrarwood.components.sections.home.HomeShipping
import com.dev0029.ahrarwood.components.sections.home.HomeShippingMobile
import com.dev0029.ahrarwood.constants.PageRoutes
import com.dev0029.ahrarwood.extensions.isMobileCompatible
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.dev0029.ahrarwood.components.sections.home.HomeSection
import com.dev0029.ahrarwood.network.firebase.ChatbaseChatbot
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.document
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.Div

// Container that has a tagline and grid on desktop, and just the tagline on mobile
val HeroContainerStyle = CssStyle {
    base { Modifier.fillMaxWidth().gap(2.cssRem) }
    Breakpoint.MD { Modifier.margin { top(20.vh) } }
}

// A demo grid that appears on the homepage because it looks good
val HomeGridStyle = CssStyle.base {
    Modifier
        .gap(0.5.cssRem)
        .width(70.cssRem)
        .height(18.cssRem)
}

private val GridCellColorVar by StyleVariable<Color>()
val HomeGridCellStyle = CssStyle.base {
    Modifier
        .backgroundColor(GridCellColorVar.value())
        .boxShadow(blurRadius = 0.6.cssRem, color = GridCellColorVar.value())
        .borderRadius(1.cssRem)
}

@Composable
private fun GridCell(color: Color, row: Int, column: Int, width: Int? = null, height: Int? = null) {
    Div(
        HomeGridCellStyle.toModifier()
            .setVariable(GridCellColorVar, color)
            .gridItem(row, column, width, height)
            .toAttrs()
    )
}

@Page
@Composable
fun HomePage(modifier: Modifier = Modifier) {
    document.title = Res.string.title_home
    val ctx = rememberPageContext()
    val breakpoint = rememberBreakpoint()
    val isDarkMode = ColorMode.current.isDark
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeHeader(PageRoutes.HOME,modifier)
        Column(
            modifier = modifier
                .fillMaxSize()
                .margin(top = 102.px),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HomeBanner(modifier)
            HomeSection(breakpoint,modifier)
            if (breakpoint.isMobileCompatible())
                HomeShippingMobile(modifier)
            else
                HomeShipping(modifier)
            HomeFooter(breakpoint,modifier)
        }
    }
}
