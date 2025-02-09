package com.dev0029.ahrarwood.utils

import com.dev0029.ahrarwood.extensions.ignoreNull
import com.dev0029.ahrarwood.js.jsPDF
import com.dev0029.ahrarwood.models.BookListModel
import com.dev0029.ahrarwood.styles.fontUnicode
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.FirebaseStorageMetadata
import dev.gitlive.firebase.storage.storage
import kotlinx.browser.window
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.w3c.files.Blob
import org.w3c.files.File
import org.w3c.files.FilePropertyBag

object Utils {


    fun isBrowserLanguageEn() : Boolean {
        return window.navigator.language.substringBefore("-") == "en"
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun createBookListPdf(books: List<BookListModel>,id: String,download: Boolean) {
        val pdf = jsPDF()
        val base64Font = fontUnicode
        pdf.addFileToVFS("Roboto-Regular-normal.ttf", base64Font);
        pdf.addFont("Roboto-Regular-normal.ttf", "Roboto-Regular", "normal");
        pdf.setFont("Roboto-Regular", "normal")
        runCatching {
            val imageUrl = "https://ahrarahsap.com/banner_logo.png"
            pdf.addImage(imageUrl, "PNG", 10.0, 10.0, 50.0, 20.0,null,null)
        }

        pdf.setFontSize(12)

        var yPosition = 50
        val margin = 20
        val lineHeight = 10

        books.forEachIndexed { index, book ->
            if (yPosition > 270) {
                pdf.addPage()
                yPosition = 20
            }
            pdf.text("${index+1}. ${book.title},${book.author},${book.publisher.ignoreNull()},${book.publishYear.ignoreNull()}", margin, yPosition)
            yPosition += lineHeight
        }
        if (download)
            pdf.save("booklist_$id.pdf")
        else {
            val blob = pdf.output("blob") as Blob
            MainScope().launch {
                try {
                    uploadPdfToStorage(blob,id)
                } catch (e: Exception) {
                    console.log("ex: ${e.message}")
                }
            }
        }
    }

    suspend fun uploadPdfToStorage(blob: Blob,id: String) {
        val fb =Firebase.storage
        val file = blobToFile(blob,"$id.pdf")
        fb.reference.child("$id.pdf").putFile(file, metadata = FirebaseStorageMetadata(
            contentType = "application/pdf"
        ))
    }

    fun blobToFile(blob: Blob, fileName: String): File {
        return File(arrayOf(blob), fileName, FilePropertyBag(type = "application/pdf"))
    }

    fun splitSentenceInHalf(sentence: String): String {
        val words = sentence.split(" ")
        if (words.size < 4) return sentence

        val midIndex = words.size / 2
        val firstPart = words.take(midIndex).joinToString(" ")
        val secondPart = words.drop(midIndex).joinToString(" ")

        return "$firstPart\n$secondPart"
    }

}