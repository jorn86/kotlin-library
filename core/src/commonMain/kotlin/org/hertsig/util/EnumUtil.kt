package org.hertsig.util 

expect val Enum<*>.display: String

open class EnumCompanion<E: Enum<E>, T>(values: Array<E>, value: (E) -> T) {
    private val index = values.associateBy(value)
    fun fromValue(value: T) = index.getValue(value)
    fun fromValueOrNull(value: T) = index[value]
}
