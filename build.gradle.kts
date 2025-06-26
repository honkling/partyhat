import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin

plugins {
    kotlin("jvm") version "2.2.0"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "me.honkling"
version = "0.1.0"

val kotlinVersion = (plugins.getPlugin("kotlin") as KotlinBasePlugin).pluginVersion

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://jitpack.io/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compileOnly(kotlin("reflect"))
}

tasks {
    build {
        dependsOn("shadowJar")
    }

    processResources {
        val props = mapOf(
            "version" to version, "kotlin" to kotlinVersion
        )
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