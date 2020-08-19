import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-gradle-plugin")
    kotlin("jvm")

    id("com.gradle.plugin-publish")
}

dependencies {
    implementation(kotlin("gradle-plugin-api"))
}

pluginBundle {
    website = "https://github.com/FalseHonesty/ElementaTools"
    vcsUrl = "https://github.com/FalseHonesty/ElementaTools.git"
    tags = listOf("kotlin", "elementa")
}

gradlePlugin {
    plugins {
        create("elementaTools") {
            id = "dev.falsehonesty.elementatools"
            displayName = "Elementa Tools Plugin"
            description = "Kotlin Compiler Plugin to enhance your usage of Elementa"
            implementationClass = "dev.falsehonesty.elementatools.ElementaToolsGradlePlugin"
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.register("publish") {
    dependsOn("publishPlugins")
}
