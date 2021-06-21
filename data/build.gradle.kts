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

    testOptions {
        unitTests.isReturnDefaultValues = true
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
    implementation(project(":domain"))
    testImplementation(project(":base-test"))

    implementation(DataDependency.OKHTTP_LOGGING) {
        exclude("org.json", "json")
    }

    implementation(DataDependency.COROUTINES)
    implementation(DataDependency.ROOM_COROUTINES)
//    api(DataDependency.ROOM)

    kapt(DataDependency.DAGGER_COMPILER)
    kapt(DataDependency.MOSHI_COMPILER)
    kapt(DataDependency.ROOM_COMPILER)

    implementation(DataDependency.MEMORY)
    implementation(DataDependency.MEMORY_COROUTINES)
    implementation(DataDependency.RMR_SHARED_PREFERENCES)
    implementation(DataDependency.SECURITY_CRYPTO)

    testImplementation(TestDependency.JUNIT)
    kaptTest(DataDependency.MOSHI_COMPILER)
}
