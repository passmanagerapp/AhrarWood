package com.dev0029.ahrarwood.pages

import androidx.compose.runtime.Composable
import com.dev0029.ahrarwood.components.sections.contact.ContactContent
import com.dev0029.ahrarwood.components.sections.contact.ContactContentMobile
import com.dev0029.ahrarwood.extensions.isMobileCompatible
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document

@Page
@Composable
fun ContactPage(
    modifier: Modifier = Modifier
) {
    document.title = Res.string.title_contact
    val breakpoint = rememberBreakpoint()
     if (breakpoint.isMobileCompatible())
         ContactContentMobile(breakpoint, modifier)
    else
         ContactContent(breakpoint, modifier)
}