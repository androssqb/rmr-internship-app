package com.redmadrobot.app.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

class Searcher @Inject constructor() {

    private companion object {
        const val DEBOUNCE_PERIOD = 300L
        const val MIN_TEXT_LENGTH = 2
    }

    private val text = MutableStateFlow("")

    fun setText(text: String) {
        this.text.value = text
    }

    fun observeField(): Flow<String> {
        return text
            .debounce(DEBOUNCE_PERIOD)
            .filter { it.length > MIN_TEXT_LENGTH }
    }

    fun clearText() {
        text.value = ""
    }
}
