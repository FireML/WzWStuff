import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    `java-library`
    alias(libs.plugins.shadow)
    alias(libs.plugins.plugin.yml)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/FireML/")
    maven("https://repo.helpch.at/releases/")

    // Should always be last because it's dumb.
    maven("https://jitpack.io")
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.griefprevention)
    compileOnly(libs.placeholderapi)

    implementation(libs.daisylib)
    implementation(libs.jda)
}

group = "uk.firedev"
version = project.property("project-version") as String
java.sourceCompatibility = JavaVersion.VERSION_25

paper {
    name = project.name
    version = project.version.toString()
    main = "uk.firedev.wzwstuff.WzWStuff"
    apiVersion = "26.2"
    author = "FireML"

    loader = "uk.firedev.wzwstuff.LibraryLoader"
    generateLibrariesJson = true

    serverDependencies {
        register("GriefPrevention") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
        register("PlaceholderAPI") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveBaseName.set(project.name)
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("")

        relocate("uk.firedev.daisylib", "uk.firedev.wzwstuff.libs.daisylib")
        relocate("net.dv8tion.jda", "uk.firedev.wzwstuff.libs.jda")
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    generatePaperPluginDescription {
        useGoogleMavenCentralProxy()
    }
}
