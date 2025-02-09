@file:JsModule("three/examples/jsm/geometries/TextGeometry")
@file:JsNonModule

package com.dev0029.ahrarwood.utils.threedmodel


external class TextGeometry(text: String, parameters: TextGeometryParameters) : BufferGeometry

external interface TextGeometryParameters {
    var font: Font
    var size: Double
    var depth: Double
    var bevelEnabled: Boolean
    var bevelThickness: Double
    var bevelSize: Double
}
