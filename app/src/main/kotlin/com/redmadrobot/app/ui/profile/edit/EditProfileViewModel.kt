package com.redmadrobot.app.ui.profile.edit

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.redmadrobot.app.R
import com.redmadrobot.app.model.FieldState
import com.redmadrobot.app.ui.base.viewmodel.*
import com.redmadrobot.app.utils.FieldValidator
import com.redmadrobot.app.utils.Validators
import com.redmadrobot.app.utils.converters.ErrorConverter
import com.redmadrobot.data.model.DeAuthorizationType
import com.redmadrobot.data.repository.LogoutRepository
import com.redmadrobot.data.repository.UserRepository
import com.redmadrobot.data.security.Encrypter
import com.redmadrobot.domain.model.UserProfile
import com.redmadrobot.extensions.lifecycle.mapDistinct
import com.redmadrobot.extensions.lifecycle.provideDelegate
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@Suppress("TooManyFunctions")
class EditProfileViewModel @Inject constructor(
    private val repository: UserRepository,
    private val validators: Validators,
    private val error: ErrorConverter,
    private val encrypter: Encrypter,
    private val logoutRepository: LogoutRepository,
) : BaseViewModel() {

    private var user: UserProfile? = null

    private val editProfileViewState: MutableLiveData<EditProfileViewState> = MutableLiveData(createInitialState())
    val currentUserData = editProfileViewState.mapDistinct { it.currentProfileData }
    val isLoading = editProfileViewState.mapDistinct { it.isLoading }
    val isSaveButtonEnabled = editProfileViewState.mapDistinct {
        it.nickname.isValid && it.firstName.isValid &&
            it.secondName.isValid && it.birthDate.isValid && profileDataChanged()
    }
    private var state: EditProfileViewState by editProfileViewState

    init {
        with(validators) {
            nicknameValidator.collect(R.string.sign_up_second_nickname_error) { state.copy(nickname = it) }
            firstNameValidator.collect(R.string.sign_up_second_name_error) { state.copy(firstName = it) }
            secondNameValidator.collect(R.string.sign_up_second_name_error) { state.copy(secondName = it) }
            dateValidator.collect(R.string.sign_up_second_date_error) { state.copy(birthDate = it) }
        }

        repository.fileName
            .map { fileName -> encrypter.decryptProfile(fileName = fileName) }
            .onEach { userProfile ->
                if (userProfile != null) {
                    user = userProfile
                    updateInitialState(profile = userProfile)
                } else {
                    logoutRepository.clearData(DeAuthorizationType.NO_USER_DATA_FILE)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSaveClicked(
        nickname: String,
        firstName: String,
        secondName: String,
        birthDate: String
    ) {
        val isUserProfileValid = with(validators) {
            nicknameValidator.validateField(text = nickname) &&
                firstNameValidator.validateField(text = firstName) &&
                secondNameValidator.validateField(text = secondName) &&
                dateValidator.validateField(text = birthDate)
        }
        if (isUserProfileValid) {
            repository.updateUserProfile(
                firstName = firstName,
                secondName = secondName,
                nickname = nickname,
                birthDate = birthDate
            )
                .onStart { state = state.copy(isLoading = true) }
                .onCompletion { state = state.copy(isLoading = false) }
                .onEach { eventsQueue.offerEvent(NavigateUp) }
                .catch { throwable: Throwable ->
                    val message = error.message(throwable)
                    eventsQueue.offerEvent(ShowMessageEvent(textMessage = message))
                }
                .launchIn(viewModelScope)
        }
    }

    private inline fun FieldValidator.collect(
        @StringRes errorRes: Int,
        crossinline getState: (FieldState) -> EditProfileViewState,
    ) {
        observeFieldState()
            .onEach { fieldState ->
                state = getState(fieldState)
                if (!fieldState.isValid && fieldState.value.isNotEmpty()) {
                    eventsQueue.offerEvent(ShowMessageEvent(errorRes))
                }
            }
            .launchIn(viewModelScope)
    }

    fun onNicknameTextChanged(nickname: String) {
        validators.nicknameValidator.setText(text = nickname)
    }

    fun onFirstNameTextChanged(name: String) {
        validators.firstNameValidator.setText(text = name)
    }

    fun onSecondNameTextChanged(surname: String) {
        validators.secondNameValidator.setText(text = surname)
    }

    fun onDateTextChanged(date: String) {
        validators.dateValidator.setText(text = date)
    }

    fun onToolbarBackClicked() {
        eventsQueue.offerEvent(NavigateUp)
    }

    fun onCalendarIconClicked() {
        if (state.isBirthDayFieldEndIconClickable) {
            state = state.copy(isBirthDayFieldEndIconClickable = false)
            eventsQueue.offerEvent(ShowDatePikerEvent)
        }
    }

    fun onDatePickerDismiss() {
        state = state.copy(isBirthDayFieldEndIconClickable = true)
    }

    fun onDatePikerPositiveClicked(date: String) {
        eventsQueue.offerEvent(SetSelectedDateEvent(date))
    }

    private fun profileDataChanged(): Boolean {
        return user?.nickname != state.nickname.value ||
            user?.firstName != state.firstName.value ||
            user?.lastName != state.secondName.value ||
            user?.birthDay != state.birthDate.value
    }

    private fun updateInitialState(profile: UserProfile) {
        val nicknameFieldState = FieldState(value = profile.nickname.orEmpty(), isValid = true)
        val firstNameFieldState = FieldState(value = profile.firstName, isValid = true)
        val secondNameFieldState = FieldState(value = profile.lastName, isValid = true)
        val birthDateFieldState = FieldState(value = profile.birthDay, isValid = true)
        state = state.copy(
            nickname = nicknameFieldState,
            firstName = firstNameFieldState,
            secondName = secondNameFieldState,
            birthDate = birthDateFieldState,
            currentProfileData = profile
        )
    }

    private fun createInitialState(): EditProfileViewState {
        return EditProfileViewState(
            nickname = FieldState(value = "", isValid = false),
            firstName = FieldState(value = "", isValid = false),
            secondName = FieldState(value = "", isValid = false),
            birthDate = FieldState(value = "", isValid = false),
            currentProfileData = UserProfile(
                id = "",
                firstName = "",
                lastName = "",
                avatarUrl = null,
                birthDay = "",
                nickname = null
            ),
            isLoading = false,
            isBirthDayFieldEndIconClickable = true
        )
    }

    data class EditProfileViewState(
        val nickname: FieldState,
        val firstName: FieldState,
        val secondName: FieldState,
        val birthDate: FieldState,
        val currentProfileData: UserProfile,
        val isLoading: Boolean,
        val isBirthDayFieldEndIconClickable: Boolean,
    )
}
