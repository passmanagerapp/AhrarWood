package com.dev0029.ahrarwood.base

import androidx.compose.runtime.*
import com.dev0029.ahrarwood.models.BookListModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SharedViewModel {
    private val _bookItems = MutableStateFlow< Pair<String,List<BookListModel>>>(Pair("",emptyList()))
    val bookItems: StateFlow<Pair<String,List<BookListModel>>> = _bookItems

    fun setItems(newItems: Pair<String,List<BookListModel>>) {
        _bookItems.value = newItems
    }
}
