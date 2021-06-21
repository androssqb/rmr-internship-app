package com.redmadrobot.app

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class FixedClock(private val testDate: String) : Clock {

    override fun now(): Instant {
        return Instant.parse(testDate)
    }
}
