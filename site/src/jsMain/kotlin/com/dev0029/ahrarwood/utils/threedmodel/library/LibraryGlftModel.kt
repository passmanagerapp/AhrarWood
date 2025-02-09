package com.dev0029.ahrarwood.utils.threedmodel.library

import com.dev0029.ahrarwood.utils.threedmodel.AmbientLight
import com.dev0029.ahrarwood.utils.threedmodel.Box3
import com.dev0029.ahrarwood.utils.threedmodel.DirectionalLight
import com.dev0029.ahrarwood.utils.threedmodel.GLTFLoader
import com.dev0029.ahrarwood.utils.threedmodel.MeshPhysicalMaterial
import com.dev0029.ahrarwood.utils.threedmodel.OrbitControls
import com.dev0029.ahrarwood.utils.threedmodel.PerspectiveCamera
import com.dev0029.ahrarwood.utils.threedmodel.Raycaster
import com.dev0029.ahrarwood.utils.threedmodel.Scene
import com.dev0029.ahrarwood.utils.threedmodel.Vector2
import com.dev0029.ahrarwood.utils.threedmodel.Vector3
import com.dev0029.ahrarwood.utils.threedmodel.WebGLRenderer
import com.dev0029.ahrarwood.utils.threedmodel.WebGLRendererOptions
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.MouseEvent
import kotlin.apply
import kotlin.js.unsafeCast
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

fun setupLibraryGlftModel(
    containerId: String,
    width: Double,
    height: Double,
    onModelClick:(id:String) -> Unit) : Scene {
    val scene = Scene()
    val loadingDiv = document.createElement("div")
    loadingDiv.id = "loading-${containerId}"
    loadingDiv.apply {
        setAttribute("style", """
            position: absolute;
            width: 50%;
            height: 50%;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 20px;
            color: #3498db;
            font-weight: bold;
        """.trimIndent())
    }
    loadingDiv.textContent = "${Res.string.loading} %0"

    val container = document.getElementById(containerId)
    container?.appendChild(loadingDiv)

    val cleanupHandler = {
        document.getElementById(loadingDiv.id)?.let { div ->
            div.parentElement?.removeChild(div)
        }
    }

    window.onbeforeunload = {
        cleanupHandler()
        null
    }

    // Camera setup
    val camera = PerspectiveCamera(75.0, width / height, 0.1, 1000.0)

    camera.position.z = 10

    // Lighting setup
    val ambientLight = AmbientLight(0xffffff, 1.5)
    scene.add(ambientLight)

    val directionalLight = DirectionalLight(0xffffff, 2.0)
    directionalLight.position.set(5, 10, 7.5)
    scene.add(directionalLight)

    val directionalLight2 = DirectionalLight(0xffffff, 1.0)
    directionalLight2.position.set(-5, -5, -5)
    scene.add(directionalLight2)

    val directionalLight3 = DirectionalLight(0xffffff, 1.5)
    directionalLight3.position.set(0, 0, 10)
    scene.add(directionalLight3)


    // Renderer setup
    val rendererOptions = js("{}").unsafeCast<WebGLRendererOptions>().apply {
        antialias = true
        alpha = true
        precision = "highp"
        preserveDrawingBuffer = true
    }
    val renderer = WebGLRenderer(options = rendererOptions)
    renderer.setClearColor(0xffffff,0.0)
    renderer.setSize(width.toInt(), height.toInt())
    renderer.outputEncoding = 3001
    renderer.setPixelRatio(window.devicePixelRatio)
    renderer.toneMapping = 5
    renderer.toneMappingExposure = 1.0
    container?.appendChild(renderer.domElement)

    val controls = OrbitControls(camera, renderer.domElement)

    // Load OBJ file
    val raycaster = Raycaster()
    val mouse = Vector2()

    // Add click listener
    renderer.domElement.onclick = { event : MouseEvent ->
        // Calculate mouse position in normalized device coordinates (-1 to +1)
        mouse.x = event.offsetX / (renderer.domElement.clientWidth as Double) * 2 - 1
        mouse.y = -(event.offsetY.toDouble() / renderer.domElement.clientHeight as Double) * 2 + 1

        // Update the picking ray with the camera and mouse position
        raycaster.setFromCamera(mouse, camera)

        // Calculate objects intersecting the picking ray
        val intersects = raycaster.intersectObjects(scene.children, true)

        if (intersects.isNotEmpty()) {
            val clickedObject = intersects[0].`object`
            if (clickedObject.material.type == "MeshPhysicalMaterial") {
                onModelClick(clickedObject.name)
            }
        }
    }
    val loader = GLTFLoader()
    loader.load(
        "raw/bookshelf.glb",
        { obj ->
            cleanupHandler()
            val box = Box3().setFromObject(obj.scene)
            val center = box.getCenter(Vector3())
            val size = box.getSize(Vector3())

            // Calculate scale to fit in view
            val maxDim = maxOf(size.x, size.y, size.z)
            val scale = 15.0 / maxDim  // Adjust this value to make model larger/smaller

            // Apply transformations to the scene
            obj.scene.asDynamic().position.x = -center.x * scale
            obj.scene.asDynamic().position.y = -center.y * scale
            obj.scene.asDynamic().position.z = -center.z * scale
            obj.scene.asDynamic().scale.set(scale, scale, scale)
            scene.add(obj.scene)
            renderLoop(renderer, scene, camera,controls)
        },
        { progress ->
            var percentage = ((progress.loaded.toDouble() / progress.total.toDouble()) * 100).toInt()
            if (percentage >= 100)
                percentage = 100
            loadingDiv.textContent = "${Res.string.loading} %$percentage"
        },
        { error ->
            loadingDiv.textContent = "${Res.string.not_load} ${error.message}"
        }
    )
    return scene
}

private fun renderLoop(renderer: WebGLRenderer, scene: Scene, camera: PerspectiveCamera,controls: OrbitControls) {
    val initialPosition = Vector3().asDynamic().copy(camera.position)
    var autoRotate = true
    var angle = atan2(camera.position.z, camera.position.x)
    var restartTimeout: Int? = null
    val radius = camera.position.length()
    val prevColor = (scene.getObjectByName("book1")?.material as? MeshPhysicalMaterial)?.color?.getHex()
    fun animate() {
        window.requestAnimationFrame { animate() }
        val currentColor = (scene.getObjectByName("book1")?.material as? MeshPhysicalMaterial)?.color?.getHex()
        val isBook1Changed = prevColor != currentColor
        if (autoRotate && !isBook1Changed) {
            angle += 0.001  // Rotation speed

            // Calculate new camera position
            camera.position.x = radius * cos(angle)
            camera.position.z = radius * sin(angle)

            camera.asDynamic().lookAt(scene.asDynamic().position)
        } else {
            autoRotate = false
        }

        controls.update()
        renderer.render(scene, camera)
    }
    controls.asDynamic().addEventListener("start", {
        autoRotate = false
        controls.asDynamic().addEventListener("change", {
            angle = atan2(camera.position.z, camera.position.x)
        })
        restartTimeout?.let { window.clearTimeout(it) }
    })
    controls.asDynamic().addEventListener("end", {
        restartTimeout = window.setTimeout({
            autoRotate = true
        }, 5000)
    })
    animate()
}