package org.hertsig.compose.component

import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalizationTest {
    @Test
    fun `PluralResourceKey resolves to the right keys`() {
        val bundle = ResourceBundleProvider("pluraltest", Locale.ROOT)
        val key = PluralResourceKey("n")
        assertEquals("1st", bundle.resolve(key, 1))
        assertEquals("2nd", bundle.resolve(key, 2))
        assertEquals("3rd", bundle.resolve(key, 3))
        assertEquals("4th", bundle.resolve(key, 4))
    }

    @Test
    fun `PluralResourceKey validates its first argument`() {
        val bundle = ResourceBundleProvider("pluraltest", Locale.ROOT)
        val key = PluralResourceKey("n")
        assertThrows<IllegalArgumentException> { bundle.resolve(key) }
        assertThrows<IllegalArgumentException> { bundle.resolve(key, "2") }
    }
}
