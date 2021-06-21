package com.redmadrobot.app.utils

import com.redmadrobot.app.model.FieldState
import com.redmadrobot.test.extension.*
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.TestCoroutineScope

class FieldObserverTest : FreeSpec({

    Feature("Авто проверка текста после ввода") {
        val validators = Validators()

        Scenario("По окончании ввода символов в текстовое поле происходит проверка с задержкой в 1.5 секунды") {
            // SUT
            lateinit var emailValidator: FieldValidator
            lateinit var emailResult: FieldState
            val testCoroutineScope = TestCoroutineScope()

            afterCurrentScenario { testCoroutineScope.cleanupTestCoroutines() }

            Given("проверку обеспечивает один из валидаторов") {
                emailValidator = validators.emailValidator
            }

            When("подписываемся на соответствующий валидатор") {
                emailValidator.observeFieldState()
                    .take(1)
                    .onEach { emailResult = it }
                    .launchIn(testCoroutineScope)
            }

            And("получаем значение из текстового поля") {
                emailValidator.setText(text = "mail.com@ru")
            }

            Then("по истечении задержки в 1.5 секунды") {
                val time = testCoroutineScope.advanceUntilIdle()
                time shouldBe 1_500L
            }

            And("получаем результат") {
                emailResult.isValid.shouldBeFalse()
            }
        }
    }
})
