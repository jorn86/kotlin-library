package org.hertsig.ticker

import org.hertsig.logger.logger
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private val log = logger {}

class JavaTicker(private val tickDuration: Long) : Ticker<Long> {
    override var tickCount = 0; private set
    override var lastTick = System.nanoTime(); private set

    init {
        require(tickDuration > 0) { "tickDuration must be > 0, was $tickDuration" }
        log.debug("Starting with timer value $lastTick/${Long.MAX_VALUE} and $tickDuration increments")
    }

    override suspend fun awaitNextTick(): Int {
        log.trace { "Tick $tickCount" }
        var now = System.nanoTime()
        val next = lastTick + tickDuration
        while (next > now) now = System.nanoTime()
        lastTick = now
        return ++tickCount
    }

    companion object {
        fun create(ticksPerSecond: Int = 60) = JavaTicker((1.seconds / ticksPerSecond).inWholeNanoseconds)
        fun createWindow(ticksInWindow: Int = 60, window: Duration = 1.seconds): WindowTicker<Long> {
            val windowTime = window.inWholeNanoseconds
            return WindowTicker(JavaTicker((window / ticksInWindow).inWholeNanoseconds)) { it + windowTime }
        }
    }
}
