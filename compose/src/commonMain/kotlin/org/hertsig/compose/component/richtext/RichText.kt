package org.hertsig.compose.component.richtext

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.*
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.hertsig.compose.build
import org.hertsig.compose.pointerInputAsync

@Composable
fun RichText(
    content: RichString,
    modifier: Modifier = Modifier,
    clickableHoverColor: Color = Color.LightGray.copy(alpha = .25f),
) {
    if (content.clickables.isEmpty() && content.tooltips.isEmpty()) {
        // If we have neither clickables nor tooltips, we can just defer to Compose's AnnotatedString rendering
        Text(content.annotatedString, modifier, inlineContent = content.inlineContent)
    } else {
        var text by remember(content) { mutableStateOf(content.annotatedString) }
        fun updateText(hovered: IntRange) {
            text = if (hovered.isEmpty()) {
                content.annotatedString
            } else {
                content.annotatedString.copyWith {
                    addStyle(SpanStyle(background = clickableHoverColor), hovered.first, hovered.last)
                }
            }
        }

        var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
        fun getAnnotationFromOffset(tag: String, positionOffset: Offset) = layoutResult
            ?.multiParagraph
            ?.takeIf { it.containsWithinBounds(positionOffset) }
            ?.getOffsetForPosition(positionOffset)
            ?.let { text.getStringAnnotations(tag, it, it).singleOrNull() }

        val clickablesModifier = if (content.clickables.isEmpty()) Modifier
            else Modifier.clickablesPointerInput(content, ::getAnnotationFromOffset, ::updateText)
        val tooltipsModifier = if (content.tooltips.isEmpty()) Modifier
            else Modifier.tooltipsPointerInput(content, ::getAnnotationFromOffset)

        Text(
            text,
            modifier.then(clickablesModifier).then(tooltipsModifier),
            inlineContent = content.inlineContent,
            onTextLayout = { layoutResult = it }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Modifier.clickablesPointerInput(
    content: RichString,
    getAnnotationFromCursorPosition: (String, Offset) -> AnnotatedString.Range<String>?,
    onHoverClickable: (IntRange) -> Unit,
) = pointerInputAsync(content) {
    detectTapGestures { pos ->
        val annotation = getAnnotationFromCursorPosition(CLICKABLE_TAG, pos)
        content.clickables[annotation?.item]?.invoke()
    }
}.onPointerEvent(PointerEventType.Move) {
    val annotation = getAnnotationFromCursorPosition(CLICKABLE_TAG, it.position)
    val hoveredRange = if (annotation == null) IntRange.EMPTY else IntRange(annotation.start, annotation.end)
    onHoverClickable(hoveredRange)
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun Modifier.tooltipsPointerInput(
    content: RichString,
    getAnnotationFromCursorPosition: (String, Offset) -> AnnotatedString.Range<String>?,
    tooltipState: TooltipState = rememberTooltipState(content),
): Modifier {
    fun onHover(offset: Offset) {
        val annotation = getAnnotationFromCursorPosition(TOOLTIP_TAG, offset)
        val tooltip = content.tooltips[annotation?.item]
        tooltipState.onHover(tooltip)
    }

    tooltipState.active?.let { tooltip ->
        Popup(tooltip.positionProvider, onDismissRequest = { tooltipState.clear() }) {
            Box(tooltip.modifier
                // Not needed, but if popups get stuck you can remove them by pointing at them
                .onPointerEvent(PointerEventType.Enter) { tooltipState.clear() }
            ) { tooltip.content() }
        }
    }

    return onPointerEvent(PointerEventType.Move) { onHover(it.position) }
        .onPointerEvent(PointerEventType.Exit) { tooltipState.clear() }
}

@Composable
private fun rememberTooltipState(content: RichString): TooltipState {
    val scope = rememberCoroutineScope()
    return remember(content) { TooltipState(scope) }
}

private data class TooltipState(private val scope: CoroutineScope) {
    var active: Tooltip? by mutableStateOf(null); private set
    private var currentHover: Tooltip? = null
    private var job: Job? = null

    fun clear() = onHover(null)
    fun onHover(tooltip: Tooltip?) {
        if (currentHover == tooltip) return
        job?.cancel()
        active = null
        currentHover = tooltip
        if (tooltip != null) {
            job = scope.launch { delay(tooltip.delay); active = tooltip }
        }
    }
}

private fun MultiParagraph.containsWithinBounds(positionOffset: Offset): Boolean =
    positionOffset.let { (x, y) -> x > 0 && y >= 0 && x <= width && y <= height }

private fun AnnotatedString.copyWith(additions: AnnotatedString.Builder.() -> Unit) = copy().also(additions).build()

@OptIn(ExperimentalTextApi::class)
private fun AnnotatedString.copy(): AnnotatedString.Builder {
    val new = AnnotatedString.Builder()
    new.append(text)
    spanStyles.forEach { new.addStyle(it.item, it.start, it.end) }
    paragraphStyles.forEach { new.addStyle(it.item, it.start, it.end) }
    getStringAnnotations(0, length).forEach { new.addStringAnnotation(it.tag, it.item, it.start, it.end) }
    getTtsAnnotations(0, length).forEach { new.addTtsAnnotation(it.item, it.start, it.end) }
    getUrlAnnotations(0, length).forEach { new.addUrlAnnotation(it.item, it.start, it.end) }
    return new
}

private val PointerEvent.position get() = changes.first().position
