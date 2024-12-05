import io.papermc.paperweight.userdev.PaperweightUserDependenciesExtension

plugins {
    id("com.gradleup.shadow")
}

//val isActiveProject = stonecutter.active == stonecutter.current
//if (isActiveProject) {
//    apply(plugin = "io.papermc.paperweight.userdev")
//}

dependencies {
    compileOnly(project(":core"))

    val paperApi = property("deps.paper_api") as String

//    if (isActiveProject) {
//        extensions.getByType(PaperweightUserDependenciesExtension::class.java)
//            .paperDevBundle(paperApi.substringAfterLast(':'))
//    }

    compileOnly(paperApi)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(property("java_version") as String))
}

tasks {
    shadowJar {
        relocate(
            "su.plo.voice.discs.mcver",
            "su.plo.voice.discs.v${stonecutter.current.version.replace('.', '_')}"
        )
    }

    build {
        dependsOn(shadowJar)
    }
}