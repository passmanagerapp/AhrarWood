package com.dev0029.ahrarwood.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.dev0029.ahrarwood.base.SharedViewModel
import com.dev0029.ahrarwood.components.layouts.SearchView
import com.dev0029.ahrarwood.components.sections.TourGuide
import com.dev0029.ahrarwood.components.sections.TourStep
import com.dev0029.ahrarwood.components.sections.home.HomeHeader
import com.dev0029.ahrarwood.components.widgets.BoxColor
import com.dev0029.ahrarwood.components.widgets.TwoWeightText
import com.dev0029.ahrarwood.constants.Constants
import com.dev0029.ahrarwood.constants.PageRoutes
import com.dev0029.ahrarwood.enums.SearchViewType
import com.dev0029.ahrarwood.extensions.isMobileCompatible
import com.dev0029.ahrarwood.extensions.primaryColor
import com.dev0029.ahrarwood.extensions.secondaryColor
import com.dev0029.ahrarwood.models.WoodPaint
import com.dev0029.ahrarwood.network.firebase.Analytics
import com.dev0029.ahrarwood.utils.Utils
import com.dev0029.ahrarwood.utils.threedmodel.FontLoader
import com.dev0029.ahrarwood.utils.threedmodel.Mesh
import com.dev0029.ahrarwood.utils.threedmodel.MeshPhysicalMaterial
import com.dev0029.ahrarwood.utils.threedmodel.MeshStandardMaterial
import com.dev0029.ahrarwood.utils.threedmodel.Object3D
import com.dev0029.ahrarwood.utils.threedmodel.Scene
import com.dev0029.ahrarwood.utils.threedmodel.TextGeometry
import com.dev0029.ahrarwood.utils.threedmodel.TextGeometryParameters
import com.dev0029.ahrarwood.utils.threedmodel.Texture
import com.dev0029.ahrarwood.utils.threedmodel.stand.setupStandGlftModel
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
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.forms.Checkbox
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgb
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement
import kotlin.random.Random

@Page
@Composable
fun BookStands(
    modifier: Modifier = Modifier
) {
    document.title = Res.string.title_book_stands
    val breakpoint = rememberBreakpoint()
    var scene = remember { mutableStateOf<Scene?>(null) }
    var texture = remember { mutableStateOf<Texture?>(null) }
    val currentIndex = remember { mutableStateOf(0) }
    var searchText = remember { mutableStateOf("") }
    var isSearchExpanded = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    var showTour = remember { mutableStateOf(false) }
    var isShowBook = remember { mutableStateOf(true) }
    var removedBookObj = remember { mutableStateOf<Object3D?>(null) }
    var removedBookParent = remember { mutableStateOf<Object3D?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            scene.value = null
            removedBookObj.value = null
            removedBookParent.value = null
        }
    }

    LaunchedEffect(Unit) {
        delay(250)
        showTour.value = true
    }
    val tourSteps = listOf(
        TourStep(
            target = "color-picker",
            title = Res.string.choose_color,
            description = Res.string.choose_color_desc,
            position = com.dev0029.ahrarwood.components.sections.Position.Bottom
        ),
        TourStep(
            target = "search-bar",
            title = Res.string.add_personalization,
            description = Res.string.add_personalization_desc,
            position = com.dev0029.ahrarwood.components.sections.Position.Bottom
        ),
        TourStep(
            target = "order-button",
            title = Res.string.place_order,
            description = Res.string.place_order_desc,
            position = com.dev0029.ahrarwood.components.sections.Position.Top
        )
    )
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        HomeHeader(PageRoutes.BOOK_STANDS, modifier)
        Box(
            modifier = modifier
                .fillMaxSize()
                .margin(top = if (!breakpoint.isMobileCompatible()) 104.px else 84.px),
        ) {
            // SubHeader
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(if (!breakpoint.isMobileCompatible()) 110.px else 90.px)
                    .padding(leftRight = if (!breakpoint.isMobileCompatible()) 96.px else 16.px)
                    .align(Alignment.TopCenter)
                    .backgroundColor(color = primaryColor),
            ) {
                SpanText(Res.string.book_stands_header_title, modifier = modifier
                    .fontSize(if (!breakpoint.isMobileCompatible()) 36.px else 16.px)
                    .fontWeight(FontWeight.Bold)
                    .color(Colors.White)
                    .align(if (!breakpoint.isMobileCompatible()) Alignment.CenterStart else Alignment.TopStart))
                SpanText("${Res.string.book_stands_header_desc} -> ${Res.string.triangle_stand}",
                    modifier = modifier
                        .fontSize(if (!breakpoint.isMobileCompatible()) 16.px else 12.px)
                        .fontWeight(FontWeight.Thin).color(Colors.White)
                        .align(if (!breakpoint.isMobileCompatible()) Alignment.BottomStart else Alignment.TopStart)
                        .margin(bottom = 8.px, top = if (!breakpoint.isMobileCompatible()) 0.px else 20.px))
                Column(modifier = modifier
                    .align(if (!breakpoint.isMobileCompatible()) Alignment.CenterEnd else Alignment.BottomEnd),
                    horizontalAlignment = Alignment.End) {
                    SearchView(
                        modifier = modifier.id("search-bar"),
                        type = SearchViewType.SUBMIT,
                        breakpoint = breakpoint,
                        scope = scope,
                        isSearchExpanded = isSearchExpanded.value,
                        searchText = searchText.value,
                        onSearchTextChange = {searchText.value =it},
                        onSearchExpanded = { isSearchExpanded.value = !isSearchExpanded.value},
                        onSubmit = { search ->
                            Analytics.logEvent("pageEvent:bookstand:personalization")
                            scene.value?.children?.find { it.asDynamic().userData.id == 1234 }?.let {
                                scene.value?.remove(it)
                            }
                            val size = if (search.trim().count() <= 25) 0.2 else 0.1
                            val text = if (search.trim().count() >= 25) Utils.splitSentenceInHalf(search) else search
                            val fontLoader = FontLoader()
                            fontLoader.load("fonts/helvetiker.json", onLoad = { font ->
                                val geometry = TextGeometry(text,
                                    parameters = object : TextGeometryParameters {
                                        override var font = font
                                        override var size = size
                                        override var depth = 0.1
                                        override var bevelEnabled = true
                                        override var bevelThickness = 0.005
                                        override var bevelSize = 0.005
                                    })
                                geometry.computeBoundingBox()
                                val boundingBox = geometry.boundingBox
                                val textWidth = boundingBox?.max?.x?.minus(boundingBox.min.x) ?: 0.0
                                val textMaterial = MeshStandardMaterial(type = "MeshStandardMaterial", name = "text", needsUpdate = false)
                                textMaterial.color = com.dev0029.ahrarwood.utils.threedmodel.Color().set(0x000000)
                                val textMesh = Mesh(geometry, textMaterial)
                                textMesh.asDynamic().userData.id = 1234
                                textMesh.position.set(-textWidth/2,0.0,-1.9)
                                scene.value?.add(textMesh)
                            })
                        },
                        searchResults = emptyList(),
                        onResultClick = {},
                        placeHolder = Res.string.enter_personalization
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
                TwoWeightText(modifier,Res.string.material,Res.string.beech,breakpoint,false)
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
                    BoxColor(
                        modifier = modifier,
                        paint = WoodPaint(Res.string.natural,Color("#D7B899")),
                        breakpoint = breakpoint,
                        index = 0,
                        selectedIndex = currentIndex.value,
                        onClick = {
                            if (scene.value == null)
                                return@BoxColor
                            currentIndex.value = it
                            val material = scene.value?.getObjectByName("Box")?.material as MeshPhysicalMaterial
                            if (texture.value == null) {
                                texture.value = material.map
                            }
                            material.map = texture.value
                            material.color = com.dev0029.ahrarwood.utils.threedmodel.Color()
                            material.needsUpdate = true
                        }
                    )
                    BoxColor(
                        modifier = modifier,
                        paint = WoodPaint(Res.string.white,Color("#d7d6c8")),
                        breakpoint = breakpoint,
                        index = 1,
                        selectedIndex = currentIndex.value,
                        onClick = {
                            if (scene.value == null)
                                return@BoxColor
                            currentIndex.value = it
                            val material = scene.value?.getObjectByName("Box")?.material as MeshPhysicalMaterial
                            if (texture.value == null) {
                                texture.value = material.map
                            }
                            material.map = null
                            material.color.set(0xd7d6c8)
                            material.needsUpdate = true
                        }
                    )
                    BoxColor(
                        modifier = modifier,
                        paint = WoodPaint(Res.string.matte_black,Color("#0a0c09")),
                        breakpoint = breakpoint,
                        index = 2,
                        selectedIndex = currentIndex.value,
                        onClick = {
                            if (scene.value == null)
                                return@BoxColor
                            currentIndex.value = it
                            val material = scene.value?.getObjectByName("Box")?.material as MeshPhysicalMaterial
                            if (texture.value == null) {
                                texture.value = material.map
                            }
                            material.map = texture.value
                            material.color.set(0x0a0c09)
                            material.needsUpdate = true
                        }
                    )
                    BoxColor(
                        modifier = modifier,
                        paint = WoodPaint(Res.string.honey,Color("#c08951")),
                        breakpoint = breakpoint,
                        index = 3,
                        selectedIndex = currentIndex.value,
                        onClick = {
                            if (scene.value == null)
                                return@BoxColor
                            currentIndex.value = it
                            val material = scene.value?.getObjectByName("Box")?.material as MeshPhysicalMaterial
                            if (texture.value == null) {
                                texture.value = material.map
                            }
                            material.map = texture.value
                            material.color.set(0xc08951)
                            material.needsUpdate = true
                        }
                    )
                }
                Row(modifier = modifier
                    .margin(top = 6.px),
                    verticalAlignment = Alignment.CenterVertically) {
                    SpanText(
                        text = Res.string.color,
                        modifier = modifier
                            .color(Colors.Transparent)
                            .fontSize(if (!breakpoint.isMobileCompatible()) FontSize.Inherit else FontSize.Small)
                            .fontWeight(FontWeight.Normal)
                    )
                    BoxColor(
                        modifier = modifier,
                        paint = WoodPaint(Res.string.wenge,Color("#825b4d")),
                        breakpoint = breakpoint,
                        index = 4,
                        selectedIndex = currentIndex.value,
                        onClick = {
                            if (scene.value == null)
                                return@BoxColor
                            currentIndex.value = it
                            val material = scene.value?.getObjectByName("Box")?.material as MeshPhysicalMaterial
                            if (texture.value == null) {
                                texture.value = material.map
                            }
                            material.map = texture.value
                            material.color.set(0x825b4d)
                            material.needsUpdate = true
                        }
                    )
                    BoxColor(
                        modifier = modifier,
                        paint = WoodPaint(Res.string.oak,Color("#aa7c4f")),
                        breakpoint = breakpoint,
                        index = 5,
                        selectedIndex = currentIndex.value,
                        onClick = {
                            if (scene.value == null)
                                return@BoxColor
                            currentIndex.value = it
                            val material = scene.value?.getObjectByName("Box")?.material as MeshPhysicalMaterial
                            if (texture.value == null) {
                                texture.value = material.map
                            }
                            material.map = texture.value
                            material.color.set(0xaa7c4f)
                            material.needsUpdate = true
                        }
                    )
                    BoxColor(
                        modifier = modifier,
                        paint = WoodPaint(Res.string.light_oak,Color("#c08155")),
                        breakpoint = breakpoint,
                        index = 6,
                        selectedIndex = currentIndex.value,
                        onClick = {
                            if (scene.value == null)
                                return@BoxColor
                            currentIndex.value = it
                            val material = scene.value?.getObjectByName("Box")?.material as MeshPhysicalMaterial
                            if (texture.value == null) {
                                texture.value = material.map
                            }
                            material.map = texture.value
                            material.color.set(0xc08155)
                            material.needsUpdate = true
                        }
                    )
                    BoxColor(
                        modifier = modifier,
                        paint = WoodPaint(Res.string.natural_walnut,Color("#614e40")),
                        breakpoint = breakpoint,
                        index = 7,
                        selectedIndex = currentIndex.value,
                        onClick = {
                            if (scene.value == null)
                                return@BoxColor
                            /*val textureLoader = TextureLoader()
                            val obj = scene.value?.getObjectByName("ImageFront")
                            val material = obj?.material as MeshPhysicalMaterial
                            material.asDynamic().map = textureLoader.load("bookfront2.png",{})
                            material.asDynamic().needsUpdate = true*/
                            currentIndex.value = it
                            val material = scene.value?.getObjectByName("Box")?.material as MeshPhysicalMaterial
                            if (texture.value == null) {
                                texture.value = material.map
                            }
                            material.map = texture.value
                            material.color.set(0x614e40)
                            material.needsUpdate = true
                        }
                    )
                }
                Checkbox(
                    modifier = modifier.margin(top = 6.px),
                    checked = isShowBook.value, onCheckedChange = {
                    isShowBook.value = it
                    if (!it) {
                        val obj = scene.value?.getObjectByName("book")
                        removedBookObj.value = obj
                        removedBookParent.value = obj?.parent
                        obj?.removeFromParent()
                    } else {
                        removedBookObj.value?.let { removedBookParent.value?.add(it) }
                    }
                }) {
                    Text(Res.string.show_book)
                }
            }

            Box(
                modifier = modifier
                    .margin(top = if (!breakpoint.isMobileCompatible()) 110.px else 90.px)
                    .align(Alignment.Center)
            ) {

                Box(
                    modifier = modifier.align(Alignment.Center)
                ) {
                    Div(attrs = {
                        classes(ModelStyles.modelContainer)
                        id("triangle-container")
                        style {
                             // border(1.px, LineStyle.Solid,rgb(200,200,200))
                        }
                    }) {
                        LaunchedEffect(Unit) {
                            val width = if (breakpoint.isMobileCompatible()) 360.0 else 1080.0
                            val height = if (breakpoint.isMobileCompatible()) 220.0 else 720.0
                            scene.value = setupStandGlftModel(
                                containerId = "triangle-container",
                                width = width,
                                height = height,
                                {})
                        }
                    }
                }


            }

            Button(
                onClick = {
                    val variation1 = if (searchText.value.isEmpty()) "4491790444" else "4491790442"
                    val url = Constants.ETSY_TRIANGLE_URL.replace("{variation0}","${Utils.getTriangleVariationNumber(currentIndex.value)}")
                        .replace("{variation1}",variation1)
                    Analytics.logEvent("pageEvent:bookstand:orderButton")
                    window.open(url, target = "_blank")
                },
                modifier = Modifier
                    .id("order-button").backgroundColor(secondaryColor)
                    .padding(topBottom = 12.px, leftRight = 24.px)
                    .margin(topBottom = if (breakpoint.isMobileCompatible()) 32.px else 64.px, leftRight = 32.px)
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
                enabled =  true
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

        }
    }
    if (showTour.value)
    TourGuide(
        steps = tourSteps,
        id = Constants.HAS_SEEN_TOUR_STAND,
        onComplete = {}
    )
}