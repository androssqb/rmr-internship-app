package com.redmadrobot.app.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.redmadrobot.app.ui.base.viewmodel.BaseViewModel
import com.redmadrobot.app.ui.base.viewmodel.Navigate
import com.redmadrobot.app.ui.base.viewmodel.ShowMessageEvent
import com.redmadrobot.app.utils.converters.ErrorConverter
import com.redmadrobot.app.utils.converters.UserDataConverter
import com.redmadrobot.data.model.DeAuthorizationType
import com.redmadrobot.data.repository.LogoutRepository
import com.redmadrobot.data.repository.UserRepository
import com.redmadrobot.data.security.Encrypter
import com.redmadrobot.extensions.lifecycle.mapDistinct
import com.redmadrobot.extensions.lifecycle.provideDelegate
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val logoutRepository: LogoutRepository,
    private val userDataConverter: UserDataConverter,
    private val error: ErrorConverter,
    private val encrypter: Encrypter,
) : BaseViewModel() {

    val profileViewState: MutableLiveData<ProfileViewState> = MutableLiveData(createInitialState())
    val isLoading = profileViewState.mapDistinct { it.isLoading }
    private var state: ProfileViewState by profileViewState

    init {
        if (userRepository.fileName.replayCache.isEmpty()) {
            fetchUserProfile()
        }

        userRepository.fileName
            .map { fileName -> encrypter.decryptProfile(fileName = fileName) }
            .onEach { profile ->
                if (profile != null) {
                    val nickname = userDataConverter.convertToNickname(nickname = profile.nickname)
                    val name = userDataConverter
                        .convertToFullName(firstName = profile.firstName, lastName = profile.lastName)
                    val age = userDataConverter.convertToUserAge(userBirthDate = profile.birthDay)
                    state = state.copy(nickname = nickname, fullName = name, age = age)
                } else {
                    logoutRepository.clearData(DeAuthorizationType.NO_USER_DATA_FILE)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun fetchUserProfile() {
        userRepository.fetchUserProfileFromServer()
            .onStart { state = state.copy(isLoading = true) }
            .onCompletion { state = state.copy(isLoading = false) }
            .catch { throwable: Throwable ->
                val message = error.message(throwable)
                eventsQueue.offerEvent(ShowMessageEvent(textMessage = message))
            }
            .launchIn(viewModelScope)
    }

    fun onPatchUserClicked() {
        val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
        eventsQueue.offerEvent(Navigate(action))
    }

    fun onLogoutClicked() {
        logoutRepository.logout()
            .launchIn(viewModelScope)
    }

    private fun createInitialState(): ProfileViewState {
        return ProfileViewState(
            nickname = "",
            fullName = "",
            age = "",
            isLoading = false
        )
    }

    data class ProfileViewState(
        val nickname: String,
        val fullName: String,
        val age: String,
        val isLoading: Boolean,
    )
}
