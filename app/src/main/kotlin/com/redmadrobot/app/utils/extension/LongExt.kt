package com.redmadrobot.app.utils.extension

import java.text.SimpleDateFormat
import java.util.*

fun Long.toDate(): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(this)
}
