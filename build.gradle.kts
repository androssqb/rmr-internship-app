import io.gitlab.arturbosch.detekt.Detekt

plugins {
    // pre-load plugins
    id(GradlePluginId.ANDROID_APPLICATION) version GradlePluginVersions.ANDROID_GRADLE apply false
    id(GradlePluginId.SAFE_ARGS) version GradlePluginVersions.SAFE_ARGS apply false
    kotlin(GradlePluginId.KOTLIN_ANDROID) version GradlePluginVersions.KOTLIN apply false

    // apply plugins
    id(GradlePluginId.DETEKT) version GradlePluginVersions.DETEKT
    id(GradlePluginId.GRADLE_VERSIONS) version GradlePluginVersions.GRADLE_VERSIONS
}

subprojects {
    apply(plugin = GradlePluginId.DETEKT)
    detekt {
        config = rootProject.files("detekt/config.yml")
        baseline = rootProject.file("detekt/baseline.xml")
        autoCorrect = true
        parallel = true
        reports {
            xml {
                enabled = true
                destination = rootProject.file("build/reports/detekt.xml")
            }
        }
    }

    tasks.withType<Detekt>().configureEach {
        jvmTarget = "1.8"
    }

    dependencies {
        detektPlugins(DetektDependency.DETEKT_FORMATTING)
    }

    repositories {
        google()
        mavenCentral()
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }
    apply { from("$rootDir/gradle/version.gradle.kts") }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.FlowPreview"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.InternalCoroutinesApi"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    }
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}
