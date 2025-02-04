package com.dev0029.ahrarwood.helpers

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.silk.theme.colors.ColorMode

object AppHelper {

    @Composable
    fun isDarkMode() : Boolean = ColorMode.current.isDark
}