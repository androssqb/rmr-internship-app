package com.redmadrobot.test.listeners

import io.kotest.core.listeners.TestListener
import io.kotest.core.test.Description
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult

class EachScenarioTestListener(
    private val featureDescription: Description,
    private val beforeEachScenario: (() -> Unit)? = null,
    private val afterEachScenario: (() -> Unit)? = null
) : TestListener {
    private fun TestCase.isChildOfExpectedFeature() = description.parent == featureDescription
    private fun TestCase.isScenario() = displayName.startsWith("Scenario: ")
    private fun TestCase.shouldBeProcessed() = isChildOfExpectedFeature() && isScenario()
    override suspend fun beforeContainer(testCase: TestCase) {
        if (beforeEachScenario != null && testCase.shouldBeProcessed()) beforeEachScenario.invoke()
    }
    override suspend fun afterContainer(testCase: TestCase, result: TestResult) {
        if (afterEachScenario != null && testCase.shouldBeProcessed()) afterEachScenario.invoke()
    }
}
