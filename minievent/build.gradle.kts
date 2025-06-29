import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin

plugins {
    kotlin("jvm") version "2.2.0"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "me.honkling"
version = "0.1.0"

val kotlinVersion = (plugins.getPlugin("kotlin") as KotlinBasePlugin).pluginVersion

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io/")
}

dependencies {
    compileOnly("net.minestom:minestom-snapshots:4fe2993057")
    compileOnly("me.honkling:slate:1.0.3")
    compileOnly("team.azalea:plugins:1ab3251a57")
    compileOnly("cc.ekblad:4koma:1.2.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.14.0")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//    implementation("dev.flavored:bamboo:1.1.1")
    implementation(files("/data/IDEAProjects/bamboo/build/libs/bamboo-1.1.2.jar"))
    implementation("com.github.honkling.commando:minestom:e7170651bd")
    implementation(rootProject)
}

tasks {
    build {
        dependsOn("shadowJar")
    }

    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

kotlin {
    jvmToolchain(21)
}
