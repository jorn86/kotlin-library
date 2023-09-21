package org.hertsig.util

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.hertsig.logger.logger
import kotlin.time.Duration

private val log = logger {}

fun <T> MutableStateFlow<T>.set(value: T) { tryEmit(value) }
operator fun MutableStateFlow<Int>.plusAssign(operand: Int) = set(value + operand)
operator fun MutableStateFlow<Float>.plusAssign(operand: Float) = set(value + operand)
operator fun <T> MutableStateFlow<List<T>>.plusAssign(element: T) = set(value + element)
operator fun <T> MutableStateFlow<List<T>>.minusAssign(element: T) = set(value - element)

/**
 * Emits values into the downstream flow at most once per <window>.
 * Is guaranteed to always emit the latest value within <window> of receiving it.
 */
fun <T> Flow<T>.throttle(window: Duration, clock: Clock = Clock.System) = channelFlow {
    var latestEmission = Instant.DISTANT_PAST
    var emitLater: Job? = null
    collect {
        emitLater?.cancel()
        val now = clock.now()
        if (latestEmission < now - window) {
            log.trace { "Last emission was $latestEmission, sending $it now" }
            send(it)
            latestEmission = now
        } else {
            log.trace { "Last emission was $latestEmission, delay sending $it" }
            emitLater = launch {
                delay(window - (latestEmission - now))
                log.trace { "Sending delayed value $it" }
                send(it)
                latestEmission = clock.now()
            }
        }
    }
}
