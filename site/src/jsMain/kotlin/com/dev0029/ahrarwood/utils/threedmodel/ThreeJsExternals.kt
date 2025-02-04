@file:JsModule("three")
@file:JsNonModule

package com.dev0029.ahrarwood.utils.threedmodel

external class Scene {
    val children: Array<Object3D>
    fun add(obj: Any)
    fun remove(obj: Object3D)
    fun getObjectByName(name: String): Object3D?
}

external class PerspectiveCamera(
    fov: Double,
    aspect: Double,
    near: Double,
    far: Double
) {
    var position: dynamic
}

external class WebGLRenderer(options: WebGLRendererOptions = definedExternally) {
    fun setSize(width: Int, height: Int)
    val domElement: dynamic
    var physicallyCorrectLights: Boolean
    fun setPixelRatio(ratio: Double)
    var outputEncoding: Int
    var toneMapping: Int
    var toneMappingExposure: Double
    fun setClearColor(color: Int, alpha: Double = definedExternally)
    fun render(scene: Scene, camera: PerspectiveCamera)
}

external class AmbientLight(color: Int, intensity: Double = definedExternally)

external class DirectionalLight(color: Int, intensity: Double = definedExternally) {
    var position: dynamic
}

open external class Object3D {
    val isObject3D: Boolean
    val scale: Vector3
    val position: Vector3
    var name: String
    var material: Material
    val children: Array<Object3D>
    fun traverse(callback: (child: Any) -> Unit)
}

external interface Material {
    val type: String
    val name: String
}

external class MeshPhongMaterial {
    var color: dynamic
    var map: dynamic  // To store the texture
}

external class TextureLoader {
    fun load(url: String, onLoad: (dynamic) -> Unit, onProgress: ((dynamic) -> Unit)? = definedExternally, onError: ((dynamic) -> Unit)? = definedExternally)
}

external class Raycaster {
    fun setFromCamera(coords: Vector2, camera: PerspectiveCamera)
    fun intersectObjects(objects: Array<dynamic>, recursive: Boolean = definedExternally): Array<Intersection>
}

external class Vector2 {
    var x: Double
    var y: Double
}

external interface Intersection {
    val distance: Double
    val point: Vector3
    val `object`: Object3D
}

external class Vector3 {
    var x: Double
    var y: Double
    var z: Double
    fun multiply(v: Vector3): Vector3
    fun set(x: Double, y: Double, z: Double): Vector3
}


external interface MaterialParams {
    var color: Int
    var wireframe: Boolean
    var side: Int
}

external class Mesh(
    geometry: BufferGeometry,
    material2: Material
) : Object3D {
    var geometry: BufferGeometry
    var material2: Material
}

external class BufferGeometry : EventDispatcher {
    var attributes: dynamic
    var index: BufferAttribute?

    fun setAttribute(name: String, attribute: BufferAttribute): BufferGeometry
    fun setIndex(index: BufferAttribute): BufferGeometry
    fun computeVertexNormals()
    fun computeBoundingBox()
    fun computeBoundingSphere()
}
open external class BufferAttribute(
    array: dynamic,
    itemSize: Int,
    normalized: Boolean = definedExternally
) {
    var count: Int
    var itemSize: Int
    fun setXYZ(index: Int, x: Double, y: Double, z: Double): BufferAttribute
    fun setXY(index: Int, x: Double, y: Double): BufferAttribute
}

open external class EventDispatcher {
    fun addEventListener(type: String, listener: (event: dynamic) -> Unit)
    fun hasEventListener(type: String, listener: (event: dynamic) -> Unit): Boolean
    fun removeEventListener(type: String, listener: (event: dynamic) -> Unit)
    fun dispatchEvent(event: dynamic)
}

external class MeshStandardMaterial(params: dynamic = definedExternally,
                                    override val type: String,
                                    override val name: String
) : Material {
    var color: Color
    var metalness: Double
    var roughness: Double
}

open external class Color {
    fun set(color: Color): Color
    fun set(color: Int): Color
    fun set(color: String): Color
    fun set(r: Double, g: Double, b: Double): Color
    fun getHex(): Int
}

external interface WebGLRendererOptions {
    var antialias: Boolean
    var alpha: Boolean
    var precision: String
    var preserveDrawingBuffer: Boolean
}

external class Box3 {
    fun setFromObject(obj: dynamic): Box3
    fun getCenter(target: Vector3): Vector3
    fun getSize(target: Vector3): Vector3
}

external interface MeshPhysicalMaterial : Material {
    var color: Color
}
