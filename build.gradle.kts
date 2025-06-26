import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin

plugins {
    kotlin("jvm") version "2.2.0"
    id("com.gradleup.shadow") version "8.3.0"
    `maven-publish`
}

group = "me.honkling.partyhat"
version = "0.1.0"

val kotlinVersion = (plugins.getPlugin("kotlin") as KotlinBasePlugin).pluginVersion

allprojects {
    apply(plugin = "kotlin")
    repositories.mavenCentral()

    kotlin {
        jvmToolchain(21)
    }

    tasks.build {
        dependsOn("shadowJar", "publishToMavenLocal")
    }
}

subprojects {
    apply(plugin = "com.gradleup.shadow")
    apply(plugin = "maven-publish")
    apply(plugin = "java")

    group = rootProject.group
    version = rootProject.version

    dependencies {
        compileOnly(kotlin("stdlib-jdk8"))
        compileOnly(kotlin("reflect"))
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = rootProject.group.toString()
                artifactId = project.name
                version = rootProject.version.toString()

                from(components["java"])
            }
        }
    }

    java {
        withSourcesJar()
        withJavadocJar()
    }
}