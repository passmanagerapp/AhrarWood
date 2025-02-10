package com.dev0029.ahrarwood.components.sections.home

import androidx.compose.runtime.*
import com.dev0029.ahrarwood.SitePalettes
import com.dev0029.ahrarwood.components.sections.ColorModeButton
import com.dev0029.ahrarwood.components.widgets.HomeSectionButton
import com.dev0029.ahrarwood.constants.ImagePaths
import com.dev0029.ahrarwood.constants.PageRoutes
import com.dev0029.ahrarwood.network.firebase.Analytics
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.graphics.Image
import org.jetbrains.compose.web.css.*
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.silk.components.forms.TextInput
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.core.PageContext
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.icons.fa.FaBars
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.common.SmoothColorStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.compose.web.ExperimentalComposeWebApi

@OptIn(ExperimentalComposeWebApi::class)
@Composable
fun HomeHeader(
    currentPath: String,
    modifier: Modifier
) {
    var searchText = remember { mutableStateOf("") }
    var isSearchExpanded = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val ctx = rememberPageContext()
    val breakpoint = rememberBreakpoint()
    var isMobileMenuOpen = remember { mutableStateOf(false) }
    val isDarkMode = ColorMode.current.isDark
    val bgColor = if (isDarkMode) SitePalettes.dark.nearBackground else SitePalettes.light.nearBackground
    Box(modifier = SmoothColorStyle.toModifier()
        .fillMaxWidth()
        .backgroundColor(bgColor)
        .position(Position.Fixed)
        .zIndex(100)
        .padding(leftRight = if (breakpoint >= Breakpoint.MD) 96.px else 16.px),
        contentAlignment = Alignment.TopCenter
    ) {
        Row(
            modifier = modifier
                .margin(topBottom = 16.px)
                .align(Alignment.TopStart)
        ) {
            Image(ImagePaths.LOGO,
                modifier = modifier
                    .styleModifier {
                        if (isDarkMode)
                            property("filter", "invert(1)")
                    }
                    .width(if (breakpoint >= Breakpoint.MD) 216.px else 150.px)
                    .height(if (breakpoint >= Breakpoint.MD) 72.px else 50.px)
                    .cursor(Cursor.Pointer)
                    .onClick { ctx.router.navigateTo(PageRoutes.HOME) }
            )
        }

        if (breakpoint >= Breakpoint.MD) {
            Row(
                modifier = modifier
                    .margin(topBottom = 16.px)
                    .align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBar(
                    modifier = modifier,
                    scope = scope,
                    isSearchExpanded = isSearchExpanded.value,
                    searchText = searchText.value,
                    currentPath = currentPath,
                    ctx = ctx,
                    onSearchTextChange = {searchText.value = it},
                    onSearchExpanded = { isSearchExpanded.value = !isSearchExpanded.value}
                )
            }
        } else {
            Box(
                modifier = modifier
                    .size(40.px)
                    .align(Alignment.CenterEnd)
                    .cursor(Cursor.Pointer)
                    .onClick { isMobileMenuOpen.value = !isMobileMenuOpen.value},
                contentAlignment = Alignment.Center
            ) {
                FaBars()
            }
        }

        if (breakpoint < Breakpoint.MD && isMobileMenuOpen.value) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .position(Position.Fixed)
                    .top(88.px) // Height of header + padding
                    .left(0.px)
                    .backgroundColor(bgColor)
                    .padding(16.px)
                    .boxShadow(0.px, 4.px, 8.px,null, rgba(0, 0, 0, 0.1))
                    .zIndex(99),
                horizontalAlignment = Alignment.Start
            ) {
                SearchBar(
                    modifier = modifier,
                    scope = scope,
                    isSearchExpanded = isSearchExpanded.value,
                    searchText = searchText.value,
                    currentPath = currentPath,
                    ctx = ctx,
                    onSearchTextChange = {searchText.value = it},
                    onSearchExpanded = { isSearchExpanded.value = !isSearchExpanded.value}
                )

            }
        }

    }
}


@OptIn(ExperimentalComposeWebApi::class)
@Composable
fun SearchBar(
    modifier: Modifier,
    scope: CoroutineScope,
    isSearchExpanded: Boolean,
    searchText: String,
    currentPath: String,
    ctx: PageContext,
    onSearchTextChange: (query: String) -> Unit,
    onSearchExpanded: () -> Unit
) {
    val transition = Transition()
    Row(
        modifier = modifier
            .width(if (isSearchExpanded) 240.px else 40.px)
            .height(40.px)
            .margin(right = 24.px)
            .border {
                width(1.px)
                style(LineStyle.Solid)
                color(Color.lightgray)
            }
            .borderRadius(4.px)
            .transition(Transition.all(300.ms,TransitionTimingFunction.EaseInOut)),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSearchExpanded) {
            TextInput(
                text = searchText,
                onTextChange = {onSearchTextChange(it) },
                modifier = modifier
                    .fillMaxWidth(85.percent)
                    .height(100.percent)
                    .border(0.px)
                    .padding(leftRight = 12.px)
                    .outline(0.px)
                    .backgroundColor(Colors.Transparent)
                    .attrsModifier {
                        attr("placeholder", Res.string.search)
                        attr("type", "search")
                    }
            )
        }

        Box(
            modifier = modifier
                .width(40.px)
                .height(40.px)
                .cursor(Cursor.Pointer)
                .padding(8.px)
                .onClick {
                    scope.launch {
                        if (isSearchExpanded && searchText.isNotEmpty()) {
                            Analytics.logEvent("searchSubmit")
                        } else {
                            onSearchExpanded()
                            if (isSearchExpanded) {
                                delay(100)
                            } else {
                                onSearchTextChange("")
                            }
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                src = ImagePaths.SEARCH_ICON,
                modifier = modifier
                    .width(20.px)
                    .height(20.px)
            )
        }
    }

    HomeSectionButton(modifier, Res.string.home, PageRoutes.HOME, currentPath, onClick = {
        ctx.router.navigateTo(it)
    })
    HomeSectionButton(modifier, Res.string.book_stands, PageRoutes.BOOK_STANDS, currentPath, onClick = {
        ctx.router.navigateTo(it)
    })
    HomeSectionButton(modifier, Res.string.create_library, PageRoutes.CREATE_MINIATURE_LIBRARY, currentPath, onClick = {
        ctx.router.navigateTo(it)
    })
    HomeSectionButton(modifier, Res.string.about, PageRoutes.ABOUT,currentPath, onClick = {
        //ctx.router.navigateTo(it)
    })
    HomeSectionButton(modifier, Res.string.contact, PageRoutes.CONTACT, currentPath, onClick = {
        ctx.router.navigateTo(it)
    })
    ColorModeButton()

}