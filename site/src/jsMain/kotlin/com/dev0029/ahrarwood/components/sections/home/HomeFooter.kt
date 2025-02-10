package com.dev0029.ahrarwood.components.sections.home

import androidx.compose.runtime.Composable
import com.dev0029.ahrarwood.SitePalettes
import com.dev0029.ahrarwood.components.widgets.AboutExternalIcon
import com.dev0029.ahrarwood.constants.Constants
import com.dev0029.ahrarwood.constants.ImagePaths
import com.dev0029.ahrarwood.extensions.footerBgColor
import com.dev0029.ahrarwood.extensions.isMobileCompatible
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.px

@Composable
fun HomeFooter(
    breakpoint: Breakpoint,
    modifier: Modifier,
) {
    val isDarkMode = ColorMode.current.isDark
    Box(
        modifier = modifier.fillMaxWidth()
            .backgroundColor(if (isDarkMode) SitePalettes.dark.nearBackground else SitePalettes.light.nearBackground)
            .height(if (breakpoint >= Breakpoint.MD) 96.px else 56.px)
            .padding(leftRight = if (breakpoint >= Breakpoint.MD) 96.px else 16.px)
            ,
    ) {
        val copyRight = "${Res.string.footer_author}/${Constants.VERSION_CODE}"
        SpanText(text = copyRight, modifier = modifier
            .align(Alignment.CenterStart)
            .fontSize(if (breakpoint.isMobileCompatible()) FontSize.Small else FontSize.Unset)
        )
        Row(modifier = modifier
            .align(Alignment.CenterEnd)
            .styleModifier {
                if (isDarkMode)
                    property("filter", "invert(1)")
            }
        ) {
            AboutExternalIcon(modifier,breakpoint, ImagePaths.WHATSAPP, Constants.WHATSAPP_URL)
            AboutExternalIcon(modifier,breakpoint, ImagePaths.ETSY, Constants.ETSY_SHOP_URL)
            AboutExternalIcon(modifier,breakpoint, ImagePaths.INSTAGRAM, Constants.INSTAGRAM_SHOP_URL)
            AboutExternalIcon(modifier,breakpoint, ImagePaths.YOUTUBE, Constants.YOUTUBE_SHOP_URL)
            AboutExternalIcon(modifier,breakpoint, ImagePaths.PINTEREST, Constants.PINTEREST_SHOP_URL)
        }
    }

}