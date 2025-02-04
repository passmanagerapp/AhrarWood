package com.dev0029.ahrarwood.models

data class BookListModel(
    val id: String = "",
    val isbn: String = "",
    val title: String = "",
    val author: String = "",
    val publisher: String? = null,
    val publishYear : Int?=null
)
