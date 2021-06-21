pluginManagement {

    repositories {
        gradlePluginPortal()
        google()
    }

    resolutionStrategy {
        eachPlugin {
            val id = requested.id.id
            when {
                id.startsWith("com.android") ->
                    useModule("com.android.tools.build:gradle:${requested.version}")
                id.startsWith("androidx.navigation.safeargs") ->
                    useModule("androidx.navigation:navigation-safe-args-gradle-plugin:${requested.version}")
            }
        }
    }
}
// FIXME - Переименовать на свое
rootProject.name = ("base-project")

include(
    "app",
    "data",
    "domain",
    "base-test"
)
