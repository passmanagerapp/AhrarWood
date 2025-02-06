@file:JsModule("three/examples/jsm/loaders/GLTFLoader")
@file:JsNonModule

package com.dev0029.ahrarwood.utils.threedmodel

import org.w3c.dom.ErrorEvent
import org.w3c.xhr.ProgressEvent

external class GLTFLoader {
    fun load(
        url: String,
        onLoad: (gltf: GLTF) -> Unit,
        onProgress: ((event: ProgressEvent) -> Unit)? = definedExternally,
        onError: ((event: ErrorEvent) -> Unit)? = definedExternally
    )
}

external interface GLTF {
    val scenes: Array<Scene>
    val scene: Scene
    val assets: dynamic
}

external interface ErrorEvent {
    val message: String
    val filename: String?
    val lineno: Int?
    val colno: Int?
    val error: dynamic
}
