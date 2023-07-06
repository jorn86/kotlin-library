package org.hertsig.util

import com.google.common.base.CaseFormat

val Enum<*>.display: String get() = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name.replace('_', ' '))
