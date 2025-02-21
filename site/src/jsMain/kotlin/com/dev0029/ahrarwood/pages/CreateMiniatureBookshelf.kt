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
import com.dev0029.ahrarwood.components.sections.TourGuide
import com.dev0029.ahrarwood.components.sections.TourStep
import com.dev0029.ahrarwood.components.sections.home.HomeHeader
import com.dev0029.ahrarwood.components.widgets.DoubleBoxColor
import com.dev0029.ahrarwood.components.widgets.TwoWeightText
import com.dev0029.ahrarwood.constants.Constants
import com.dev0029.ahrarwood.constants.PageRoutes
import com.dev0029.ahrarwood.extensions.getRandomHexColor
import com.dev0029.ahrarwood.extensions.ignoreNull
import com.dev0029.ahrarwood.extensions.isMobileCompatible
import com.dev0029.ahrarwood.extensions.maunColor
import com.dev0029.ahrarwood.extensions.primaryColor
import com.dev0029.ahrarwood.extensions.secondaryColor
import com.dev0029.ahrarwood.models.BookDialogModel
import com.dev0029.ahrarwood.models.BookListModel
import com.dev0029.ahrarwood.models.WoodPaint
import com.dev0029.ahrarwood.network.ApiService
import com.dev0029.ahrarwood.network.firebase.Analytics
import com.dev0029.ahrarwood.network.model.SearchResult
import com.dev0029.ahrarwood.utils.Utils
import com.dev0029.ahrarwood.utils.threedmodel.MeshPhysicalMaterial
import com.dev0029.ahrarwood.utils.threedmodel.Scene
import com.dev0029.ahrarwood.utils.threedmodel.Texture
import com.dev0029.ahrarwood.utils.threedmodel.library.setupLibraryGlftModel
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontSize
import com.varabyte.kobweb.compose.css.FontWeight
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
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.left
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLElement
import kotlin.random.Random


@Page
@Composable
fun CreateMiniatureBookshelfPage(
    modifier: Modifier = Modifier
) {
    document.title = Res.string.title_bookshelf
    val ctx = rememberPageContext()
    val modelQuery = ctx.route.params["model"]
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
    val width = if (breakpoint.isMobileCompatible()) 360.0 else 1080.0
    val height = if (breakpoint.isMobileCompatible()) 220.0 else 690.0
    val currentIndex = remember { mutableStateOf(if (modelQuery == "1") 1 else 1) }
    val colorIndex = remember { mutableStateOf(0) }
    var textureExterior = remember { mutableStateOf<Texture?>(null) }
    var textureInterior = remember { mutableStateOf<Texture?>(null) }
    val isDark = ColorMode.current.isDark
    var showTour = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(250)
        showTour.value = true
    }
    val tourSteps = listOf(
        TourStep(
            target = "model",
            title = "Select model",
            description = "Click the dot button and see other models",
            position = com.dev0029.ahrarwood.components.sections.Position.Top
        ),
        TourStep(
            target = "color-picker",
            title = Res.string.choose_color,
            description = Res.string.choose_color_desc,
            position = com.dev0029.ahrarwood.components.sections.Position.Bottom
        ),
        TourStep(
            target = "order-button",
            title = Res.string.place_order,
            description = Res.string.place_order_desc,
            position = com.dev0029.ahrarwood.components.sections.Position.Top
        )
    )

    DisposableEffect(Unit) {
        onDispose {
            document.getElementById("loading-three-container")?.let { loadingDiv ->
                loadingDiv.parentElement?.removeChild(loadingDiv)
            }
        }
    }
    Column(modifier = modifier.fillMaxSize()) {
        HomeHeader(PageRoutes.CREATE_MINIATURE_LIBRARY,modifier)
        Box(modifier = modifier
            .fillMaxSize()
            .margin(top = if (!breakpoint.isMobileCompatible()) 104.px else 84.px),
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(if (!breakpoint.isMobileCompatible()) 110.px else 90.px)
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
                if (currentIndex.value == 0)
                Column(modifier = modifier
                    .align(if (!breakpoint.isMobileCompatible()) Alignment.TopEnd else Alignment.Center),
                    horizontalAlignment = Alignment.End) {
                    SpanText(text = "${selectedBooks.value.size}/60",
                        modifier = modifier.color(Colors.White).fontSize(if (!breakpoint.isMobileCompatible()) 18.px else 12.px))
                    SearchView(
                        modifier = modifier,
                        breakpoint = breakpoint,
                        scope = scope,
                        isSearchExpanded = isSearchExpanded.value,
                        searchText = searchText.value,
                        onSearchTextChange = {searchText.value =it},
                        onSearchExpanded = { isSearchExpanded.value = !isSearchExpanded.value},
                        onSubmit = { search ->
                            Analytics.logEvent("pageEvent:bookshelf:searchAuthor")
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
            Column(
                modifier = modifier
                    .id("color-picker")
                    .align(if (breakpoint.isMobileCompatible()) Alignment.TopCenter else Alignment.CenterStart)
                    .margin(
                        top = if (!breakpoint.isMobileCompatible()) 24.px else 100.px,
                        left = if (!breakpoint.isMobileCompatible()) 24.px else 0.px,
                    )
            ) {
                val material = if (currentIndex.value == 0) Res.string.pine else Res.string.beech_pine
                TwoWeightText(modifier,Res.string.material,material,breakpoint,showIcon = false)
                if (currentIndex.value == 0)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                ) {
                    SpanText(
                        text = Res.string.color,
                        modifier = modifier
                            .fontWeight(FontWeight.Normal)
                            .fontSize(if (!breakpoint.isMobileCompatible()) FontSize.Inherit else FontSize.Small)
                    )
                    Box(
                        modifier = modifier
                            .backgroundColor(Color("#4b646d"))
                            .borderRadius(5.px)
                            .margin(left = 8.px)
                            .styleModifier {
                                property("width", "40px")
                                property("height", "40px")
                            }
                    )
                }
                if (currentIndex.value == 1) {
                    SpanText(
                        text = Res.string.interior_exterior,
                        modifier = modifier
                            .fontWeight(FontWeight.Normal)
                            .fontSize(if (!breakpoint.isMobileCompatible()) FontSize.Inherit else FontSize.Small)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                    ) {
                        DoubleBoxColor(
                            modifier = modifier,
                            paint = WoodPaint(Res.string.oak_white,Color("#aa7c4f"),Color("#d7d6c8")),
                            breakpoint = breakpoint,
                            index = 0,
                            selectedIndex = colorIndex.value,
                            onClick = {
                                Analytics.logEvent("pageEvent:bookshelf:color0")
                                if (scene.value == null)
                                    return@DoubleBoxColor
                                colorIndex.value = it
                                val materialExterior = scene.value?.getObjectByName("wood_white")?.material as MeshPhysicalMaterial
                                val materialInterior = scene.value?.getObjectByName("wood_brown")?.material as MeshPhysicalMaterial
                                if (textureExterior.value == null) {
                                    textureExterior.value = materialExterior.map
                                    textureInterior.value = materialInterior.map
                                }
                                materialExterior.map = textureExterior.value
                                materialExterior.color = com.dev0029.ahrarwood.utils.threedmodel.Color()
                                materialExterior.needsUpdate = true

                                materialInterior.map = textureInterior.value
                                materialInterior.color = com.dev0029.ahrarwood.utils.threedmodel.Color()
                                materialInterior.needsUpdate = true
                            }
                        )
                        DoubleBoxColor(
                            modifier = modifier,
                            paint = WoodPaint(Res.string.honey_walnut,Color("#c08951"),Color("#614e40")),
                            breakpoint = breakpoint,
                            index = 1,
                            selectedIndex = colorIndex.value,
                            onClick = {
                                Analytics.logEvent("pageEvent:bookshelf:color1")
                                if (scene.value == null)
                                    return@DoubleBoxColor
                                colorIndex.value = it
                                val materialExterior = scene.value?.getObjectByName("wood_white")?.material as MeshPhysicalMaterial
                                val materialInterior = scene.value?.getObjectByName("wood_brown")?.material as MeshPhysicalMaterial
                                if (textureExterior.value == null) {
                                    textureExterior.value = materialExterior.map
                                    textureInterior.value = materialInterior.map
                                }
                                materialInterior.map = textureInterior.value
                                materialInterior.color.set(0xc08951)
                                materialInterior.needsUpdate = true

                                materialExterior.map = textureExterior.value
                                materialExterior.color.set(0x614e40)
                                materialExterior.needsUpdate = true
                            }
                        )
                        //c16f5f
                        DoubleBoxColor(
                            modifier = modifier,
                            paint = WoodPaint(Res.string.maun_beech,Color("#8d372d"),Color("#c8ab7b")),
                            breakpoint = breakpoint,
                            index = 2,
                            selectedIndex = colorIndex.value,
                            onClick = {
                                Analytics.logEvent("pageEvent:bookshelf:color2")
                                if (scene.value == null)
                                    return@DoubleBoxColor
                                colorIndex.value = it
                                val materialExterior = scene.value?.getObjectByName("wood_white")?.material as MeshPhysicalMaterial
                                val materialInterior = scene.value?.getObjectByName("wood_brown")?.material as MeshPhysicalMaterial
                                if (textureExterior.value == null) {
                                    textureExterior.value = materialExterior.map
                                    textureInterior.value = materialInterior.map
                                }
                                materialInterior.map = textureInterior.value
                                materialInterior.color.set(maunColor)
                                materialInterior.needsUpdate = true

                                materialExterior.map = textureExterior.value
                                materialExterior.color.set(0xb65c53)
                                materialExterior.needsUpdate = true
                            }
                        )
                        DoubleBoxColor(
                            modifier = modifier,
                            paint = WoodPaint(Res.string.white_white,Color("#d7d6c8"),Color("#d7d6c8")),
                            breakpoint = breakpoint,
                            index = 3,
                            selectedIndex = colorIndex.value,
                            onClick = {
                                Analytics.logEvent("pageEvent:bookshelf:color3")
                                if (scene.value == null)
                                    return@DoubleBoxColor
                                colorIndex.value = it
                                val materialExterior = scene.value?.getObjectByName("wood_white")?.material as MeshPhysicalMaterial
                                val materialInterior = scene.value?.getObjectByName("wood_brown")?.material as MeshPhysicalMaterial
                                if (textureExterior.value == null) {
                                    textureExterior.value = materialExterior.map
                                    textureInterior.value = materialInterior.map
                                }
                                materialInterior.map = null
                                materialInterior.color.set(0xd7d6c8)
                                materialInterior.needsUpdate = true

                                materialExterior.map = null
                                materialExterior.color.set(0xd7d6c8)
                                materialExterior.needsUpdate = true
                            }
                        )
                        DoubleBoxColor(
                            modifier = modifier,
                            paint = WoodPaint(Res.string.walnut_walnut,Color("#c17d50"),Color("#614e40")),
                            breakpoint = breakpoint,
                            index = 4,
                            selectedIndex = colorIndex.value,
                            onClick = {
                                Analytics.logEvent("pageEvent:bookshelf:color4")
                                if (scene.value == null)
                                    return@DoubleBoxColor
                                colorIndex.value = it
                                val materialExterior = scene.value?.getObjectByName("wood_white")?.material as MeshPhysicalMaterial
                                val materialInterior = scene.value?.getObjectByName("wood_brown")?.material as MeshPhysicalMaterial
                                if (textureExterior.value == null) {
                                    textureExterior.value = materialExterior.map
                                    textureInterior.value = materialInterior.map
                                }
                                materialInterior.map = textureInterior.value
                                materialInterior.color.set(0xc17d50)
                                materialInterior.needsUpdate = true

                                materialExterior.map = textureExterior.value
                                materialExterior.color.set(0x614e40)
                                materialExterior.needsUpdate = true
                            }
                        )
                    }
                }
            }

            Box(
                modifier = modifier
                    .margin(top = if (!breakpoint.isMobileCompatible()) 110.px else 90.px)
                    .align(Alignment.Center),
            ) {
                Box(
                    modifier = modifier.align(Alignment.Center)
                ) {
                    Div(attrs = {
                        classes(ModelStyles.modelContainer)
                        id("three-container") // ID to target the container
                        style {
                          //  border(1.px, LineStyle.Solid,rgb(200,200,200))
                        }
                    }) {
                        LaunchedEffect(currentIndex.value) {
                            scene.value?.let { oldScene ->
                                // Remove all objects from the scene
                                while (oldScene.children.isNotEmpty()) {
                                    oldScene.remove(oldScene.children[0])
                                }
                                // Remove the renderer's DOM element
                                document.getElementById("three-container")?.let { container ->
                                    container.innerHTML = ""
                                }
                            }
                            scene.value = setupLibraryGlftModel(
                                containerId = "three-container",
                                width = width,
                                height = height,
                                index = currentIndex.value,
                                onModelClick = { id ->
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
                    Analytics.logEvent("pageEvent:bookshelf:orderButton${currentIndex.value}")
                    if (currentIndex.value == 0) {
                        val uniqueId = Random.nextInt(100000,999999)
                        SharedViewModel.setItems(Pair("AW-$uniqueId",selectedBooks.value))
                        ctx.router.navigateTo(PageRoutes.CREATE_MINIATURE_LIBRARY_ORDER)
                    } else {
                        window.open("${Constants.ETSY_MINIBOOK2_URL}${Utils.getVariationNumber(colorIndex.value)}", target = "_blank")
                    }
                },
                modifier = Modifier
                    .id("order-button")
                    .backgroundColor(secondaryColor)
                    .padding(topBottom = 12.px, leftRight = 24.px)
                    .margin(topBottom = if (breakpoint.isMobileCompatible()) 32.px else 64.px, leftRight = 32.px)
                    .borderRadius(25.px)
                    .cursor(if (currentIndex.value == 0) {
                        if (selectedBooks.value.size == 60) Cursor.Pointer else Cursor.NotAllowed
                    } else Cursor.Pointer)
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
                enabled =  if (currentIndex.value == 0) selectedBooks.value.size == 60 else true
            ) {
                SpanText(
                    text = Res.string.section3_button,
                    modifier = Modifier
                        .color(Colors.SaddleBrown)
                        .fontSize(if (!breakpoint.isMobileCompatible()) 18.px else 12.px)
                        .styleModifier {
                            property("font-weight", "600")
                        }
                )
            }

            Row(
                modifier = Modifier
                    .id("model")
                    .align(Alignment.BottomCenter)
                    .position(Position.Absolute)
                    .bottom(if (breakpoint >= Breakpoint.MD) 16.px else 8.px)
                    .left(if (breakpoint >= Breakpoint.MD) 24.px else 12.px)
                    .gap(if (breakpoint >= Breakpoint.MD) 12.px else 8.px)
            ) {
                repeat(2) { index ->
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
                                }
                            })
                    )
                }
            }


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

    if (showTour.value)
        TourGuide(
            steps = tourSteps,
            id = Constants.HAS_SEEN_TOUR_BOOKSHELF,
            onComplete = {}
        )

}
object ModelStyles : StyleSheet() {
    val modelContainer by style {
        property("overflow", "hidden")
        property("position", "relative")
    }
}
