package org.hertsig.compose.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import org.hertsig.compose.util.Content
import java.text.MessageFormat
import java.util.*

private val LocalContext = compositionLocalOf { emptyList<ResourceProvider>() }

@Composable
fun r(key: ResourceKey, vararg arguments: Any) = resolveLocalizedString(key, *arguments)

@Composable
fun p(key: String, first: Int, vararg arguments: Any) = resolveLocalizedString(PluralResourceKey(key), first, *arguments)

@Composable
fun ProvideLocalization(vararg providers: ResourceProvider, content: Content) {
    CompositionLocalProvider(LocalContext provides providers.toList(), content = content)
}

@Composable
fun resolveLocalizedString(key: ResourceKey, vararg arguments: Any): String {
    val providers = LocalContext.current
    return providers.firstNotNullOfOrNull { it.resolve(key, *arguments) }
        ?: error("None out of ${providers.size} providers has a value for $key")
}

@Composable
fun rememberResourceResolver(): ResourceResolver {
    val providers = LocalContext.current
    return remember(providers) { ResourceResolver(providers) }
}

open class ResourceResolver(private val providers: List<ResourceProvider>) {
    fun resolve(key: ResourceKey, vararg arguments: Any): String {
        return providers.firstNotNullOfOrNull { it.resolve(key, *arguments) }
            ?: error("None out of ${providers.size} providers has a value for $key")
    }
}

interface ResourceKey
data class StringResourceKey(val key: String): ResourceKey
data class PluralResourceKey(val baseKey: String): ResourceKey
data class FallbackResourceKey(val keys: List<ResourceKey>): ResourceKey {
    constructor(vararg keys: ResourceKey) : this(keys.toList())

    companion object {
        fun create(keys: List<ResourceKey>) = when (keys.size) {
            0 -> throw IllegalArgumentException("No keys given")
            1 -> keys.single()
            else -> FallbackResourceKey(keys)
        }
    }
}
data class ResourceKeyWithArguments(val key: ResourceKey, val arguments: Array<out Any>): ResourceKey {
    constructor(key: String, vararg arguments: Any) : this(StringResourceKey(key), arguments)
}

interface ResourceProvider {
    fun resolve(key: ResourceKey, vararg arguments: Any): String?
}

class ResourceBundleProvider(baseName: String, locale: Locale): ResourceProvider {
    private val bundle = ResourceBundle.getBundle(baseName, locale)

    override fun resolve(key: ResourceKey, vararg arguments: Any): String? {
        return when (key) {
            is StringResourceKey -> format(bundle.getString(key.key), *arguments)
            is PluralResourceKey -> resolvePlural(key.baseKey, *arguments)
            is FallbackResourceKey -> resolveFallback(key, *arguments)
            is ResourceKeyWithArguments -> resolve(key.key, *key.arguments)
            else -> null
        }
    }

    private fun resolveFallback(fallbackKey: FallbackResourceKey, vararg arguments: Any): String {
        for (key in fallbackKey.keys) {
            val result = tryResolve(key, *arguments)
            if (result != null) return result
        }
        error("No key in $fallbackKey resolved to a value")
    }

    private fun tryResolve(key: ResourceKey, vararg arguments: Any) = try {
        resolve(key, *arguments)
    } catch (e: MissingResourceException) {
        null
    }

    private fun resolvePlural(baseKey: String, vararg arguments: Any): String {
        val amount = arguments.firstOrNull() as? Int
        require(amount != null) { "First argument of a PluralResourceKey must be an Int, but was ${arguments.firstOrNull()}" }
        val pluralKey = "$baseKey.$amount"
        if (bundle.containsKey(pluralKey)) return format(bundle.getString(pluralKey), *arguments)
        return format(bundle.getString(baseKey), *arguments)
    }

    private fun format(resource: String, vararg arguments: Any): String {
        val stringArguments = arguments.map { when (it) {
            is ResourceKey -> resolve(it)
            else -> it
        } }
        return MessageFormat.format(resource, *stringArguments.toTypedArray()).trim()
    }
}
