import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties
import org.gradle.api.tasks.Sync

plugins {
    kotlin("jvm") version "2.4.0"
    id("net.neoforged.moddev") version "2.0.148"
    `maven-publish`
    idea
}

fun prop(name: String): String = project.property(name) as String

fun parentProp(name: String): String {
    val props = Properties()
    file("../../gradle.properties").reader(Charsets.UTF_8).use { props.load(it) }
    return props.getProperty(name) ?: error("Missing $name in the 1.21.1 gradle.properties")
}

val modId = parentProp("mod_id")
val modVersion = parentProp("mod_version")
val modGroupId = parentProp("mod_group_id")
val modName = parentProp("mod_name")
val modLicense = parentProp("mod_license")
val modAuthors = parentProp("mod_authors")
val modDescription = parentProp("mod_description")
val minecraftVersion = prop("minecraft_version")
val minecraftVersionRange = prop("minecraft_version_range")
val neoVersion = prop("neo_version")
val kffVersion = prop("kff_version")
val kffVersionRange = prop("kff_version_range")

version = modVersion
group = modGroupId

base {
    archivesName.set("${modId}-neoforge-${minecraftVersion}")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(25))

val sharedKotlin by tasks.registering(Sync::class) {
    from(file("../../src/main/kotlin")) {
        exclude("com/placedsticks/mod/block/RodBlock.kt")
        exclude("com/placedsticks/mod/block/RodPlacementAction.kt")
        exclude("com/placedsticks/mod/block/ModBlocks.kt")
        exclude("com/placedsticks/mod/event/RodPlacement.kt")
    }
    into(layout.buildDirectory.dir("shared-kotlin"))
}

kotlin {
    jvmToolchain(25)
    sourceSets.named("main") {
        kotlin.srcDir(sharedKotlin)
        kotlin.srcDir(file("src/main/kotlin"))
        kotlin.srcDir(file("src/neoforge/kotlin"))
    }
    sourceSets.named("test") {
        kotlin.srcDir(file("../../src/test/kotlin"))
    }
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_25)
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

sourceSets.main {
    resources.srcDir("src/main/resources")
    resources.srcDir(file("../../src/main/resources"))
    resources {
        exclude("**/*.bbmodel")
        exclude("src/generated/**/.cache")
    }
}

repositories {
    maven {
        name = "Kotlin for Forge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }
    mavenCentral()
}

neoForge {
    enable {
        version = neoVersion
        // CI sets this automatically. Locally it skips Vineflower, which needs more RAM than this machine has.
        setDisableRecompilation(true)
    }

    runs {
        create("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
            jvmArguments.addAll(listOf("-Xms512m", "-Xmx1536m", "-XX:+UseSerialGC"))
        }
        create("server") {
            server()
            programArguments.addAll(listOf("--nogui", "--port", "25567"))
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
            jvmArguments.addAll(listOf("-Xms512m", "-Xmx1024m", "-XX:+UseSerialGC"))
        }
        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel.set(org.slf4j.event.Level.INFO)
        }
    }

    mods {
        create(modId) {
            sourceSet(sourceSets.main.get())
        }
    }
}

val localRuntime by configurations.creating
configurations.named("runtimeClasspath") {
    extendsFrom(localRuntime)
}

dependencies {
    implementation("thedarkcolour:kotlinforforge-neoforge:$kffVersion")
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

val generateModMetadata by tasks.registering(ProcessResources::class) {
    val replaceProperties = mapOf(
        "minecraft_version" to minecraftVersion,
        "minecraft_version_range" to minecraftVersionRange,
        "neo_version" to neoVersion,
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_license" to modLicense,
        "mod_version" to modVersion,
        "mod_authors" to modAuthors,
        "mod_description" to modDescription,
        "kff_version_range" to kffVersionRange,
    )
    inputs.properties(replaceProperties)
    from("src/main/templates")
    into("build/generated/sources/modMetadata")
    filteringCharset = "UTF-8"
    filter { line ->
        var result = line
        for ((key, value) in replaceProperties) {
            result = result.replace("\${$key}", value)
        }
        result
    }
}

sourceSets.main.get().resources.srcDir(generateModMetadata)
neoForge.ideSyncTask(generateModMetadata)

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.jar {
    manifest {
        attributes(
            "Specification-Title" to modId,
            "Specification-Vendor" to modAuthors,
            "Specification-Version" to "1",
            "Implementation-Title" to modName,
            "Implementation-Version" to modVersion,
            "Implementation-Vendor" to modAuthors,
        )
    }
}
