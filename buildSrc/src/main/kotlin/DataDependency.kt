private object DataVersions {
    const val OKHTTP_LOGGING = "3.1.0"
    const val MAP_MEMORY = "2.0"
    const val CORE = "1.3.2-0"
    const val ROOM = "2.3.0"
}

object DataDependency {
    const val OKHTTP_LOGGING = "com.github.ihsanbal:LoggingInterceptor:${DataVersions.OKHTTP_LOGGING}"

    const val DAGGER_COMPILER = "com.google.dagger:dagger-compiler:${CoreVersions.DAGGER}"
    const val MOSHI_COMPILER = "com.squareup.moshi:moshi-kotlin-codegen:${CoreVersions.MOSHI}"
    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${CoreVersions.COROUTINES}"

    const val MEMORY = "com.redmadrobot.mapmemory:mapmemory:${DataVersions.MAP_MEMORY}"
    const val MEMORY_COROUTINES = "com.redmadrobot.mapmemory:mapmemory-coroutines:${DataVersions.MAP_MEMORY}"

    const val RMR_SHARED_PREFERENCES = "com.redmadrobot.extensions:core-ktx:${DataVersions.CORE}"
    const val SECURITY_CRYPTO = "androidx.security:security-crypto:${AppVersions.SECURITY_CRYPTO}"

    const val ROOM_COMPILER = "androidx.room:room-compiler:${DataVersions.ROOM}"
    const val ROOM_COROUTINES = "androidx.room:room-ktx:${DataVersions.ROOM}"
    const val ROOM = "androidx.room:room-runtime:${DataVersions.ROOM}"
}
