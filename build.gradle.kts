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
}

dependencies {
    compileOnly(libs.paper.api)

    paperLibrary(libs.daisylib)
    paperLibrary(libs.jda)
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
        register("DaisyLib") {
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
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    generatePaperPluginDescription {
        useGoogleMavenCentralProxy()
    }
}
