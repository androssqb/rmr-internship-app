package com.redmadrobot.app.utils.converters

import com.redmadrobot.app.R
import com.redmadrobot.domain.providers.ResourceProvider
import kotlinx.datetime.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataConverter @Inject constructor(
    private val clock: Clock,
    private val resourceProvider: ResourceProvider,
) {

    fun convertToUserAge(userBirthDate: String): String {
        val currentDate = clock.todayAt(TimeZone.UTC)
        val birthDate = userBirthDate.toLocalDate()
        val age = currentDate.minus(birthDate).years
        return resourceProvider.getQuantityString(R.plurals.user_age, age, age)
    }

    fun convertToFullName(firstName: String, lastName: String): String {
        return "$firstName $lastName"
    }

    fun convertToNickname(nickname: String?): String {
        return nickname?.let { "@$it" }.orEmpty()
    }
}
