package org.hertsig.ticker

import kotlinx.coroutines.flow.MutableStateFlow
import org.hertsig.util.set
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class WindowTicker<T: Comparable<T>>(
    private val ticker: Ticker<T>,
    private val windowAdder: (T) -> T
): Ticker<T> by ticker {
    private val history = mutableListOf<T>()
    val ticksInWindow = MutableStateFlow(0)

    override suspend fun awaitNextTick(): Int {
        val tick = ticker.awaitNextTick()
        history.add(lastTick)
        history.retainAll { windowAdder(it) >= lastTick }
        ticksInWindow.set(history.size)
        return tick
    }

    companion object {
        fun create(ticker: KotlinTicker, window: Duration = 1.seconds) =
            WindowTicker(ticker) { it + window }
    }
}
