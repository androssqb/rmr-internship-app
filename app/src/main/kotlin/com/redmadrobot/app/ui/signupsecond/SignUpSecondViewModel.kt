package com.redmadrobot.app.ui.signupsecond

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.redmadrobot.app.R
import com.redmadrobot.app.model.FieldState
import com.redmadrobot.app.ui.base.viewmodel.BaseViewModel
import com.redmadrobot.app.ui.base.viewmodel.Navigate
import com.redmadrobot.app.ui.base.viewmodel.NavigateUp
import com.redmadrobot.app.ui.base.viewmodel.SetSelectedDateEvent
import com.redmadrobot.app.ui.base.viewmodel.ShowDatePikerEvent
import com.redmadrobot.app.ui.base.viewmodel.ShowMessageEvent
import com.redmadrobot.app.utils.FieldValidator
import com.redmadrobot.app.utils.Validators
import com.redmadrobot.app.utils.converters.ErrorConverter
import com.redmadrobot.domain.model.UserCredentials
import com.redmadrobot.domain.model.UserProfile
import com.redmadrobot.domain.usecases.AuthUseCase
import com.redmadrobot.extensions.lifecycle.mapDistinct
import com.redmadrobot.extensions.lifecycle.provideDelegate
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@Suppress("TooManyFunctions")
class SignUpSecondViewModel @AssistedInject constructor(
    private val auth: AuthUseCase,
    private val validators: Validators,
    private val error: ErrorConverter,
    @Assisted(EMAIL) private val email: String,
    @Assisted(PASSWORD) private val password: String,
) : BaseViewModel() {

    private companion object {
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }

    private val singUpSecondViewState: MutableLiveData<SignUpSecondViewState> = MutableLiveData(createInitialState())
    val isLoading = singUpSecondViewState.mapDistinct { it.isLoading }
    val checkInButtonEnabled = singUpSecondViewState.mapDistinct {
        it.nickname.isValid && it.firstName.isValid && it.secondName.isValid && it.birthDate.isValid
    }
    private var state: SignUpSecondViewState by singUpSecondViewState

    init {
        with(validators) {
            nicknameValidator.collect(R.string.sign_up_second_nickname_error) { state.copy(nickname = it) }
            firstNameValidator.collect(R.string.sign_up_second_name_error) { state.copy(firstName = it) }
            secondNameValidator.collect(R.string.sign_up_second_name_error) { state.copy(secondName = it) }
            dateValidator.collect(R.string.sign_up_second_date_error) { state.copy(birthDate = it) }
        }
    }

    fun onCheckInClicked(
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
            val credentials = UserCredentials(email = email, password = password)
            val profile = UserProfile(
                id = "",
                firstName = state.firstName.value,
                lastName = state.secondName.value,
                nickname = state.nickname.value,
                birthDay = state.birthDate.value,
                avatarUrl = null
            )
            auth.checkIn(credentials = credentials, profile = profile)
                .onStart { state = state.copy(isLoading = true) }
                .onCompletion { state = state.copy(isLoading = false) }
                .onEach {
                    val action = SignUpSecondFragmentDirections.actionSignUpSecondFragmentToDoneFragment()
                    eventsQueue.offerEvent(Navigate(action))
                }
                .catch { throwable: Throwable ->
                    val message = error.message(throwable)
                    eventsQueue.offerEvent(ShowMessageEvent(textMessage = message))
                }
                .launchIn(viewModelScope)
        }
    }

    private inline fun FieldValidator.collect(
        @StringRes errorRes: Int,
        crossinline getState: (FieldState) -> SignUpSecondViewState,
    ) {
        observeFieldState()
            .onEach { fieldState ->
                state = getState(fieldState)
                if (!fieldState.isValid && fieldState.value.isNotEmpty()) {
                    eventsQueue.offerEvent(ShowMessageEvent(messageId = errorRes))
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

    private fun createInitialState(): SignUpSecondViewState {
        return SignUpSecondViewState(
            nickname = FieldState(value = "", isValid = false),
            firstName = FieldState(value = "", isValid = false),
            secondName = FieldState(value = "", isValid = false),
            birthDate = FieldState(value = "", isValid = false),
            isLoading = false,
            isBirthDayFieldEndIconClickable = true
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(EMAIL) email: String,
            @Assisted(PASSWORD) password: String,
        ): SignUpSecondViewModel
    }

    data class SignUpSecondViewState(
        val nickname: FieldState,
        val firstName: FieldState,
        val secondName: FieldState,
        val birthDate: FieldState,
        val isLoading: Boolean,
        val isBirthDayFieldEndIconClickable: Boolean,
    )
}
