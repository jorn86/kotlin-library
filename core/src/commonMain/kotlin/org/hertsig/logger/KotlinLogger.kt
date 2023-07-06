package org.hertsig.logger

private val log = logger {}

fun logger(name: () -> Unit) = logger(parseName(name))

internal expect fun parseName(lambda: () -> Unit): String
expect fun logger(name: String): KotlinLogger

abstract class KotlinLogger {
    abstract fun error(t: Throwable? = null, message: LazyMessage)
    abstract fun error(message: String, t: Throwable? = null)
    abstract fun warn(t: Throwable? = null, message: LazyMessage)
    abstract fun warn(message: String, t: Throwable? = null)
    abstract fun info(t: Throwable? = null, message: LazyMessage)
    abstract fun info(message: String, t: Throwable? = null)
    abstract fun debug(t: Throwable? = null, message: LazyMessage)
    abstract fun debug(message: String, t: Throwable? = null)
    abstract fun trace(t: Throwable? = null, message: LazyMessage)
    abstract fun trace(message: String, t: Throwable? = null)
}

typealias LazyMessage = () -> String
internal fun LazyMessage.safeInvoke(): String = try {
    invoke()
} catch (e: Exception) {
    val message = "Exception invoking lazy message ${this::class.simpleName}"
    log.error(message, e)
    message
}
