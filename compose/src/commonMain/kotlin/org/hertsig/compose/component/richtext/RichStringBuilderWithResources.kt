package org.hertsig.compose.component.richtext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.SpanStyle
import org.hertsig.compose.component.ResourceKey
import org.hertsig.compose.component.ResourceResolver
import org.hertsig.compose.component.rememberResourceResolver

@Composable
fun rememberRichStringWithResources(vararg keys: Any, builder: RichStringBuilderWithResources.() -> Unit): RichString {
    val resolver = rememberResourceResolver()
    return remember(resolver, *keys) {
        RichStringBuilderWithResources(resolver).apply(builder).build()
    }
}

open class RichStringBuilderWithResources
internal constructor(private val resourceResolver: ResourceResolver): RichStringBuilder() {
    fun append(key: ResourceKey, vararg arguments: Any) = append(resolve(key, arguments))
    fun append(style: SpanStyle, key: ResourceKey, vararg arguments: Any) = append(resolve(key, arguments), style)
    fun appendIfNotBlank(key: ResourceKey, extra: String = "", vararg arguments: Any): RichStringBuilder {
        val resource = resolve(key, arguments)
        if (resource.isNotBlank()) append("$resource$extra")
        return this
    }
    private fun resolve(key: ResourceKey, arguments: Array<out Any>) = resourceResolver.resolve(key, *arguments)
}
