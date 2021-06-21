package com.redmadrobot.app.utils

import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

@ExperimentalKotest
@Suppress("MaxLineLength")
class FieldValidatorTest : FunSpec({

    val validators = Validators()

    data class InputTextTest(val text: String, val expectedResult: Boolean)

    context("Пароль должен быть не короче 8 символов, состоять только из латинских символов и содержать хотя бы одну цифру, большую и маленькую букву.") {
        withData(
            InputTextTest("Uu12345667", true),
            InputTextTest("Uu167sfgtcfghds", true),
            InputTextTest("USDFSDGFSFGHSDu1", true),
            InputTextTest("Пароль99", false),
            InputTextTest("U", false),
            InputTextTest("Uu", false),
            InputTextTest("Uu1234566@", false),
            InputTextTest("Uu12 345 66", false),
            InputTextTest("uughjfcgjgjhghj", false),
            InputTextTest("SDGSDGHSDGFSDG", false),
            InputTextTest("%&^&^^%$%___fg we4", false)
        ) { (password: String, expectedResult: Boolean) ->
            val result = validators.passwordValidator.validateField(password)
            result shouldBe expectedResult
        }
    }

    context("Ник должен состоять только из латинских символов и цифр") {
        withData(
            InputTextTest("BeautifulNick99", true),
            InputTextTest("4574745799", true),
            InputTextTest("Nick", true),
            InputTextTest("nick", true),
            InputTextTest("nick456", true),
            InputTextTest("n ick4 56", false),
            InputTextTest("@dfggjh687", false),
            InputTextTest("@nick_687", false)
        ) { (nickname: String, expectedResult: Boolean) ->
            val result = validators.nicknameValidator.validateField(nickname)
            result shouldBe expectedResult
        }
    }

    context("Имя и фамилия должен состоять только из латинских символов") {
        withData(
            InputTextTest("Andrey", true),
            InputTextTest("Rossinevich", true),
            InputTextTest("@ndrey", false),
            InputTextTest("Andr ey", false),
            InputTextTest("Андрей", false),
            InputTextTest("Andrey90", false)
        ) { (name: String, expectedResult: Boolean) ->
            val nameResult = validators.firstNameValidator.validateField(name)
            val surnameResult = validators.secondNameValidator.validateField(name)
            nameResult shouldBe expectedResult
            surnameResult shouldBe expectedResult
        }
    }

    context("Дата должна быть введена в формате ГГГГ-ММ-ДД") {
        withData(
            InputTextTest("1990-03-10", true),
            InputTextTest("1990-03-5", true),
            InputTextTest("90-3-5", false),
            InputTextTest("10.03.1990", false),
            InputTextTest("10/03/1990", false),
            InputTextTest("4567456@%", false),
            InputTextTest("dgh#$%@%#", false)
        ) { (date: String, expectedResult: Boolean) ->
            val result = validators.dateValidator.validateField(date)
            result shouldBe expectedResult
        }
    }
})
