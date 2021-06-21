package com.redmadrobot.app.ui.post

import android.net.Uri
import com.redmadrobot.extensions.lifecycle.Event

data class PhotoCaptureEvent(val uri: Uri) : Event

data class PermissionsRequestEvent(val permissions: Array<String>) : Event

data class OnePermissionRequestEvent(val permission: String) : Event
