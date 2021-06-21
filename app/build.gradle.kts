import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.util.*

plugins {
    id(GradlePluginId.ANDROID_APPLICATION)
    id(GradlePluginId.SAFE_ARGS)
    kotlin(GradlePluginId.KOTLIN_ANDROID)
    kotlin(GradlePluginId.KAPT)
    id(GradlePluginId.PARCELIZE)
}

android {

    useLibrary("android.test.runner")
    useLibrary("android.test.base")
    useLibrary("android.test.mock")

    testBuildType = BuildTypes.DEBUG

    buildFeatures {
        viewBinding = true
    }

    lintOptions {
        isCheckDependencies = true
        isIgnoreWarnings = true
        xmlReport = true
        xmlOutput = file("${project.rootDir}/build/reports/android-lint.xml")
        htmlReport = true
        htmlOutput = file("${project.rootDir}/build/reports/android-lint.html")
        baselineFile = file("${project.rootDir}/lint/baseline.xml")
        lintConfig = file("${project.rootDir}/lint/lint.xml")
    }

    defaultConfig {
        applicationId = "com.workplaces.rossinevich"

        minSdkVersion(AndroidConfigVersions.MIN_SDK_VERSION)
        targetSdkVersion(AndroidConfigVersions.TARGET_SDK_VERSION)
        compileSdkVersion(AndroidConfigVersions.COMPILE_SDK_VERSION)

        versionCode = extra.get("version_code") as Int
        versionName = extra.get("version_name") as String

        resConfigs("ru")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName(BuildTypes.DEBUG) {
            val debugStoreFile = file("../cert/debug.keystore")
            val debugPropsFile = file("../cert/debug.properties")
            val debugProps = Properties().apply {
                load(debugPropsFile.inputStream())
            }

            storeFile = debugStoreFile
            keyAlias = debugProps.getProperty(SigningProperties.ALIAS_PROP)
            keyPassword = debugProps.getProperty(SigningProperties.KEY_PASSWORD_PROP)
            storePassword = debugProps.getProperty(SigningProperties.STORE_PASSWORD_PROP)
        }

        register(BuildTypes.RELEASE) {
            val keystorePropertiesFile = rootProject.file("keystore.properties")
            val keystoreProperties = Properties()
            if (keystorePropertiesFile.exists()) {
                keystoreProperties.load(keystorePropertiesFile.inputStream())

                storeFile = file(keystoreProperties.getProperty("RELEASE_STORE_FILE"))
                storePassword = keystoreProperties.getProperty("RELEASE_STORE_PASS")
                keyPassword = keystoreProperties.getProperty("RELEASE_KEY_PASS")
                keyAlias = keystoreProperties.getProperty("RELEASE_ALIAS")
            }
        }
    }

    buildTypes {

        val proguardFiles = rootProject.fileTree("proguard").files +
            getDefaultProguardFile("proguard-android-optimize.txt")

        getByName(BuildTypes.DEBUG) {
            applicationIdSuffix = ".debug"

            isDebuggable = true

            isMinifyEnabled = false
            isShrinkResources = false

            proguardFiles(*proguardFiles.toTypedArray())
            signingConfig = signingConfigs.findByName("debug")
        }

        getByName(BuildTypes.RELEASE) {
            applicationIdSuffix = ""

            isDebuggable = false

            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(*proguardFiles.toTypedArray())
            signingConfig = signingConfigs.findByName("release")
        }
    }

    applicationVariants.all {
        val applicationId = mergedFlavor.applicationId
        val applicationIdSuffix = buildType.applicationIdSuffix

        outputs
            .map { it as BaseVariantOutputImpl }
            .forEach { output ->
                output.outputFileName =
                    "$applicationId$applicationIdSuffix-$versionName($versionCode).apk"
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

    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    testImplementation(project(":base-test"))

    implementation(AppDependency.CORE)
    implementation(AppDependency.APPCOMPAT)
    implementation(AppDependency.CONSTRAINT_LAYOUT)
    implementation(AppDependency.LIFECYCLE_VIEWMODEL)
    implementation(AppDependency.MATERIAL_UI)
    implementation(AppDependency.BROWSER)
    implementation(AppDependency.RMR_VIEW_BINDING)
    implementation(AppDependency.RMR_LIVEDATA)
    implementation(AppDependency.DATE_TIME)
    implementation(AppDependency.ACTIVITY)
    implementation(AppDependency.FRAGMENT)
    implementation(AppDependency.GOOGLE_MAPS)
    implementation(AppDependency.GOOGLE_PLACES)

    implementation(AppDependency.NAVIGATION_FRAGMENT)
    implementation(AppDependency.NAVIGATION_FRAGMENT_KTX)
    implementation(AppDependency.NAVIGATION_UI)
    implementation(AppDependency.NAVIGATION_UI_KTX)

    implementation(AppDependency.EPOXY)
    kapt(AppDependency.EPOXY_PROCESSOR)

    implementation(AppDependency.COIL)

    kapt(AppDependency.DAGGER_COMPILER)

    implementation(AppDependency.MOSHI)
    implementation(AppDependency.MOSHI_CONVERTER)
    implementation(AppDependency.LOGGING_INTERCEPTOR)

    implementation(AppDependency.MEMORY)
    implementation(AppDependency.MEMORY_COROUTINES)

    implementation(AppDependency.ROOTBEER)
    implementation(AppDependency.SECURITY_CRYPTO)

    testImplementation(TestDependency.JUNIT)
    testImplementation(TestDependency.ANDROIDX)
}
