package org.hertsig.compose

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector

@Throws(ReflectiveOperationException::class)
fun resolveIcon(iconName: String): ImageVector {
    val (set, name) = iconName.split(".", limit = 2)
    val (pkg, obj) = when (set) {
        "Filled" -> "androidx.compose.material.icons.filled" to Icons.Filled
        "Outlined" -> "androidx.compose.material.icons.outlined" to Icons.Outlined
        "Rounded" -> "androidx.compose.material.icons.rounded" to Icons.Rounded
        "Sharp" -> "androidx.compose.material.icons.sharp" to Icons.Sharp
        "TwoTone" -> "androidx.compose.material.icons.twotone" to Icons.TwoTone
        else -> throw IllegalArgumentException("Invalid icon set $set")
    }
    try {
        return Class.forName("$pkg.${name}Kt")
            .declaredMethods.single()
            .invoke(null, obj) as ImageVector
    } catch (e: ClassNotFoundException) {
        throw IllegalArgumentException("Invalid icon name $name")
    }
}
