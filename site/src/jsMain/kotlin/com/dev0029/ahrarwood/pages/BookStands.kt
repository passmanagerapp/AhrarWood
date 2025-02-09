package com.dev0029.ahrarwood.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.dev0029.ahrarwood.components.layouts.SearchView
import com.dev0029.ahrarwood.components.sections.home.HomeHeader
import com.dev0029.ahrarwood.components.widgets.TwoWeightText
import com.dev0029.ahrarwood.constants.PageRoutes
import com.dev0029.ahrarwood.extensions.isMobileCompatible
import com.dev0029.ahrarwood.extensions.primaryColor
import com.dev0029.ahrarwood.utils.threedmodel.MeshPhysicalMaterial
import com.dev0029.ahrarwood.utils.threedmodel.Scene
import com.dev0029.ahrarwood.utils.threedmodel.stand.setupStandGlftModel
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import com.dev0029.ahrarwood.components.widgets.BoxColor
import com.dev0029.ahrarwood.models.WoodPaint
import com.dev0029.ahrarwood.utils.Utils
import com.dev0029.ahrarwood.utils.threedmodel.FontLoader
import com.dev0029.ahrarwood.utils.threedmodel.Mesh
import com.dev0029.ahrarwood.utils.threedmodel.MeshStandardMaterial
import com.dev0029.ahrarwood.utils.threedmodel.TextGeometry
import com.dev0029.ahrarwood.utils.threedmodel.TextGeometryParameters
import com.dev0029.ahrarwood.utils.threedmodel.Texture
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.ui.modifiers.bottom
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.left
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.percent

@Page
@Composable
fun BookStands(
    modifier: Modifier = Modifier
) {
    val breakpoint = rememberBreakpoint()
    var scene = remember { mutableStateOf<Scene?>(null) }
    var texture = remember { mutableStateOf<Texture?>(null) }
    var woodColor = remember { mutableStateOf<com.dev0029.ahrarwood.utils.threedmodel.Color?>(null) }
    val currentIndex = remember { mutableStateOf(0) }
    var searchText = remember { mutableStateOf("") }
    var isSearchExpanded = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        HomeHeader(PageRoutes.BOOK_STANDS, modifier)
        Box(
            modifier = modifier
                .fillMaxSize()
                .margin(top = 102.px),
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
                SpanText("${Res.string.book_stands_header_desc} -> Triangle book stand",
                    modifier = modifier
                        .fontSize(if (!breakpoint.isMobileCompatible()) 16.px else 12.px)
                        .fontWeight(FontWeight.Thin).color(Colors.White)
                        .align(if (!breakpoint.isMobileCompatible()) Alignment.BottomStart else Alignment.TopStart)
                        .margin(bottom = 8.px, top = if (!breakpoint.isMobileCompatible()) 0.px else 20.px))
                Column(modifier = modifier.fillMaxWidth()
                    .align(if (!breakpoint.isMobileCompatible()) Alignment.CenterEnd else Alignment.Center),
                    horizontalAlignment = Alignment.End) {
                    SearchView(
                        modifier = modifier,
                        breakpoint = breakpoint,
                        scope = scope,
                        isSearchExpanded = isSearchExpanded.value,
                        searchText = searchText.value,
                        onSearchTextChange = {searchText.value =it},
                        onSearchExpanded = { isSearchExpanded.value = !isSearchExpanded.value},
                        onSubmit = { search ->
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
                                textMesh.position.set(-0.6,0.0,-1.9)
                               // textMesh.position.x = -textWidth / 2
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
                modifier = modifier.align(Alignment.CenterStart)
                    .margin(left = 24.px, top = 24.px)
            ) {
                TwoWeightText(modifier,Res.string.material,Res.string.beech,showIcon = false)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                ) {
                    SpanText(
                        text = Res.string.color,
                        modifier = modifier
                            .fontWeight(FontWeight.Normal)
                    )
                    BoxColor(
                        modifier = modifier,
                        paint = WoodPaint(Res.string.natural,Color("#D7B899")),
                        index = 0,
                        selectedIndex = currentIndex.value,
                        onClick = {
                            if (scene.value == null)
                                return@BoxColor
                            currentIndex.value = it
                            val material = scene.value?.getObjectByName("BookShelf")?.material as MeshPhysicalMaterial
                            if (texture.value == null) {
                                texture.value = material.map
                                woodColor.value = material.color
                            }
                            material.map = texture.value
                            material.color = com.dev0029.ahrarwood.utils.threedmodel.Color()
                            material.needsUpdate = true
                        }
                    )
                    BoxColor(
                        modifier = modifier,
                        paint = WoodPaint(Res.string.white,Color("#ffffff")),
                        index = 1,
                        selectedIndex = currentIndex.value,
                        onClick = {
                            if (scene.value == null)
                                return@BoxColor
                            currentIndex.value = it
                            val material = scene.value?.getObjectByName("BookShelf")?.material as MeshPhysicalMaterial
                            if (texture.value == null) {
                                texture.value = material.map
                                woodColor.value = material.color
                            }
                            material.map = null
                            material.color.set(0xffffff)
                            material.needsUpdate = true
                        }
                    )
                    BoxColor(
                        modifier = modifier,
                        paint = WoodPaint(Res.string.matte_black,Colors.Black),
                        index = 2,
                        selectedIndex = currentIndex.value,
                        onClick = {
                            if (scene.value == null)
                                return@BoxColor
                            currentIndex.value = it
                            val material = scene.value?.getObjectByName("BookShelf")?.material as MeshPhysicalMaterial
                            if (texture.value == null) {
                                println("woodColor updating..2")
                                texture.value = material.map
                                woodColor.value = material.color
                            }
                            material.map = null
                            material.color.set(0x000000)
                            material.needsUpdate = true
                        }
                    )
                    BoxColor(
                        modifier = modifier,
                        paint = WoodPaint(Res.string.honey,Color("#C5AE7A")),
                        index = 3,
                        selectedIndex = currentIndex.value,
                        onClick = {
                            if (scene.value == null)
                                return@BoxColor
                            currentIndex.value = it
                            val material = scene.value?.getObjectByName("BookShelf")?.material as MeshPhysicalMaterial
                            if (texture.value == null) {
                                println("woodColor updating..3")
                                texture.value = material.map
                                woodColor.value = material.color
                            }
                            material.map = null
                            material.color.set(0xC5AE7A)
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
                            .fontWeight(FontWeight.Normal)
                    )
                    BoxColor(
                        modifier = modifier,
                        paint = WoodPaint(Res.string.wenge,Color("#4A3B31")),
                        index = 4,
                        selectedIndex = currentIndex.value,
                        onClick = {
                            if (scene.value == null)
                                return@BoxColor
                            currentIndex.value = it
                            val material = scene.value?.getObjectByName("BookShelf")?.material as MeshPhysicalMaterial
                            if (texture.value == null) {
                                println("woodColor updating..4")
                                texture.value = material.map
                                woodColor.value = material.color
                            }
                            material.map = null
                            material.color.set(0x4A3B31)
                            material.needsUpdate = true
                        }
                    )
                    BoxColor(
                        modifier = modifier,
                        paint = WoodPaint(Res.string.oak,Color("#8F7C58")),
                        index = 5,
                        selectedIndex = currentIndex.value,
                        onClick = {
                            if (scene.value == null)
                                return@BoxColor
                            currentIndex.value = it
                            val material = scene.value?.getObjectByName("BookShelf")?.material as MeshPhysicalMaterial
                            if (texture.value == null) {
                                println("woodColor updating..5")
                                texture.value = material.map
                                woodColor.value = material.color
                            }
                            material.map = null
                            material.color.set(0x8F7C58)
                            material.needsUpdate = true
                        }
                    )
                    BoxColor(
                        modifier = modifier,
                        paint = WoodPaint(Res.string.light_oak,Color("#C4A67A")),
                        index = 6,
                        selectedIndex = currentIndex.value,
                        onClick = {
                            if (scene.value == null)
                                return@BoxColor
                            currentIndex.value = it
                            val material = scene.value?.getObjectByName("BookShelf")?.material as MeshPhysicalMaterial
                            if (texture.value == null) {
                                println("woodColor updating..6")
                                texture.value = material.map
                                woodColor.value = material.color
                            }
                            material.map = null
                            material.color.set(0xC4A67A)
                            material.needsUpdate = true
                        }
                    )
                    BoxColor(
                        modifier = modifier,
                        paint = WoodPaint(Res.string.natural_walnut,Color("#47290F")),
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
                            val material = scene.value?.getObjectByName("BookShelf")?.material as MeshPhysicalMaterial
                            if (texture.value == null) {
                                println("woodColor updating..7")
                                texture.value = material.map
                                woodColor.value = material.color
                            }
                            material.map = null
                            material.color.set(0x47290F)
                            material.needsUpdate = true
                        }
                    )
                }
            }

            Box(
                modifier = modifier
                    .align(Alignment.Center)
            ) {

                Box(
                    modifier = modifier.align(Alignment.Center)
                ) {
                    Div(attrs = {
                        classes(ModelStyles.modelContainer)
                        id("triangle-container")
                    }) {
                        LaunchedEffect(Unit) {
                            scene.value = setupStandGlftModel(
                                containerId = "triangle-container",
                                width = 1080.0,
                                height = 720.0,
                                {})
                        }
                    }
                }


            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .position(Position.Absolute)
                    .bottom(if (breakpoint >= Breakpoint.MD) 16.px else 8.px)
                    .left(if (breakpoint >= Breakpoint.MD) 24.px else 12.px)
                    .gap(if (breakpoint >= Breakpoint.MD) 12.px else 8.px)
            ) {
                repeat(2) { index ->
                    Div(
                        attrs = Modifier
                            .size(if (breakpoint >= Breakpoint.MD) 12.px else 8.px)
                            .backgroundColor(if (index == 0) Colors.White else Colors.LightGray)
                            .borderRadius(50.percent)
                            .cursor(Cursor.Pointer)
                            .toAttrs({
                                onClick {

                                }
                            })
                    )
                }
            }

        }
    }
}