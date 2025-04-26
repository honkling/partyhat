package me.honkling.partyhat.loader

import me.honkling.partyhat.runtime.Module
import me.honkling.partyhat.runtime.miniGames
import me.honkling.partyhat.runtime.modules
import java.io.File
import kotlin.collections.map
import kotlin.reflect.KClass
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.ScriptCollectedData
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.api.SourceCode
import kotlin.script.experimental.api.asSuccess
import kotlin.script.experimental.api.collectedAnnotations
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.api.providedProperties
import kotlin.script.experimental.api.refineConfigurationBeforeEvaluate
import kotlin.script.experimental.api.with
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.baseClassLoader
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

fun evaluateScript(
    file: File,
    imports: List<String>,
    vararg symbols: Pair<Pair<String, KClass<*>>, Any>
): ResultWithDiagnostics<EvaluationResult> {
    val script = file.toScriptSource()
    val compilationConfiguration = createJvmCompilationConfigurationFromTemplate<Script> {
        defaultImports(
            "me.honkling.partyhat.runtime.modules",
            "me.honkling.partyhat.runtime.miniGames",
            *imports.toTypedArray()
        )
        providedProperties(*symbols.map { it.first.first to it.first.second }.toTypedArray())
    }

    val evaluationConfiguration = ScriptEvaluationConfiguration {
        jvm {
            baseClassLoader(this::class.java.classLoader)
        }

        refineConfigurationBeforeEvaluate {
            val annotation = it.compiledScript.compilationConfiguration.notTransientData[annotationKey] as Annotation?
            println(annotation ?: "<null>")
            if (annotation == null)
                return@refineConfigurationBeforeEvaluate it.evaluationConfiguration.asSuccess()

            return@refineConfigurationBeforeEvaluate it.evaluationConfiguration.with {
                providedProperties("module" to when (annotation) {
                    is BasicModule -> modules.register(file)
                    is MiniGameModule -> miniGames.register(file)
                    else -> throw IllegalStateException("Unknown annotation")
                }, *symbols.map { it.first.first to it.second }.toTypedArray())
            }.asSuccess()
        }
    }

    return BasicJvmScriptingHost().eval(script, compilationConfiguration, evaluationConfiguration)
}