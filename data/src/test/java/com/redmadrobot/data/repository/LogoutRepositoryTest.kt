package com.redmadrobot.data.repository

import com.redmadrobot.data.model.DeAuthorizationType
import com.redmadrobot.data.network.LogoutApi
import com.redmadrobot.data.providers.TestDispatcherProvider
import com.redmadrobot.data.providers.TestPreferencesProvider
import com.redmadrobot.data.security.Encrypter
import com.redmadrobot.domain.model.Token
import com.redmadrobot.domain.providers.PreferencesProvider
import com.redmadrobot.mapmemory.MapMemory
import com.redmadrobot.mapmemory.mapMemoryOf
import com.redmadrobot.test.extension.*
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestCoroutineScope
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class LogoutRepositoryTest : FreeSpec({

    Feature("Пользователь заканчивает сессию в приложении") {
        lateinit var testCoroutineScope: TestCoroutineScope
        lateinit var prefs: PreferencesProvider
        lateinit var memory: MapMemory
        lateinit var api: LogoutApi
        lateinit var encrypter: Encrypter

        // SUT
        lateinit var repository: LogoutRepository

        val fakeToken = Token(accessToken = "fake_access", refreshToken = "fake_refresh")
        val fakeData = "Some data"
        val fakeKey = "some_key"

        var logoutEventResult: DeAuthorizationType? = null
        var logoutResult: Unit? = null

        beforeEachScenario {
            testCoroutineScope = TestCoroutineScope()
            prefs = TestPreferencesProvider()
            memory = mapMemoryOf(fakeKey to fakeData)
            api = mockk(relaxed = true)
            encrypter = mockk(relaxed = true)
            repository = LogoutRepository(api, prefs, TestDispatcherProvider(), memory, encrypter)

            prefs.saveToken(fakeToken)
        }

        afterEachScenario {
            testCoroutineScope.cleanupTestCoroutines()
            logoutEventResult = null
            logoutResult = null
        }

        Scenario("Вышел срок refresh token") {

            Given("Подписываемся на событие деавторизации") {
                repository.deAuthorization
                    .onEach { logoutEventResult = it }
                    .launchIn(testCoroutineScope)
            }

            When("Отрабатывает метод clearData") {
                repository.clearData(DeAuthorizationType.REFRESH_FAILED)
            }

            Then("Access и Refresh токены удалены") {
                assertSoftly {
                    prefs.fetchAccessToken().shouldBeEmpty()
                    prefs.fetchRefreshToken().shouldBeEmpty()
                }
            }

            And("In-memory cache пустой") {
                memory.shouldBeEmpty()
            }

            And("Событие деавторизации отправлено") {
                logoutEventResult shouldBe DeAuthorizationType.REFRESH_FAILED
            }
        }

        Scenario("Пользовать нажал кнопку [Выйти] и получил успеший ответ от сервера") {

            Given("Код ответа сервера 2хх") {
                coEvery { api.logoutUser() } returns Response.success(Unit)
            }

            When("Подписываемся на событие деавторизации") {
                repository.deAuthorization
                    .onEach { logoutEventResult = it }
                    .launchIn(testCoroutineScope)
            }

            And("Отрабатывает метод logout") {
                repository.logout()
                    .onEach { logoutResult = it }
                    .launchIn(this)
            }

            Then("Access и Refresh токены удалены") {
                assertSoftly {
                    prefs.fetchAccessToken().shouldBeEmpty()
                    prefs.fetchRefreshToken().shouldBeEmpty()
                }
            }

            And("In-memory cache пустой") {
                memory.shouldBeEmpty()
            }

            And("Событие деавторизации отправлено") {
                assertSoftly {
                    logoutEventResult shouldBe DeAuthorizationType.LOGOUT_SUCCESS
                    logoutResult shouldBe Unit
                }
            }
        }

        Scenario("Пользовать нажал кнопку [Выйти] и получил ошибку от сервера") {

            Given("Код ответа сервера 4хх - 5хх") {
                coEvery { api.logoutUser() } returns
                    Response.error(404, "Test Server Error".toResponseBody("text/plain".toMediaTypeOrNull()))
            }

            When("Подписываемся на событие деавторизации") {
                repository.deAuthorization
                    .onEach { logoutEventResult = it }
                    .launchIn(testCoroutineScope)
            }

            And("Отрабатывает метод logout") {
                repository.logout()
                    .onEach { logoutResult = it }
                    .launchIn(this)
            }

            Then("Access и Refresh токены удалены") {
                assertSoftly {
                    prefs.fetchAccessToken().shouldBeEmpty()
                    prefs.fetchRefreshToken().shouldBeEmpty()
                }
            }

            And("In-memory cache пустой") {
                memory.shouldBeEmpty()
            }

            And("Событие деавторизации отправлено") {
                logoutEventResult shouldBe DeAuthorizationType.LOGOUT_SUCCESS
            }
        }
    }
})
