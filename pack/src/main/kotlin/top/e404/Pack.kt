package top.e404

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

suspend fun main() {
    val target = mapOf(
        "1.12-" to "DynamicGlowOre_1.12-.zip",
        "1.12-_pbr" to "DynamicGlowOre_1.12-_pbr.zip",
        "1.13-1.16" to "DynamicGlowOre_1.13-1.16.zip",
        "1.13-1.16_pbr" to "DynamicGlowOre_1.13-1.16_pbr.zip",
        "1.17+" to "DynamicGlowOre_1.17+.zip",
        "ae2_pbr" to "DynamicGlowOre_ae2_1.16.5_pbr.zip",
        "create_pbr" to "DynamicGlowOre_create_1.16.5_pbr.zip",
        "mek_pbr" to "DynamicGlowOre_mek_1.16.5_pbr.zip",
    )

    val buildDir = File("build")

    coroutineScope {
        target.forEach { (folder, zipName) ->
            launch(Dispatchers.IO) {
                ZipOutputStream(buildDir.resolve(zipName).outputStream()).use { zos -> File(folder).zip(null, zos) }
            }
        }
    }
}

suspend fun File.zip(path: String?, zos: ZipOutputStream) {
    if (isDirectory) {
        listFiles()?.forEach {
            it.zip(if (path == null) it.name else "$path/${it.name}", zos)
        }
        return
    }
    withContext(Dispatchers.IO) {
        zos.putNextEntry(ZipEntry(path ?: name))
        inputStream().use { it.copyTo(zos) }
    }
}