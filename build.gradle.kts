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
    compileOnly("net.minestom:minestom-snapshots:4fe2993057")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compileOnly(kotlin("reflect"))
}

tasks.build {
    dependsOn("shadowJar")
}

kotlin {
    jvmToolchain(21)
}