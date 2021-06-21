package com.redmadrobot.data.providers

import com.redmadrobot.domain.model.Token
import com.redmadrobot.domain.providers.PreferencesProvider

class TestPreferencesProvider : PreferencesProvider {

    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val USER_ID = "user_id"
    }

    private var accessToken = ACCESS_TOKEN
    private var refreshToken = REFRESH_TOKEN
    private var userId = USER_ID

    override fun saveToken(token: Token) {
        accessToken = token.accessToken
        refreshToken = token.refreshToken
    }

    override fun clearToken() {
        accessToken = ""
        refreshToken = ""
    }

    override fun fetchAccessToken() = accessToken
    override fun fetchRefreshToken() = refreshToken

    override fun saveUserId(id: String) {
        userId = id
    }

    override fun fetchUserId(): String  = userId
}
