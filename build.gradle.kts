plugins {
    id("java-library")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/FireML/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("uk.firedev:DaisyLib:3.1-SNAPSHOT")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks {
    processResources {
        val props = mapOf("version" to version)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}
