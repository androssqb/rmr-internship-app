package com.redmadrobot.data.prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import com.redmadrobot.domain.model.Token
import com.redmadrobot.domain.providers.PreferencesProvider
import com.redmadrobot.extensions.core.content.remove
import com.redmadrobot.extensions.core.content.string
import javax.inject.Inject

class UserPreferences @Inject constructor(private val preferences: SharedPreferences) : PreferencesProvider {

    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val USER_ID = "user_id"
    }

    private var accessToken by preferences.string(ACCESS_TOKEN)
    private var refreshToken by preferences.string(REFRESH_TOKEN)
    private var userId by preferences.string(USER_ID)

    override fun saveToken(token: Token) {
        accessToken = token.accessToken
        refreshToken = token.refreshToken
    }

    override fun clearToken() {
        preferences.edit {
            remove(ACCESS_TOKEN, REFRESH_TOKEN)
        }
    }

    override fun fetchAccessToken() = accessToken
    override fun fetchRefreshToken() = refreshToken

    override fun saveUserId(id: String) {
        userId = id
    }

    override fun fetchUserId() = userId
}
