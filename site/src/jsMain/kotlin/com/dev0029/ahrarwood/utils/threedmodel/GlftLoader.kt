@file:JsModule("three/examples/jsm/loaders/GLTFLoader")
@file:JsNonModule

package com.dev0029.ahrarwood.utils.threedmodel

import org.w3c.xhr.ProgressEvent

external class GLTFLoader {
    fun load(
        url: String,
        onLoad: (gltf: GLTF) -> Unit,
        onProgress: ((event: ProgressEvent) -> Unit)? = definedExternally,
        onError: ((dynamic) -> Unit)? = definedExternally
    )
}

external interface GLTF {
    val scenes: Array<Scene>
    val scene: Scene
    val assets: dynamic
}
