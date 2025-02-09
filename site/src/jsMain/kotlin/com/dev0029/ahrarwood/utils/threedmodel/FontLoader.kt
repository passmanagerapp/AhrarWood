@file:JsModule("three/examples/jsm/loaders/FontLoader")
@file:JsNonModule

package com.dev0029.ahrarwood.utils.threedmodel

import org.w3c.dom.ErrorEvent
import org.w3c.xhr.ProgressEvent

external class FontLoader {
    fun load(
        url: String,
        onLoad: (font: Font) -> Unit,
        onProgress: ((event: ProgressEvent) -> Unit)? = definedExternally,
        onError: ((event: ErrorEvent) -> Unit)? = definedExternally
    )
}

external interface Font {
    val data: dynamic
}
