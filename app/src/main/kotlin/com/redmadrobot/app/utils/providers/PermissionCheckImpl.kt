package com.redmadrobot.app.utils.providers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.redmadrobot.domain.providers.PermissionCheck
import javax.inject.Inject

class PermissionCheckImpl @Inject constructor(private val context: Context) : PermissionCheck {

    companion object {
        val cameraAndStoragePermissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
        const val googleMapPermission = Manifest.permission.ACCESS_FINE_LOCATION
    }

    override fun cameraAndStoragePermissionsGranted(): Boolean {
        var allGranted = true
        cameraAndStoragePermissions.forEach { permission ->
            allGranted =
                ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
            if (!allGranted) return@forEach
        }
        return allGranted
    }

    override fun locationFinePermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(context, googleMapPermission) == PackageManager.PERMISSION_GRANTED
    }
}
