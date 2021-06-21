internal object AppVersions {
    // UI
    const val CORE = "1.3.0"
    const val APPCOMPAT = "1.2.0"
    const val BROWSER = "1.3.0"
    const val CONSTRAINT_LAYOUT = "2.0.4"
    const val LIFECYCLE = "2.3.1"
    const val MATERIAL_UI = "1.3.0"
    const val RMR_VIEW_BINDING = "4.1.2-2"
    const val RMR_LIVEDATA = "2.3.0-0"
    const val NAVIGATION = "2.4.0-alpha02"
//    const val NAVIGATION = "2.3.5"
    const val RETROFIT = "2.9.0"
    const val LOGGING = "4.9.1"
    const val MAP_MEMORY = "2.0"
    const val DATE_TIME = "0.2.0"
    const val EPOXY = "4.6.1"
    const val COIL = "1.2.1"
    const val ROOTBEER = "0.0.9"
    const val SECURITY_CRYPTO = "1.0.0"
    const val ACTIVITY_KTX = "1.3.0-beta01"
    const val FRAGMENT_KTX = "1.3.4"
    const val GOOGLE_MAPS = "17.0.1"
    const val GOOGLE_PLACES = "2.4.0"
}

object AppDependency {
    // UI
    const val CORE = "androidx.core:core-ktx:${AppVersions.CORE}"
    const val APPCOMPAT = "androidx.appcompat:appcompat:${AppVersions.APPCOMPAT}"
    const val BROWSER = "androidx.browser:browser:${AppVersions.BROWSER}"
    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${AppVersions.CONSTRAINT_LAYOUT}"
    const val LIFECYCLE_VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:${AppVersions.LIFECYCLE}"
    const val MATERIAL_UI = "com.google.android.material:material:${AppVersions.MATERIAL_UI}"
    const val RMR_VIEW_BINDING = "com.redmadrobot.extensions:viewbinding-ktx:${AppVersions.RMR_VIEW_BINDING}"
    const val RMR_LIVEDATA = "com.redmadrobot.extensions:lifecycle-livedata-ktx:${AppVersions.RMR_LIVEDATA}"

    // NAVIGATION
    const val NAVIGATION_FRAGMENT =
        "androidx.navigation:navigation-fragment:${AppVersions.NAVIGATION}"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui:${AppVersions.NAVIGATION}"
    const val NAVIGATION_FRAGMENT_KTX =
        "androidx.navigation:navigation-fragment-ktx:${AppVersions.NAVIGATION}"
    const val NAVIGATION_UI_KTX = "androidx.navigation:navigation-ui-ktx:${AppVersions.NAVIGATION}"

    // DAGGER
    const val DAGGER_COMPILER = "com.google.dagger:dagger-compiler:${CoreVersions.DAGGER}"

    // MOSHI
    const val MOSHI = "com.squareup.moshi:moshi-kotlin:${CoreVersions.MOSHI}"
    const val MOSHI_CONVERTER = "com.squareup.retrofit2:converter-moshi:${AppVersions.RETROFIT}"

    // HttpLoggingInterceptor
    const val LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:${AppVersions.LOGGING}"

    const val MEMORY = "com.redmadrobot.mapmemory:mapmemory:${AppVersions.MAP_MEMORY}"
    const val MEMORY_COROUTINES = "com.redmadrobot.mapmemory:mapmemory-coroutines:${AppVersions.MAP_MEMORY}"

    const val DATE_TIME = "org.jetbrains.kotlinx:kotlinx-datetime:${AppVersions.DATE_TIME}"

    // Epoxy
    const val EPOXY = "com.airbnb.android:epoxy:${AppVersions.EPOXY}"
    const val EPOXY_PROCESSOR = "com.airbnb.android:epoxy-processor:${AppVersions.EPOXY}"

    // Coil
    const val COIL = "io.coil-kt:coil:${AppVersions.COIL}"

    // Rootbeer
    const val ROOTBEER = "com.scottyab:rootbeer-lib:${AppVersions.ROOTBEER}"

    const val SECURITY_CRYPTO = "androidx.security:security-crypto:${AppVersions.SECURITY_CRYPTO}"

    // Activity Result
    const val ACTIVITY = "androidx.activity:activity-ktx:${AppVersions.ACTIVITY_KTX}"
    const val FRAGMENT = "androidx.fragment:fragment-ktx:${AppVersions.FRAGMENT_KTX}"

    const val GOOGLE_MAPS = "com.google.android.gms:play-services-maps:${AppVersions.GOOGLE_MAPS}"
    const val GOOGLE_PLACES = "com.google.android.libraries.places:places:${AppVersions.GOOGLE_PLACES}"
}
