package com.redmadrobot.test.listeners

import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult

class EachFeatureTestListener(
    private val beforeEachFeature: (() -> Unit)? = null,
    private val afterEachFeature: (() -> Unit)? = null,
) : TestListener {

    private fun TestCase.isFeature() = displayName.startsWith("Feature: ")
    override suspend fun beforeContainer(testCase: TestCase) {
        if (beforeEachFeature != null && testCase.isFeature()) beforeEachFeature.invoke()
    }

    override suspend fun afterContainer(testCase: TestCase, result: TestResult) {
        if (afterEachFeature != null && testCase.isFeature()) afterEachFeature.invoke()
    }
}
