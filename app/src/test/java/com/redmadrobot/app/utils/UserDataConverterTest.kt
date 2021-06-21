package com.redmadrobot.app.utils

import com.redmadrobot.app.FixedClock
import com.redmadrobot.app.R
import com.redmadrobot.app.providers.TestResourceProvider
import com.redmadrobot.app.utils.converters.UserDataConverter
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

@ExperimentalKotest
class UserDataConverterTest : FunSpec({

    val clock = FixedClock("2021-01-01T00:00:00Z")
    val provider = TestResourceProvider()
    val converter = UserDataConverter(clock, provider)

    data class AgeConverterTest(val birthDay: String, val expectedResult: String)

    context("Вычисление возраста на основании даты рождения") {
        withData(
            AgeConverterTest("1990-03-10", "30 ${R.plurals.user_age}"),
            AgeConverterTest("1999-08-17", "21 ${R.plurals.user_age}"),
            AgeConverterTest("1988-11-22", "32 ${R.plurals.user_age}")
        ) { (birthDay: String, expectedResult: String) ->
            val result = converter.convertToUserAge(birthDay)
            result.shouldBe(expectedResult)
        }
    }
})
