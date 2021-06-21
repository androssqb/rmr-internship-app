plugins {
    id(GradlePluginId.ANDROID_LIBRARY)
    kotlin(GradlePluginId.KOTLIN_ANDROID)
    kotlin(GradlePluginId.KAPT)
}

android {
    defaultConfig {
        minSdkVersion(AndroidConfigVersions.MIN_SDK_VERSION)
        targetSdkVersion(AndroidConfigVersions.TARGET_SDK_VERSION)
        compileSdkVersion(AndroidConfigVersions.COMPILE_SDK_VERSION)

        versionCode = extra.get("version_code") as Int
        versionName = extra.get("version_name") as String
    }

    buildTypes {

        val proguardFiles = rootProject.fileTree("proguard").files +
            getDefaultProguardFile("proguard-android-optimize.txt")

        getByName(BuildTypes.DEBUG) {
            isDebuggable = true

            isMinifyEnabled = false

            proguardFiles(*proguardFiles.toTypedArray())
        }

        getByName(BuildTypes.RELEASE) {
            isDebuggable = false

            isMinifyEnabled = true

            proguardFiles(*proguardFiles.toTypedArray())
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    sourceSets.forEach { it.java.srcDir("src/${it.name}/kotlin") }
}

dependencies {
    api(DomainDependency.KOTLIN)
    api(DomainDependency.COROUTINES)

    api(DomainDependency.DAGGER)

    api(DomainDependency.MOSHI)
    api(DomainDependency.MOSHI_ADAPTERS)

    api(DomainDependency.RETROFIT)
    api(DomainDependency.RETROFIT_MOSHI)

    api(DataDependency.ROOM)
    api(DomainDependency.TIMBER)
    api(DomainDependency.LOGGER)

    kapt(DomainDependency.MOSHI_COMPILER)
}
