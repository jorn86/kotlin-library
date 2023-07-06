package org.hertsig.logger

actual fun logger(name: String): KotlinLogger {
    TODO("Not yet implemented")
}

internal actual fun parseName(lambda: () -> Unit) = lambda::class.simpleName.orEmpty()
