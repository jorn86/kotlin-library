package org.hertsig.logger

import org.slf4j.Logger
import org.slf4j.LoggerFactory

actual fun logger(name: String): KotlinLogger = Slf4jLogger(LoggerFactory.getLogger(name))
fun slf4j(lambda: () -> Unit) = Slf4jLogger(LoggerFactory.getLogger(parseName(lambda)))

internal actual fun parseName(lambda: () -> Unit): String  {
    val name = lambda.javaClass.name
    return when {
        name.contains("Kt$") -> name.substringBefore("Kt$")
        else -> name.substringBefore("$")
    }
}

class Slf4jLogger(private val log: Logger): KotlinLogger(), Logger by log {
    override fun error(t: Throwable?, message: LazyMessage) {
        if (isErrorEnabled) error(message.safeInvoke(), t)
    }

    override fun warn(t: Throwable?, message: LazyMessage) {
        if (isWarnEnabled) warn(message.safeInvoke(), t)
    }

    override fun info(t: Throwable?, message: LazyMessage) {
        if (isInfoEnabled) info(message.safeInvoke(), t)
    }

    override fun debug(t: Throwable?, message: LazyMessage) {
        if (isDebugEnabled) debug(message.safeInvoke(), t)
    }

    override fun trace(t: Throwable?, message: LazyMessage) {
        if (isTraceEnabled) trace(message.safeInvoke(), t)
    }

    override fun error(message: String, t: Throwable?) = log.error(message, t)
    override fun warn(message: String, t: Throwable?) = log.warn(message, t)
    override fun info(message: String, t: Throwable?) = log.info(message, t)
    override fun debug(message: String, t: Throwable?) = log.debug(message, t)
    override fun trace(message: String, t: Throwable?) = log.trace(message, t)
}
