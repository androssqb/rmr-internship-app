plugins {
    kotlin("jvm")
}
dependencies {
    implementation(DomainDependency.KOTLIN)

    api(TestDependency.KOTEST_RUNNER)
    api(TestDependency.KOTEST_ASSERTIONS_CORE)
    api(TestDependency.KOTEST_PROPERTY)
    api(TestDependency.KOTEST_DATATEST)
    api(TestDependency.MOCKK)
    api(TestDependency.MEMORY_TEST)
    api(TestDependency.COROUTINES)
}
