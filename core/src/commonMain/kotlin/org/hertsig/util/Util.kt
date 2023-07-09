package org.hertsig.util

fun <T> T.applyIf(condition: Boolean, action: T.() -> T) = if (condition) action() else this
