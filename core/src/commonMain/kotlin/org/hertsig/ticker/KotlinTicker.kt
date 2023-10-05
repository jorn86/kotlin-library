package org.hertsig.ticker

import org.hertsig.logger.logger
import kotlin.time.ComparableTimeMark
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

private val log = logger {}

@OptIn(ExperimentalTime::class)
class KotlinTicker(
    private val tickDuration: Duration,
    private val timer: TimeSource.WithComparableMarks = TimeSource.Monotonic
): Ticker<ComparableTimeMark> {
    override var tickCount = 0; private set
    override var lastTick = timer.markNow(); private set

    init {
        require(tickDuration.isPositive()) { "tickDuration must be > 0, was $tickDuration" }
        log.debug("Starting with timer value $lastTick and $tickDuration increments")
    }

    override suspend fun awaitNextTick(): Int {
        log.trace { "Tick $tickCount" }
        val next = lastTick + tickDuration
        @Suppress("ControlFlowWithEmptyBody")
        while (next.hasNotPassedNow()); // Sleeping/delaying makes this (way) too unreliable
        lastTick = timer.markNow()
        return ++tickCount
    }

    companion object {
        fun create(ticksPerSecond: Int = 60) = KotlinTicker(1.seconds / ticksPerSecond)
        fun createWindow(ticksInWindow: Int = 60, window: Duration = 1.seconds) =
            WindowTicker.create(KotlinTicker(window / ticksInWindow), window)
    }
}
