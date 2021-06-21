private object TestVersions {
    const val JUNIT = "4.13.2"
    const val ANDROIDX = "2.1.0"
    const val KOTEST = "4.6.0"
    const val MOCKK = "1.10.6"
    const val COROUTINES = "1.5.0"
    const val MEMORY_TEST = "2.0"
}

object TestDependency {
    const val JUNIT = "junit:junit:${TestVersions.JUNIT}"
    const val ANDROIDX = "androidx.arch.core:core-testing:${TestVersions.ANDROIDX}"

    const val KOTEST_RUNNER = "io.kotest:kotest-runner-junit5-jvm:${TestVersions.KOTEST}"
    const val KOTEST_ASSERTIONS_CORE = "io.kotest:kotest-assertions-core-jvm:${TestVersions.KOTEST}"
    const val KOTEST_PROPERTY = "io.kotest:kotest-property-jvm:${TestVersions.KOTEST}"
    const val KOTEST_DATATEST = "io.kotest:kotest-framework-datatest-jvm:${TestVersions.KOTEST}"

    const val MOCKK = "io.mockk:mockk:${TestVersions.MOCKK}"

    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${TestVersions.COROUTINES}"

    const val MEMORY_TEST = "com.redmadrobot.mapmemory:mapmemory-test:${TestVersions.MEMORY_TEST}"
}
