package com.dev0029.ahrarwood

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.dev0029.ahrarwood.network.firebase.FirebaseConfig
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.style.common.SmoothColorStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.loadFromLocalStorage
import com.varabyte.kobweb.silk.theme.colors.saveToLocalStorage
import com.varabyte.kobweb.silk.theme.colors.systemPreference
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.analytics.analytics
import dev.gitlive.firebase.initialize
import dev.gitlive.firebase.installations.installations
import io.github.skeptick.libres.LibresSettings
import kotlinx.browser.localStorage
import kotlinx.browser.window
import org.jetbrains.compose.web.css.vh

private const val COLOR_MODE_KEY = "ahrarwood:colorMode"

@InitSilk
fun initColorMode(ctx: InitSilkContext) {
    ctx.config.initialColorMode = ColorMode.loadFromLocalStorage(COLOR_MODE_KEY) ?: ColorMode.systemPreference
}

@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    LaunchedEffect(Unit) {
        initFirebase()
    }
    initLocalization()
    SilkApp {
        val colorMode = ColorMode.current
        LaunchedEffect(colorMode) {
            colorMode.saveToLocalStorage(COLOR_MODE_KEY)
        }

        Surface(
            SmoothColorStyle.toModifier()
                .minHeight(100.vh)
                .scrollBehavior(ScrollBehavior.Smooth)
        ) {
            content()
        }
    }
}

private fun initFirebase() {
    val app = dev.gitlive.firebase.Firebase.initialize(
        options = FirebaseOptions(
            applicationId = FirebaseConfig.APP_ID,
            apiKey = FirebaseConfig.API_KEY,
            storageBucket = FirebaseConfig.STORAGE_BUCKET,
            projectId = FirebaseConfig.PROJECT_ID,
            gcmSenderId = FirebaseConfig.MESSAGING_SENDER_ID,
            authDomain = FirebaseConfig.AUTH_DOMAIN
        )
    )
    val fb=  dev.gitlive.firebase.Firebase
    fb.installations(app)
    fb.analytics.setAnalyticsCollectionEnabled(true)
}

private fun initLocalization() {
    val locales = listOf("en", "tr","de")
    LibresSettings.languageCode =
        localStorage.getItem("locale")
            ?: locales.find { it == window.navigator.language.substringBefore("-") }
                    ?: locales.first()
}
