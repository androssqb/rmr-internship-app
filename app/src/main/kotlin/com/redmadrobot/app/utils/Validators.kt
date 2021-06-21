package com.redmadrobot.app.utils

import com.redmadrobot.app.model.FieldState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import java.util.regex.Pattern
import javax.inject.Inject

class Validators @Inject constructor() {

    private companion object {
        private val EMAIL_PATTERN: Pattern = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
        )
        private val PASSWORD_PATTERN: Pattern =
            Pattern.compile("^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])[a-zA-Z0-9]{8,}$")
        private val NAME_PATTERN: Pattern = Pattern.compile("[a-zA-Z]{1,20}")
        private val NICKNAME_PATTERN: Pattern = Pattern.compile("[a-zA-Z0-9]{1,20}")
        private val DATE_PATTERN: Pattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{1,2}")
    }

    val emailValidator = FieldValidator(EMAIL_PATTERN)
    val passwordValidator = FieldValidator(PASSWORD_PATTERN)
    val firstNameValidator = FieldValidator(NAME_PATTERN)
    val secondNameValidator = FieldValidator(NAME_PATTERN)
    val nicknameValidator = FieldValidator(NICKNAME_PATTERN)
    val dateValidator = FieldValidator(DATE_PATTERN)
}

class FieldValidator(private val pattern: Pattern) {

    companion object {
        private const val DEBOUNCE_PERIOD = 1_500L
    }

    private val text = MutableStateFlow("")

    fun setText(text: String) {
        this.text.value = text
    }

    fun observeFieldState(): Flow<FieldState> {
        return text
            .debounce(DEBOUNCE_PERIOD)
            .map { value -> FieldState(value = value, isValid = validateField(text = value)) }
    }

    fun validateField(text: String): Boolean {
        return pattern.matcher(text).matches()
    }
}
