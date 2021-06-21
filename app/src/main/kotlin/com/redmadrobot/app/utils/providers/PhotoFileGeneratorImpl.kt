package com.redmadrobot.app.utils.providers

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.redmadrobot.domain.providers.PhotoFileGenerator
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayAt
import java.io.File
import javax.inject.Inject

class PhotoFileGeneratorImpl @Inject constructor(
    private val context: Context,
    private val clock: Clock,
) : PhotoFileGenerator {

    override fun createImageFile(): File? {
        val timeStamp: String = clock.todayAt(TimeZone.UTC).toString()
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("InternshipApp_$timeStamp", ".jpg", storageDir)
    }

    override fun getUriFromGeneratedFile(): Uri? {
        val photoFile = createImageFile()
        return photoFile?.let { FileProvider.getUriForFile(context, "com.workplaces.rossinevich", photoFile) }
    }
}
