package com.redmadrobot.test.listeners

import io.kotest.core.listeners.TestListener
import io.kotest.core.test.Description
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult

class CurrentScenarioTestListener(
    private val currentScenarioDescription: Description,
    private val beforeCurrentScenario: (() -> Unit)? = null,
    private val afterCurrentScenario: (() -> Unit)? = null
) : TestListener {
    private var isBeforeCurrentScenarioConsumed = false
    private fun TestCase.isChildOfExpectedScenario() = description.parent == currentScenarioDescription
    private fun TestCase.isExpectedScenario() = description == currentScenarioDescription
    override suspend fun beforeTest(testCase: TestCase) {
        if (beforeCurrentScenario != null && testCase.isChildOfExpectedScenario() && !isBeforeCurrentScenarioConsumed) {
            isBeforeCurrentScenarioConsumed = true
            beforeCurrentScenario.invoke()
        }
    }
    override suspend fun afterContainer(testCase: TestCase, result: TestResult) {
        if (afterCurrentScenario != null && testCase.isExpectedScenario()) afterCurrentScenario.invoke()
    }
}
