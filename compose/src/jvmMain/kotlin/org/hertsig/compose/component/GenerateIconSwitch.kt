package org.hertsig.compose.component

import androidx.compose.material.icons.Icons.Filled
import com.google.common.reflect.ClassPath
import java.io.BufferedWriter
import kotlin.io.path.Path
import kotlin.io.path.bufferedWriter

fun main() {
    // Actually generating this then compiling crashes the compiler
    Path("./kotlin-library/compose/src/commonMain/kotlin/org/hertsig/compose/IconIndex.kt").bufferedWriter().use {
        it.appendLine("package org.hertsig.compose")
            .appendLine()
            .appendLine("import androidx.compose.material.icons.Icons")
            .appendLine("import androidx.compose.material.icons.filled.*")
            .appendLine("import androidx.compose.material.icons.outlined.*")
            .appendLine("import androidx.compose.material.icons.rounded.*")
            .appendLine("import androidx.compose.material.icons.sharp.*")
            .appendLine("import androidx.compose.material.icons.twotone.*")
            .appendLine()
            .appendLine("fun resolveIcon(name: String) = when (name) {")
        addLines(it, "androidx.compose.material.icons.filled", "Filled")
        addLines(it, "androidx.compose.material.icons.outlined", "Outlined")
        addLines(it, "androidx.compose.material.icons.rounded", "Rounded")
        addLines(it, "androidx.compose.material.icons.sharp", "Sharp")
        addLines(it, "androidx.compose.material.icons.twotone", "TwoTone")
        it.appendLine("    else -> error(\"Unrecognized icon name \$name\")")
            .appendLine("}")
    }
    Filled
}

private fun addLines(writer: BufferedWriter, pkg: String, prefix: String) {
    ClassPath.from(ClassLoader.getSystemClassLoader()).getTopLevelClasses(pkg).forEach {
        val name = it.simpleName.substringBeforeLast("Kt")
        writer.appendLine("""    "$prefix.$name" -> Icons.$prefix.$name""")
    }
}
