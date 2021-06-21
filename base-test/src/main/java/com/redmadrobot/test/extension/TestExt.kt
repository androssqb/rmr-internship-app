package com.redmadrobot.test.extension

import com.redmadrobot.test.listeners.CurrentScenarioTestListener
import com.redmadrobot.test.listeners.EachFeatureTestListener
import com.redmadrobot.test.listeners.EachScenarioTestListener
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.scopes.ContainerContext
import io.kotest.core.spec.style.scopes.FreeSpecContainerContext
import io.kotest.core.spec.style.scopes.FreeSpecRootContext
import io.kotest.core.spec.style.scopes.FreeSpecTerminalContext

// region Gherkin style
fun FreeSpecRootContext.Feature(name: String, test: suspend FreeSpecContainerContext.() -> Unit) {
    "Feature: $name" - test
}

suspend fun FreeSpecContainerContext.Scenario(name: String, test: suspend FreeSpecContainerContext.() -> Unit) {
    "Scenario: $name" - test
}

suspend fun FreeSpecContainerContext.Given(name: String, test: suspend FreeSpecTerminalContext.() -> Unit) {
    "Given: $name"(test)
}

suspend fun FreeSpecContainerContext.When(name: String, test: suspend FreeSpecTerminalContext.() -> Unit) {
    "When: $name"(test)
}

suspend fun FreeSpecContainerContext.Then(name: String, test: suspend FreeSpecTerminalContext.() -> Unit) {
    "Then: $name"(test)
}

suspend fun FreeSpecContainerContext.And(name: String, test: suspend FreeSpecTerminalContext.() -> Unit) {
    "And: $name"(test)
}
// endregion

// region Listeners
fun FreeSpec.beforeEachFeature(listener: () -> Unit) {
    listener(EachFeatureTestListener(beforeEachFeature = listener))
}

fun FreeSpec.afterEachFeature(listener: () -> Unit) {
    listener(EachFeatureTestListener(afterEachFeature = listener))
}

fun ContainerContext.beforeEachScenario(listener: () -> Unit) {
    testCase.spec.listener(
        EachScenarioTestListener(
            testCase.description,
            beforeEachScenario = listener
        )
    )
}

fun ContainerContext.afterEachScenario(listener: () -> Unit) {
    testCase.spec.listener(
        EachScenarioTestListener(
            testCase.description,
            afterEachScenario = listener
        )
    )
}

fun ContainerContext.beforeCurrentScenario(listener: () -> Unit) {
    testCase.spec.listener(
        CurrentScenarioTestListener(
            testCase.description,
            beforeCurrentScenario = listener
        )
    )
}

fun ContainerContext.afterCurrentScenario(listener: () -> Unit) {
    testCase.spec.listener(
        CurrentScenarioTestListener(
            testCase.description,
            afterCurrentScenario = listener
        )
    )
}
// endregion
