package org.hertsig.util

import com.google.common.collect.Ordering
import java.util.*

inline fun <reified E:Enum<E>> enumSetOf(vararg values: E): EnumSet<E> {
    val set = EnumSet.noneOf(E::class.java)
    set.addAll(values)
    return set
}

fun <E: Any> Comparator<E>.nullsFirst(): Comparator<E?> = Ordering.from(this).nullsFirst<E>()
fun <E: Any> Comparator<E>.nullsLast(): Comparator<E?> = Ordering.from(this).nullsLast<E>()
fun <E,V> List<E>.sortedBy(comparator: Comparator<V>, by: (E) -> V) = sortedWith(Ordering.from(comparator).onResultOf(by))
