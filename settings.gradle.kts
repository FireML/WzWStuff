rootProject.name = "WzWStuff"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("paper-api", "io.papermc.paper:paper-api:26.2.build.+")
            library("daisylib", "uk.firedev:DaisyLib:4.0-SNAPSHOT")
            library("jda", "net.dv8tion:JDA:6.5.0")
            library("griefprevention", "com.github.GriefPrevention:GriefPrevention:16.18.7")
            library("placeholderapi", "me.clip:placeholderapi:2.12.3")

            plugin("shadow", "com.gradleup.shadow").version("9.0.0")
            plugin("plugin-yml", "de.eldoria.plugin-yml.paper").version("0.9.0")
        }
    }
}