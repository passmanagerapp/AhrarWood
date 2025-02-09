package com.dev0029.ahrarwood.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.dev0029.ahrarwood.components.widgets.TwoWeightText2
import com.dev0029.ahrarwood.constants.ImagePaths
import com.dev0029.ahrarwood.extensions.isMobileCompatible
import com.dev0029.ahrarwood.network.model.Doc
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.forms.TextInput
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaCaretDown
import com.varabyte.kobweb.silk.components.icons.fa.FaSpinner
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*
import org.w3c.dom.HTMLElement

@Composable
fun SearchView(
    modifier: Modifier,
    breakpoint: Breakpoint,
    scope: CoroutineScope,
    isSearchExpanded: Boolean,
    searchText: String,
    onSearchTextChange: (query: String)-> Unit,
    onSearchExpanded: () -> Unit,
    onSubmit: (search:String) -> Unit,
    searchResults: List<Doc> = emptyList(),
    onResultClick: (Doc) -> Unit = {},
    placeHolder: String = Res.string.search_author,
    currentPage: Int = 1,
    totalPages: Int = 1,
    onPageChange: (Int) -> Unit = {}
) {
    val isLoading = remember { mutableStateOf(false) }
    var isCollapse = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .position(Position.Relative)
            .width(360.px)
            .zIndex(1000)
    ) {
        // Search bar
        Row(
            modifier = modifier
                .width(if (isSearchExpanded) 360.px else 40.px)
                .height(40.px)
                .margin(topBottom = if (!breakpoint.isMobileCompatible()) 6.px else 2.px)
                .position(Position.Absolute)  // Fixed position
                .right(0.px)  // Align to right
                .border {
                    width(1.px)
                    style(LineStyle.Solid)
                    color(Color.lightgray)
                }
                .borderRadius(4.px)
                .backgroundColor(Colors.Transparent)
                .transition(Transition.all(300.ms, TransitionTimingFunction.EaseInOut))
                .zIndex(1001),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSearchExpanded) {
                TextInput(
                    text = searchText,
                    onTextChange = { onSearchTextChange(it) },
                    modifier = modifier
                        .fillMaxWidth(85.percent)
                        .height(100.percent)
                        .border(0.px)
                        .padding(leftRight = 12.px)
                        .outline(0.px)
                        .borderRadius(0.px)
                        .color(Colors.White)
                        .backgroundColor(Colors.Transparent)
                        .attrsModifier {
                            attr("placeholder", placeHolder)
                            attr("type", "search")
                            onKeyDown { event ->
                                if (event.key == "Enter" && searchText.isNotEmpty()) {
                                    event.preventDefault()
                                    onSubmit(searchText)
                                    isLoading.value = true
                                }
                            }
                        }
                        .onClick {
                            if (isCollapse.value && searchResults.isNotEmpty()) {
                                isCollapse.value = false
                            }
                        }
                    ,
                    focusBorderColor = Colors.White
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
                                onSubmit(searchText)
                                isLoading.value = true
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
                if (isLoading.value)
                    FaSpinner(modifier = modifier
                        .size(20.px)
                        .color(Colors.White)
                        .styleModifier { property("animation", "fa-spin 1s infinite linear") }
                    )
                else
                    Image(
                        src = ImagePaths.SEARCH_ICON,
                        modifier = modifier
                            .width(20.px)
                            .height(20.px)
                    )
            }
        }

        // Results dropdown
        if (isSearchExpanded && searchResults.isNotEmpty() && !isCollapse.value) {
            isLoading.value = false
            Box(
                modifier = Modifier
                    .position(Position.Absolute)
                    .top(52.px)
                    .right(0.px)
                    .width(360.px)
                    .maxHeight(300.px)
                    .backgroundColor(Colors.White)
                    .borderRadius(4.px)
                    .boxShadow(0.px, 2.px, 4.px,null, rgba(0, 0, 0, 0.1))
                    .overflow(Overflow.Auto)
                    .zIndex(1000)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.px)
                ) {
                    Row(
                        modifier = modifier.fillMaxWidth()
                            .onClick {
                                isCollapse.value= !isCollapse.value
                            }
                    ) {
                        TwoWeightText2(modifier,"${searchResults.size}",Res.string.result_found)
                        FaCaretDown(
                            modifier = modifier.margin(left = 6.px)
                        )
                    }
                    searchResults.forEach { result ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.px)
                                .cursor(Cursor.Pointer)
                                .backgroundColor(Colors.White)
                                .onMouseOver {
                                    (it.target as? HTMLElement)?.style?.backgroundColor = "rgb(243, 244, 246)"
                                }
                                .onMouseLeave {
                                    (it.target as? HTMLElement)?.style?.backgroundColor = "white"
                                }
                                .onClick { onResultClick(result) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                SpanText(
                                    text = result.title,
                                    modifier = Modifier
                                        .padding(left = 8.px)
                                        .color(Colors.Black)
                                )
                                val publisher = if (result.publisher.isNullOrEmpty()) "-" else result.publisher.first()
                                val publishYear = if (result.firstPublishYear == null) "-" else result.firstPublishYear
                                SpanText(
                                    text = "${publisher},$publishYear",
                                    modifier = Modifier
                                        .padding(left = 8.px)
                                        .color(Colors.Black)
                                        .fontSize(12.px)
                                        .fontStyle(FontStyle.Italic)
                                        .fontWeight(FontWeight.Thin)
                                )
                            }

                        }
                    }

                    // pagination
                    /* if (totalPages > 1) {
                         Row(
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .padding(top = 8.px)
                                 .borderRadius(4.px)
                                 .backgroundColor(rgb(243, 244, 246)),
                             horizontalArrangement = Arrangement.Center,
                             verticalAlignment = Alignment.CenterVertically
                         ) {
                             if (currentPage > 1) {
                                 Box(
                                     modifier = Modifier
                                         .padding(8.px)
                                         .cursor(Cursor.Pointer)
                                         .onClick { onPageChange(currentPage - 1) }
                                 ) {
                                     SpanText("←", modifier = Modifier.color(Colors.Black))
                                 }
                             }
                             for (page in 1..totalPages) {
                                 if (page == currentPage) {
                                     Box(
                                         modifier = Modifier
                                             .padding(8.px)
                                             .backgroundColor(Colors.Black)
                                             .borderRadius(4.px)
                                     ) {
                                         SpanText(
                                             page.toString(),
                                             modifier = Modifier
                                                 .padding(leftRight = 8.px)
                                                 .color(Colors.White)
                                         )
                                     }
                                 } else {
                                     Box(
                                         modifier = Modifier
                                             .padding(8.px)
                                             .cursor(Cursor.Pointer)
                                             .onClick { onPageChange(page) }
                                     ) {
                                         SpanText(
                                             page.toString(),
                                             modifier = Modifier
                                                 .padding(leftRight = 8.px)
                                                 .color(Colors.Black)
                                         )
                                     }
                                 }
                             }
                             if (currentPage < totalPages) {
                                 Box(
                                     modifier = Modifier
                                         .padding(8.px)
                                         .cursor(Cursor.Pointer)
                                         .onClick { onPageChange(currentPage + 1) }
                                 ) {
                                     SpanText("→", modifier = Modifier.color(Colors.Black))
                                 }
                             }
                         }
                     }*/
                }
            }
        }
    }
}