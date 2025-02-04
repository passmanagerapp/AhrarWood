@file:JsModule("jspdf")
@file:JsNonModule

package com.dev0029.ahrarwood.js

external class jsPDF {

    fun text(text: String, x: Number, y: Number)
    fun addPage()
    fun save(filename: String)
    fun setFontSize(size: Number)
    fun output(type: String): dynamic
    fun addImage(imageData: String, imageFormat: String, x: Number, y: Number, width: Number, height: Number, alias: String?, compression: String?)
    fun setFont(fontName: String, fontStyle: String)
    fun addFileToVFS(fileName: String, fileData: String)
    fun addFont(fileName: String, fontName: String, fontStyle: String)
}