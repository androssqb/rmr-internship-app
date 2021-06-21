package com.redmadrobot.app.ui.signupsecond

import com.redmadrobot.app.TestLiveDataExecutionController
import com.redmadrobot.app.providers.TestResourceProvider
import com.redmadrobot.app.ui.base.viewmodel.ShowMessageEvent
import com.redmadrobot.app.utils.Validators
import com.redmadrobot.app.utils.converters.ErrorConverter
import com.redmadrobot.data.model.ServerException
import com.redmadrobot.domain.model.Error
import com.redmadrobot.domain.usecases.AuthUseCase
import com.redmadrobot.extensions.lifecycle.Event
import com.redmadrobot.test.extension.*
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.properties.Delegates

class SignUpSecondViewModelTest : FreeSpec({

    beforeSpec { TestLiveDataExecutionController.enableTestMode() }
    afterSpec { TestLiveDataExecutionController.disableTestMode() }

    Feature("Регистрация пользователя") {

        lateinit var testDispatcher: TestCoroutineDispatcher
        lateinit var auth: AuthUseCase

        // SUT
        lateinit var viewModel: SignUpSecondViewModel

        beforeEachScenario {
            testDispatcher = TestCoroutineDispatcher()
            Dispatchers.setMain(testDispatcher)
            auth = mockk(relaxed = true)

            viewModel = SignUpSecondViewModel(
                auth = auth,
                validators = Validators(),
                error = ErrorConverter(TestResourceProvider()),
                email = "test@test.com",
                password = "Test1234"
            )
        }

        afterEachScenario {
            testDispatcher.cleanupTestCoroutines()
            Dispatchers.resetMain()
        }

        Scenario("Пользователь заполнил все поля корректно") {
            var isButtonEnabled by Delegates.notNull<Boolean>()

            Given("Подписываемся на livedata, отвечающую за состояние кнопки [Зарегестрироваться]") {
                viewModel.checkInButtonEnabled.observeForever { result ->
                    isButtonEnabled = result
                }
            }

            When("Во все текстовые поля введены корректные данные, которые соответствуют заданным шаблонам") {
                viewModel.onNicknameTextChanged(nickname = "Nick")
                viewModel.onFirstNameTextChanged(name = "Andrey")
                viewModel.onSecondNameTextChanged(surname = "Rossinevich")
                viewModel.onDateTextChanged(date = "1990-03-10")
            }

            And("Дожидаемся 1.5 секунды, пока введённый текст обработается") {
                testDispatcher.advanceUntilIdle().shouldBe(1_500L)
            }

            Then("Кнопка становится активной") {
                isButtonEnabled.shouldBeTrue()
            }
        }

        Scenario("Пользователь заполнил не все поля, или хотя бы одно поле не прошло проверку") {
            var isButtonEnabled by Delegates.notNull<Boolean>()

            Given("Подписываемся на livedata, отвечающую за состояние кнопки [Зарегестрироваться]") {
                viewModel.checkInButtonEnabled.observeForever { result ->
                    isButtonEnabled = result
                }
            }

            When("В текстовое поле введены некорректные данные или оно не заполнено") {
                viewModel.onNicknameTextChanged(nickname = "&Nick")
                viewModel.onFirstNameTextChanged(name = "Andrey")
                viewModel.onDateTextChanged(date = "1990.03.10")
            }

            And("Дожидаемся 1.5 секунды, пока введённый текст обработается") {
                testDispatcher.advanceUntilIdle().shouldBe(1_500L)
            }

            Then("Кнопка неактивна") {
                isButtonEnabled.shouldBeFalse()
            }
        }

        Scenario("Пользователь нажал на кнопку [Зарегестрироваться] и получил ошибку") {
            val testMessage = "Test Server Error"
            val mockError = Error(message = testMessage, code = "TEST_CODE")
            every { auth.checkIn(any(), any()) } returns flow {
                delay(1)
                throw ServerException(mockError)
            }

            var testEvent: Event? = null
            var loading by Delegates.notNull<Boolean>()

            Given("Подписываемся на очередь эвентов") {
                viewModel.eventsQueue.observeForever { event -> testEvent = event }
            }

            And("Подписываемся на изменение состояния экрана, оповещающего от загрузке данных") {
                viewModel.isLoading.observeForever { isLoading -> loading = isLoading }
            }

            When("Пользователь нажимает кнопку [Зарегестрироваться]") {
                viewModel.onCheckInClicked("Nick", "Andrey", "Rossinevich", "1990-03-10")
            }

            Then("Состояние экрана меняется, пользователь видит анимацию ProgressLoader") {
                loading.shouldBeTrue()
            }

            And("Дожидаемся, пока запрос обработается") {
                testDispatcher.advanceUntilIdle()
            }

            And("Состояние экрана меняется, анимация ProgressLoader завершается") {
                loading.shouldBeFalse()
            }

            And("Пользователь видит Snackbar с собщением об ошибке") {
                assertSoftly {
                    testEvent.shouldBeInstanceOf<ShowMessageEvent>()
                    (testEvent as ShowMessageEvent).textMessage.shouldBe(testMessage)
                }
            }
        }
    }
})
