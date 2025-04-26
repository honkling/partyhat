package me.honkling.partyhat.loader

import me.honkling.partyhat.runtime.Module
import me.honkling.partyhat.scriptsFolder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.io.File
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.ScriptDiagnostic
import kotlin.script.experimental.jvm.util.isError

class ScriptLoader {
    val loadedScripts = mutableMapOf<File, Module>()

    fun loadAll(folder: File = scriptsFolder): Map<File, ResultWithDiagnostics<Nothing?>> {
        if (!folder.startsWith(scriptsFolder))
            throw IllegalArgumentException("Folder must be inside scripts folder")

        if (!folder.isDirectory)
            throw IllegalArgumentException("File must be a folder")

        val reports = mutableMapOf<File, ResultWithDiagnostics<Nothing?>>()

        for (file in folder.listFiles() ?: emptyArray())
            if (file.isDirectory) {
                val result = loadAll(file)
                reports.putAll(result)
            } else if (file.extension == "kts")
                reports[file] = load(file)

        return reports
    }

    fun load(file: File): ResultWithDiagnostics<Nothing?> {
        val result = evaluateScript(file, emptyList())

        if (!result.isError())
            return ResultWithDiagnostics.Failure(result.reports)

        return ResultWithDiagnostics.Success(null, result.reports)
    }

    fun enable(file: File) {
        if (!file.isFile || !file.name.startsWith("-") || file in loadedScripts)
            return

        file.renameTo(file.parentFile.resolve(file.name.substring(1)))
        load(file)
    }

    fun disable(file: File) {
        if (!file.isFile || file.name.startsWith("-") || file !in loadedScripts)
            return

        file.renameTo(file.parentFile.resolve("-" + file.name))
        loadedScripts[file]!!.cleanUp()
        loadedScripts -= file
    }

    fun displayResultMap(folder: File, map: Map<File, ResultWithDiagnostics<Nothing?>>): Component {
        val name = Component.text(
            (if (folder == scriptsFolder) "all scripts"
            else folder.relativeTo(scriptsFolder).path) + " "
        ).color(NamedTextColor.GOLD)

        val errorCount = map.values.map { it.reports }.flatten().filter { it.severity >= ScriptDiagnostic.Severity.ERROR }.size
        var component = Component.empty()
            .append(Component.text("Reloaded ")
                .color(NamedTextColor.RED)
                .append(name)
                .append(Component.text("with $errorCount error${if (errorCount == 1) "" else "s"}.")))

        for ((file, result) in map) {
            val display = displayResult(file, result, false)

            if (display != Component.empty())
                component = component.appendNewline()
                    .append()
        }

        return component
    }

    fun displayResult(file: File, result: ResultWithDiagnostics<Nothing?>, header: Boolean = true): Component {
        val reports = result.reports.filter { it.severity >= ScriptDiagnostic.Severity.WARNING }
        val name = file.relativeTo(scriptsFolder).path
        val errorCount = reports.size

        var component = if (!header) Component.empty()
            else Component.empty()
                .append(Component.text("Reloaded ")
                .color(NamedTextColor.RED)
                .append(Component.text())
                .append(Component.text("with $errorCount error${if (errorCount == 1) "" else "s"}.")))

        for ((index, report) in reports.withIndex()) {
            val start = report.location?.start
            val location = start?.let { " ($name:${start.line}:${start.col})" } ?: ""
            val color = when (report.severity) {
                ScriptDiagnostic.Severity.INFO -> NamedTextColor.GRAY
                ScriptDiagnostic.Severity.WARNING -> NamedTextColor.GOLD
                ScriptDiagnostic.Severity.DEBUG, ScriptDiagnostic.Severity.ERROR -> NamedTextColor.RED
                ScriptDiagnostic.Severity.FATAL -> NamedTextColor.DARK_RED
            }

            component = component
                .append(Component
                    .text("${report.severity.name.lowercase()}$location")
                    .color(color))
                .append(Component.text(": ${report.message}"))

            if (index + 1 < reports.size)
                component = component.appendNewline()
        }

        return component
    }
}