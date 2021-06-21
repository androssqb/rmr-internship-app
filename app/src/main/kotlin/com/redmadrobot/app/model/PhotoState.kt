package com.redmadrobot.app.model

import android.net.Uri

data class PhotoState(
    val uri: Uri,
    val isPhotoCreated: Boolean,
)
