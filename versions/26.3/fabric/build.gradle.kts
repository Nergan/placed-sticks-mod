import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties
import org.gradle.api.tasks.Sync

plugins {
    id("net.fabricmc.fabric-loom") version "1.18.4"
    kotlin("jvm")
}

fun prop(name: String): String = project.property(name) as String

fun parentProp(name: String): String {
    val props = Properties()
    rootProject.file("../../gradle.properties").reader(Charsets.UTF_8).use { props.load(it) }
    return props.getProperty(name) ?: error("Missing $name in the 1.21.1 gradle.properties")
}

version = parentProp("mod_version")
group = parentProp("mod_group_id")

base {
    archivesName.set("placedsticks-fabric-${prop("minecraft_version")}")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(25))

val sharedKotlin by tasks.registering(Sync::class) {
    from(rootProject.file("../../src/main/kotlin")) {
        exclude("com/placedsticks/mod/PlacedSticksMod.kt")
        exclude("com/placedsticks/mod/block/ModBlocks.kt")
        exclude("com/placedsticks/mod/block/RodBlock.kt")
        exclude("com/placedsticks/mod/block/RodPlacementAction.kt")
        exclude("com/placedsticks/mod/event/RodPlacement.kt")
    }
    from(rootProject.file("../../fabric/src/main/kotlin")) {
        exclude("com/placedsticks/mod/loader/fabric/PlacedSticksFabric.kt")
    }
    into(layout.buildDirectory.dir("shared-kotlin"))
}

kotlin {
    jvmToolchain(25)
    sourceSets.named("main") {
        kotlin.srcDir(sharedKotlin)
        kotlin.srcDir(rootProject.file("src/main/kotlin"))
        kotlin.srcDir(file("src/main/kotlin"))
    }
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_25)
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

sourceSets.named("main") {
    java.srcDir(rootProject.file("../../fabric/src/main/java"))
    resources.srcDir("src/main/resources")
    // fabric/ is a subproject of versions/26.3, so the 26.3 resources are rootProject/src.
    resources.srcDir(rootProject.file("src/main/resources"))
    resources.srcDir(rootProject.file("../../src/main/resources"))
}

dependencies {
    minecraft("com.mojang:minecraft:${prop("minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${prop("fabric_loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${prop("fabric_api_version")}")
    implementation("net.fabricmc:fabric-language-kotlin:${prop("fabric_kotlin_version")}")
}

tasks.processResources {
    val version = project.version.toString()
    inputs.property("version", version)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    filesMatching("fabric.mod.json") {
        filter { line -> line.replace("\${version}", version) }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

loom {
    runs {
        named("server") {
            programArg("--nogui")
            programArg("--port")
            programArg("25567")
            vmArg("-Xms128m")
            vmArg("-Xmx512m")
            vmArg("-XX:+UseSerialGC")
        }
        named("client") {
            vmArg("-Xms512m")
            vmArg("-Xmx1536m")
            vmArg("-XX:+UseSerialGC")
        }
    }
}
