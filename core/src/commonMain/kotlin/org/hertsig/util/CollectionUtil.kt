package org.hertsig.util

inline fun <reified E: Any> Iterable<*>.firstOrNull() = firstOrNull { it is E } as E?
inline fun <reified E: Any> Iterable<*>.lastOrNull() = lastOrNull { it is E } as E?

fun <K, V> MutableMap<K, V>.putFirst(key: K, value: V) {
    require(!containsKey(key)) { "Duplicate key $key" }
    put(key, value)
}
