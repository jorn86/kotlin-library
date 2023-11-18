package org.hertsig.util

inline fun <reified E: Any> Iterable<*>.firstOrNull() = firstOrNull { it is E } as E?
inline fun <reified E: Any> Iterable<*>.lastOrNull() = lastOrNull { it is E } as E?

fun <K, V> MutableMap<K, V>.putFirst(key: K, value: V) {
    require(!containsKey(key)) { "Duplicate key $key" }
    put(key, value)
}

/** As distinctBy from the stdlib, but keeps the last value for each key instead of the first one */
fun <T,K> Collection<T>.distinctByKeepLast(selector: (T) -> K): Collection<T> {
    val results = LinkedHashMap<K, T>()
    forEach {
        results[selector(it)] = it
    }
    return results.values
}

/** As subList but with sensible defaults and supports Python-style "all except" for the end parameter */
fun <T> List<T>.slice(start: Int = 0, end: Int = size) = subList(start, if (end < 0) end + size else end)

fun <T> MutableCollection<in T>.add(vararg elements: T) = addAll(elements)

fun <T> Collection<T>.containsAny(elements: Collection<T>) = elements.any { it in this }
fun <T> Collection<T>.containsAny(vararg elements: T) = elements.any { it in this }
