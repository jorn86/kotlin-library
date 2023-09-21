package org.hertsig.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.sharp.Cloud
import androidx.compose.material.icons.twotone.Crop169
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class IconIndexTest {
    @Test
    fun `existing icon resolves correctly`() {
        assertEquals(resolveIcon("Filled.Warning"), Icons.Filled.Warning)
        assertEquals(resolveIcon("Outlined.Cloud"), Icons.Outlined.Cloud)
        assertEquals(resolveIcon("Rounded.Home"), Icons.Rounded.Home)
        assertEquals(resolveIcon("Sharp.Cloud"), Icons.Sharp.Cloud)
        assertEquals(resolveIcon("TwoTone.Crop169"), Icons.TwoTone.Crop169)
    }

    @Test
    fun `invalid set throws exception`() {
        assertThrows<IllegalArgumentException> { resolveIcon("Some.Other") }
    }

    @Test
    fun `invalid name throws exception`() {
        assertThrows<IllegalArgumentException> { resolveIcon("Filled.Nonexistent") }
    }
}
