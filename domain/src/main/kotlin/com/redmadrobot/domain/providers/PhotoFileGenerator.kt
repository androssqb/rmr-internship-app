package com.redmadrobot.domain.providers

import android.net.Uri
import java.io.File

interface PhotoFileGenerator {

    fun createImageFile(): File?

    fun getUriFromGeneratedFile(): Uri?
}
