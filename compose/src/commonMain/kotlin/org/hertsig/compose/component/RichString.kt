package org.hertsig.compose.component

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.hertsig.compose.build
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

val LINK_STYLE = SpanStyle(Color.Blue, textDecoration = TextDecoration.Underline)

open class RichString internal constructor(
    val annotatedString: AnnotatedString,
    val inlineContent: Map<String, InlineTextContent>,
    val clickables: Map<String, () -> Unit>,
    val tooltips: Map<String, Tooltip>,
)

data class Tooltip(
    val modifier: Modifier = Modifier,
    val delay: Duration = 500.milliseconds,
    @OptIn(ExperimentalFoundationApi::class)
    val placement: TooltipPlacement = TooltipPlacement.CursorPoint(offset = DpOffset(0.dp, 16.dp)),
    val content: Content,
)

@Composable
fun RichText(content: RichString, modifier: Modifier = Modifier) {
    if (content.clickables.isEmpty() && content.tooltips.isEmpty()) {
        // If we have neither clickables nor tooltips, we can just defer to Compose's AnnotatedString rendering
        Text(content.annotatedString, modifier, inlineContent = content.inlineContent)
    } else {
        var text by remember { mutableStateOf(content.annotatedString) }
        fun updateText(hovered: IntRange) {
            text = if (hovered.isEmpty()) {
                content.annotatedString
            } else {
                text.copyWith {
                    addStyle(SpanStyle(background = Color.LightGray.copy(alpha = .2f)), hovered.first, hovered.last)
                }
            }
        }

        var hoveredTooltip by remember { mutableStateOf<Tooltip?>(null) }
        hoveredTooltip?.let { RichTextTooltip(it) }

        var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
        fun getOffset(positionOffset: Offset): Int? = layoutResult
            ?.multiParagraph
            ?.takeIf { it.containsWithinBounds(positionOffset) }
            ?.getOffsetForPosition(positionOffset)

        Text(
            text,
            modifier.pointerInput(content, ::getOffset, ::updateText, { hoveredTooltip = it }),
            inlineContent = content.inlineContent,
            onTextLayout = { layoutResult = it }
        )
    }
}

@Composable
private fun Modifier.pointerInput(
    content: RichString,
    getCharacterIndexFromOffset: (Offset) -> Int?,
    onHoverClickable: (IntRange) -> Unit,
    onHoverTooltip: (Tooltip?) -> Unit,
    text: AnnotatedString = content.annotatedString,
): Modifier {
    fun withAnnotation(offset: Offset, tag: String, action: (AnnotatedString.Range<String>?) -> Unit) {
        val position = getCharacterIndexFromOffset(offset)
        val annotation = position?.let { text.getStringAnnotations(tag, it, it).singleOrNull() }
        action(annotation)
    }

    return pointerInput(content) {
        coroutineScope {
            if (content.clickables.isNotEmpty()) {
                launch {
                    detectTapGestures { pos ->
                        withAnnotation(pos, CLICKABLE_TAG) { content.clickables[it?.item]?.invoke() }
                    }
                }
            }
            if (content.clickables.isNotEmpty() || content.tooltips.isEmpty()) {
                launch {
                    awaitPointerEventScope {
                        while (true) {
                            val position = awaitPointerEvent().position
                            if (content.clickables.isNotEmpty()) {
                                withAnnotation(position, CLICKABLE_TAG) {
                                    onHoverClickable(if (it == null) IntRange.EMPTY else IntRange(it.start, it.end))
                                }
                            }
                            if (content.tooltips.isNotEmpty()) {
                                withAnnotation(position, TOOLTIP_TAG) {
                                    onHoverTooltip(content.tooltips[it?.item])
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RichTextTooltip(tooltip: Tooltip) {
    fun Modifier.onPointerEvent(
        eventType: PointerEventType,
        pass: PointerEventPass = PointerEventPass.Main,
        onEvent: AwaitPointerEventScope.(event: PointerEvent) -> Unit,
    ) = pointerInput(eventType, pass, onEvent) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent(pass)
                if (event.type == eventType) {
                    onEvent(event)
                }
            }
        }
    }

    suspend fun PointerInputScope.detectDown(onDown: (Offset) -> Unit) {
        while (true) {
            awaitPointerEventScope {
                val event = awaitPointerEvent(PointerEventPass.Initial)
                val down = event.changes.find { it.changedToDown() }
                if (down != null) {
                    onDown(down.position)
                }
            }
        }
    }

    var parentBounds by remember { mutableStateOf(Rect.Zero) }
    var popupPosition by remember { mutableStateOf(Offset.Zero) }
    var cursorPosition by remember { mutableStateOf(Offset.Zero) }
    var isVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var job: Job? by remember { mutableStateOf(null) }

    fun startShowing() {
        job?.cancel()
        job = scope.launch {
            delay(tooltip.delay)
            isVisible = true
        }
    }

    fun hide() {
        job?.cancel()
        isVisible = false
    }

    fun hideIfNotHovered(globalPosition: Offset) {
        if (!parentBounds.contains(globalPosition)) {
            hide()
        }
    }

    Box(
        modifier = tooltip.modifier
            .onGloballyPositioned { parentBounds = it.boundsInWindow() }
            .onPointerEvent(PointerEventType.Enter) {
                cursorPosition = it.position
                startShowing()
            }
            .onPointerEvent(PointerEventType.Move) {
                cursorPosition = it.position
                hideIfNotHovered(parentBounds.topLeft + it.position)
            }
            .onPointerEvent(PointerEventType.Exit) {
                hideIfNotHovered(parentBounds.topLeft + it.position)
            }
            .pointerInput(Unit) {
                detectDown {
                    hide()
                }
            }
    ) {
        if (isVisible) {
            @OptIn(ExperimentalFoundationApi::class)
            (Popup(
                popupPositionProvider = tooltip.placement.positionProvider(cursorPosition),
                onDismissRequest = { isVisible = false }
            ) {
                Box(
                    Modifier
                        .onGloballyPositioned { popupPosition = it.positionInWindow() }
                        .onPointerEvent(PointerEventType.Move) {
                            hideIfNotHovered(popupPosition + it.position)
                        }
                        .onPointerEvent(PointerEventType.Exit) {
                            hideIfNotHovered(popupPosition + it.position)
                        }
                ) {
                    tooltip.content()
                }
            })
        }
    }
    LaunchedEffect(tooltip) { startShowing() }
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
    this.getStringAnnotations(0, length).forEach { new.addStringAnnotation(it.tag, it.item, it.start, it.end) }
    getTtsAnnotations(0, length).forEach { new.addTtsAnnotation(it.item, it.start, it.end) }
    getUrlAnnotations(0, length).forEach { new.addUrlAnnotation(it.item, it.start, it.end) }
    return new
}

private val PointerEvent.position get() = changes.first().position
