package com.dev0029.ahrarwood.components.sections

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.*
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Text

data class TourStep(
    val target: String,
    val title: String,
    val description: String,
    val position: Position = Position.Bottom
)

enum class Position {
    Top, Bottom, Left, Right
}

@Composable
fun TourGuide(
    steps: List<TourStep>,
    id: String,
    onComplete: () -> Unit
) {
    var currentStep by remember { mutableStateOf(0) }
    var isVisible by remember { mutableStateOf(true) }

    val hasSeenTour = localStorage.getItem(id) == "true"
    isVisible = !hasSeenTour

    if (!isVisible) return

    Box(
        modifier = Modifier.fillMaxSize().styleModifier {
            property("position", "fixed")
            property("top", "0")
            property("left", "0")
            property("z-index", "1000")
            property("background-color", "rgba(0, 0, 0, 0.5)")
        }
    ) {
        steps.getOrNull(currentStep)?.let { step ->
            // Get target element position
            val target = document.getElementById(step.target)
            val rect = target?.getBoundingClientRect()
            val leftPadding = if (step.target == "order-button") 120 else 0
            // Position tooltip near target
            Box(
                modifier = Modifier
                    .position(org.jetbrains.compose.web.css.Position.Absolute)
                    .styleModifier {
                        rect?.let {
                            when(step.position) {
                                Position.Bottom -> {
                                    property("top", "${it.bottom + 10}px")
                                    property("left", "${it.left}px")
                                }
                                Position.Top -> {
                                    property("bottom", "${window.innerHeight - it.top + 10}px")
                                    property("left", "${it.left - leftPadding}px")
                                }
                                else -> Unit
                            }
                        }
                    }
                    .backgroundColor(Colors.White)
                    .padding(16.px)
                    .borderRadius(8.px)
                    .boxShadow(0.px, 4.px, 8.px,null, rgba(0,0,0,0.1))
            ) {
                Column(
                    modifier = Modifier.width(250.px).gap(8.px)
                ) {
                    SpanText(
                        text = step.title,
                        modifier = Modifier.fontSize(18.px).fontWeight(FontWeight.Bold)
                    )
                    SpanText(text = step.description)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SpanText("${currentStep + 1}/${steps.size}")
                        Button(
                            onClick = {
                                if (currentStep < steps.size - 1) {
                                    currentStep++
                                } else {
                                    localStorage.setItem(id, "true")
                                    isVisible = false
                                    onComplete()
                                }
                            },
                            modifier = Modifier.cursor(Cursor.Pointer)
                        ) {
                            Text(if (currentStep < steps.size - 1) "Next" else "Finish")
                        }
                    }
                }
            }

            // Highlight target element
            target?.let {
                it.setAttribute("position","relative")
                it.setAttribute("zIndex","1001")
                it.setAttribute("boxShadow","0 0 0 4px rgba(52, 152, 219, 0.5)")
            }
        }
    }
}