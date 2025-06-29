package me.honkling.minievent.config

import cc.ekblad.toml.TomlMapper
import cc.ekblad.toml.decode
import cc.ekblad.toml.model.TomlValue
import me.honkling.minievent.instance

inline fun <reified T : Any> getAndMapConfig(name: String, mapper: TomlMapper): T {
    val file = instance.dataFolder.resolve(name)

    if (!file.exists()) {
        val inputStream = instance::class.java.getResourceAsStream("/$name")!!
        val outputStream = file.outputStream()
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
    }

    return mapper.decode<T>(file.toPath())
}

fun <T> TomlValue.value(): T {
    return when (this) {
        is TomlValue.List -> elements
        is TomlValue.Map -> properties
        is TomlValue.Bool -> value
        is TomlValue.Double -> value
        is TomlValue.Integer -> value
        is TomlValue.LocalDate -> value
        is TomlValue.LocalDateTime -> value
        is TomlValue.LocalTime -> value
        is TomlValue.OffsetDateTime -> value
        is TomlValue.String -> value
    } as T
}