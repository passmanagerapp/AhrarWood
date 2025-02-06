package com.dev0029.ahrarwood.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.dev0029.ahrarwood.base.SharedViewModel
import com.dev0029.ahrarwood.components.layouts.Dialog
import com.dev0029.ahrarwood.components.layouts.SearchView
import com.dev0029.ahrarwood.components.sections.home.HomeFooter
import com.dev0029.ahrarwood.components.sections.home.HomeHeader
import com.dev0029.ahrarwood.constants.PageRoutes
import com.dev0029.ahrarwood.extensions.getRandomHexColor
import com.dev0029.ahrarwood.extensions.ignoreNull
import com.dev0029.ahrarwood.extensions.isMobileCompatible
import com.dev0029.ahrarwood.extensions.primaryColor
import com.dev0029.ahrarwood.extensions.secondaryColor
import com.dev0029.ahrarwood.models.BookDialogModel
import com.dev0029.ahrarwood.models.BookListModel
import com.dev0029.ahrarwood.network.ApiService
import com.dev0029.ahrarwood.network.model.SearchResult
import com.dev0029.ahrarwood.utils.threedmodel.Scene
import com.dev0029.ahrarwood.utils.threedmodel.setupGlftModel
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLElement
import kotlin.random.Random


@Page
@Composable
fun CreateMiniatureLibraryPage(
    modifier: Modifier = Modifier
) {
    val ctx = rememberPageContext()
    val breakpoint = rememberBreakpoint()
    val response = remember { mutableStateOf<SearchResult?>(null) }
    val errorMessage = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var searchText = remember { mutableStateOf("") }
    var isSearchExpanded = remember { mutableStateOf(true) }
    var selectedBooks = remember { mutableStateOf(listOf<BookListModel>()) } //isbn,title,objectId
    var isLoading = remember { mutableStateOf(false) }
    var isDialogOpen = remember { mutableStateOf(BookDialogModel()) }
    var scene = remember { mutableStateOf<Scene?>(null) }
    val removedItemIdList = remember { mutableStateOf< List<String>>(emptyList()) }
    DisposableEffect(Unit) {
        onDispose {
            document.getElementById("loading-three-container")?.let { loadingDiv ->
                loadingDiv.parentElement?.removeChild(loadingDiv)
            }
        }
    }
    Box(modifier = modifier.fillMaxSize()) {
        HomeHeader(PageRoutes.CREATE_MINIATURE_LIBRARY,modifier)
        Box(modifier = modifier
            .fillMaxSize()
            .padding(top = if (!breakpoint.isMobileCompatible()) 104.px else 84.px, bottom = 96.px)
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(if (!breakpoint.isMobileCompatible()) 140.px else 120.px)
                    .padding(leftRight = if (!breakpoint.isMobileCompatible()) 96.px else 16.px)
                    .align(Alignment.TopCenter)
                    .backgroundColor(color = primaryColor),
            ) {
                SpanText(Res.string.library_header_title, modifier = modifier
                    .fontSize(if (!breakpoint.isMobileCompatible()) 36.px else 16.px)
                    .fontWeight(FontWeight.Bold)
                    .color(Colors.White)
                    .align(if (!breakpoint.isMobileCompatible()) Alignment.CenterStart else Alignment.TopStart))
                SpanText(Res.string.library_header_desc,
                    modifier = modifier
                        .fontSize(if (!breakpoint.isMobileCompatible()) 16.px else 12.px)
                        .fontWeight(FontWeight.Thin).color(Colors.White)
                        .align(if (!breakpoint.isMobileCompatible()) Alignment.BottomStart else Alignment.TopStart)
                        .margin(bottom = 8.px, top = if (!breakpoint.isMobileCompatible()) 0.px else 20.px))
                Column(modifier = modifier.fillMaxWidth()
                    .align(if (!breakpoint.isMobileCompatible()) Alignment.CenterEnd else Alignment.Center),
                    horizontalAlignment = Alignment.End) {
                    SpanText(text = "${selectedBooks.value.size}/60",
                        modifier = modifier.color(Colors.White).fontSize(if (!breakpoint.isMobileCompatible()) 20.px else 14.px))
                    SearchView(
                        modifier = modifier,
                        breakpoint = breakpoint,
                        scope = scope,
                        isSearchExpanded = isSearchExpanded.value,
                        searchText = searchText.value,
                        onSearchTextChange = {searchText.value =it},
                        onSearchExpanded = { isSearchExpanded.value = !isSearchExpanded.value},
                        onSubmit = { search ->
                            response.value = null
                            isLoading.value = true
                            scope.launch {
                                ApiService.fetchBooks(author = search)
                                    .onSuccess { result ->
                                        isLoading.value = false
                                        response.value = result.data
                                    }
                                    .onFailure {
                                        isLoading.value = false
                                        errorMessage.value = it.message ?: ""
                                    }
                            }
                        },
                        searchResults = response.value?.docs ?: emptyList(),
                        onResultClick = { doc ->
                            if (selectedBooks.value.size == 60)
                                return@SearchView
                            if (!doc.isbn.isNullOrEmpty()) {
                                if (selectedBooks.value.isNotEmpty() && selectedBooks.value.map { it.isbn }.contains(doc.isbn.first())) {
                                    return@SearchView
                                } else if (!doc.isbn.isEmpty()) {
                                    val randomColor = getRandomHexColor()
                                    val index = selectedBooks.value.size + 1
                                    val bookId = if (removedItemIdList.value.isNotEmpty()) removedItemIdList.value.last().ignoreNull() else "book$index"
                                    selectedBooks.value = selectedBooks.value + BookListModel(bookId,doc.isbn.first()
                                        ,doc.title,doc.author_name.first(),doc.publisher?.firstOrNull(),doc.firstPublishYear)
                                    val targetObject = scene.value?.getObjectByName(bookId)
                                    targetObject?.material?.asDynamic()?.color?.setHex(randomColor)
                                    if (removedItemIdList.value.isNotEmpty()) {
                                        removedItemIdList.value = removedItemIdList.value - bookId
                                    }
                                }
                            }
                        },
                    )
                }
            }
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = 160.px)
                    .align(Alignment.CenterStart),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = modifier.fillMaxWidth()
                        .fillMaxHeight()
                        .align(Alignment.Center)
                    ,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Div(attrs = {
                        classes(ModelStyles.modelContainer)
                        id("three-container") // ID to target the container
                        style {
                            //  border(1.px, LineStyle.Solid,rgb(200,200,200))
                        }
                    }) {
                        LaunchedEffect(Unit) {
                            scene.value = setupGlftModel("three-container", onModelClick = { id ->
                                val book = selectedBooks.value.firstOrNull { it.id == id }
                                book?.let {
                                    val title = book.title
                                    val coverUrl = "https://covers.openlibrary.org/b/isbn/${book.isbn}-L.jpg"
                                    isDialogOpen.value = BookDialogModel(true,id,title,coverUrl)
                                }
                            })
                        }
                    }
                }
            }

            Button(
                onClick = {
                    val uniqueId = Random.nextInt(100000,999999)
                    SharedViewModel.setItems(Pair("AW-$uniqueId",selectedBooks.value))
                    ctx.router.navigateTo(PageRoutes.CREATE_MINIATURE_LIBRARY_ORDER)
                },
                modifier = Modifier.backgroundColor(secondaryColor)
                    .padding(topBottom = 12.px, leftRight = 24.px)
                    .margin(32.px)
                    .borderRadius(25.px)
                    .cursor(Cursor.Pointer)
                    .align(Alignment.BottomEnd)
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
                enabled =  selectedBooks.value.size == 60
            ) {
                SpanText(
                    text = Res.string.section2_button,
                    modifier = Modifier
                        .color(Colors.SaddleBrown)
                        .fontSize(if (!breakpoint.isMobileCompatible()) 18.px else 12.px)
                        .styleModifier {
                            property("font-weight", "600")
                        }
                )
            }

        }

        Box(
            modifier = modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            HomeFooter(breakpoint,modifier)
        }

        if (isDialogOpen.value.isOpen) {
            Dialog(
                isOpen = isDialogOpen.value.isOpen,
                onClose = {isDialogOpen.value = BookDialogModel()},
                title = isDialogOpen.value.title,
                coverUrl = isDialogOpen.value.coverUrl,
                id = isDialogOpen.value.id,
                onRemove = { id ->
                    val book = selectedBooks.value.firstOrNull { it.id == id }
                    if (book == null)
                        return@Dialog
                    removedItemIdList.value = removedItemIdList.value + book.id
                    selectedBooks.value = selectedBooks.value - book
                    isDialogOpen.value = BookDialogModel()
                    val targetObject = scene.value?.getObjectByName(id)
                    targetObject?.material?.asDynamic()?.color?.setHex(0xffffff)
                }
            )
        }
    }

}
object ModelStyles : StyleSheet() {
    val modelContainer by style {
        property("overflow", "hidden")
        property("position", "relative")
    }
}
