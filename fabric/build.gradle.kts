import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("fabric-loom") version "1.9.2"
    kotlin("jvm") version "2.1.20"
}

fun prop(name: String): String = project.property(name) as String

version = prop("mod_version")
group = prop("mod_group_id")

base {
    archivesName.set("placedsticks-fabric-${prop("minecraft_version")}")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

kotlin {
    jvmToolchain(21)
    sourceSets.named("main") {
        kotlin.srcDir(rootProject.file("src/main/kotlin"))
        kotlin.exclude("**/PlacedSticksMod.kt")
        kotlin.exclude("**/ModBlocks.kt")
        kotlin.exclude("**/event/RodPlacement.kt")
    }
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

sourceSets.named("main") {
    resources.srcDir(rootProject.file("src/main/resources"))
}

dependencies {
    minecraft("com.mojang:minecraft:${prop("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${prop("fabric_loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("fabric_api_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${prop("fabric_kotlin_version")}")
}

tasks.processResources {
    val version = project.version.toString()
    inputs.property("version", version)
    filesMatching("fabric.mod.json") {
        filter { line -> line.replace("\${version}", version) }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}
