package org.hertsig.util

import com.google.common.base.CaseFormat

actual val Enum<*>.display: String get() = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name.replace('_', ' '))
