package com.redmadrobot.domain.providers

interface PermissionCheck {

    fun cameraAndStoragePermissionsGranted(): Boolean

    fun locationFinePermissionsGranted(): Boolean
}
