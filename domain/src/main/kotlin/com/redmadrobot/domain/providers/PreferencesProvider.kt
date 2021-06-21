package com.redmadrobot.domain.providers

import com.redmadrobot.domain.model.Token

interface PreferencesProvider {

    fun saveToken(token: Token)

    fun clearToken()

    fun fetchAccessToken(): String

    fun fetchRefreshToken(): String

    fun saveUserId(id: String)

    fun fetchUserId(): String
}
