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
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://jitpack.io/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit") { isTransitive = false }
    implementation(platform("com.intellectualsites.bom:bom-newest:1.52"))
    implementation("com.github.honkling.commando:spigot:e7170651bd")
    implementation("com.github.honkling:commonlib:9742e51e7d")
    implementation("cc.ekblad:4koma:1.2.0")
    implementation(rootProject)
}

tasks {
    runServer {
        minecraftVersion("1.21.4")
    }

    build {
        dependsOn("shadowJar")
    }

    jar {
        manifest {
            attributes("paperweight-mappings-namespace" to "mojang")
        }
    }

    processResources {
        val props = mapOf("version" to version, "kotlin" to kotlinVersion)
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
