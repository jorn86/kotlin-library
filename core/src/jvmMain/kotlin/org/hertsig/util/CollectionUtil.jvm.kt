package org.hertsig.util

import java.util.*

inline fun <reified E:Enum<E>> enumSetOf(vararg values: E): EnumSet<E> {
    val set = EnumSet.noneOf(E::class.java)
    set.addAll(values)
    return set
}
