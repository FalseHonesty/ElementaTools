package dev.falsehonesty.elementatools

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

class ElementaToolsGradlePlugin : KotlinCompilerPluginSupportPlugin {
    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean {
        return kotlinCompilation.target.project.plugins.hasPlugin(ElementaToolsGradlePlugin::class.java)
    }

    override fun getCompilerPluginId(): String = "dev.falsehonesty.elementa-tools"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = "dev.falsehonesty",
        artifactId = "elementa-tools",
        version = "0.1.0"
    )

    override fun applyToCompilation(
        kotlinCompilation: KotlinCompilation<*>
    ): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project

        val extension = project.extensions.findByType(ElementaToolsGradleExtension::class.java)
            ?: ElementaToolsGradleExtension()

        return project.provider { emptyList() }
    }

    override fun apply(target: Project): Unit = with(target) {
        extensions.create("elementaTools", ElementaToolsGradleExtension::class.java)
    }
}
