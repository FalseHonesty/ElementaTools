package dev.falsehonesty.elementatools

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor

@AutoService(CommandLineProcessor::class)
class ElementaToolsCommandLineProcessor : CommandLineProcessor {
    override val pluginId: String = "dev.falsehonesty.elementa-tools"

    override val pluginOptions: Collection<CliOption> = listOf()
}
