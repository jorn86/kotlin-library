package org.hertsig.compose

import org.hertsig.logger.logger

private val log = logger {}

fun registerExceptionHandler() {
    Thread.setDefaultUncaughtExceptionHandler { _, e ->
        log.error("Uncaught exception", e)
        e.suppressed.forEach {
            if (it.stackTrace.isNotEmpty()) log.error("Suppressed", it)
        }
    }
}
