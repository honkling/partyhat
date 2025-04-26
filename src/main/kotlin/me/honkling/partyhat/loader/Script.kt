package me.honkling.partyhat.loader

import me.honkling.partyhat.runtime.Module
import java.io.File
import java.net.URLClassLoader
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.listDirectoryEntries
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.ScriptCollectedData
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.asSuccess
import kotlin.script.experimental.api.collectedAnnotations
import kotlin.script.experimental.api.compilerOptions
import kotlin.script.experimental.api.providedProperties
import kotlin.script.experimental.api.refineConfiguration
import kotlin.script.experimental.api.with
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvm.updateClasspath
import kotlin.script.experimental.util.PropertiesCollection.Key

@KotlinScript(
    fileExtension = "kts",
    compilationConfiguration = ScriptConfiguration::class
)
abstract class Script

val annotationKey = Key<Annotation>("moduleAnnotation")

class ScriptConfiguration : ScriptCompilationConfiguration({
    jvm {
        compilerOptions.append("-jvm-target=21")
        dependenciesFromCurrentContext(wholeClasspath = true)
        updateClasspath(
            (javaClass.classLoader as URLClassLoader).urLs.map { File(it.path) } +
            Path("plugins").listDirectoryEntries().filter { it.extension == "jar" }.map { it.toFile() }
        )
    }

    refineConfiguration {
        onAnnotations(BasicModule::class, MiniGameModule::class) {
            val annotation = it.collectedData?.get(ScriptCollectedData.collectedAnnotations)
                ?.map { it.annotation }
                ?.find { it is BasicModule || it is MiniGameModule }
                    ?: return@onAnnotations it.compilationConfiguration.asSuccess()

            return@onAnnotations it.compilationConfiguration.with {
                providedProperties("module" to Module::class)
                data[annotationKey] = annotation
            }.asSuccess()
        }
    }
})