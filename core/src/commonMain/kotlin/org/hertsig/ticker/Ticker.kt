package org.hertsig.ticker

interface Ticker<T> {
    val tickCount: Int
    val lastTick: T
    suspend fun awaitNextTick(): Int
}
