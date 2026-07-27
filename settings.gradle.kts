rootProject.name = "WzWStuff"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("paper-api", "io.papermc.paper:paper-api:26.2.build.+")
            library("daisylib", "uk.firedev:DaisyLib:3.1-SNAPSHOT")

            plugin("shadow", "com.gradleup.shadow").version("9.0.0")
            plugin("plugin-yml", "de.eldoria.plugin-yml.paper").version("0.9.0")
        }
    }
}