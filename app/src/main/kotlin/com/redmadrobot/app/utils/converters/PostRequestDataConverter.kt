package com.redmadrobot.app.utils.converters

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRequestDataConverter @Inject constructor(private val context: Context) {

    fun convertFileDataToMultipartBody(uri: Uri): MultipartBody.Part? {
        return if (uri == Uri.EMPTY) {
            null
        } else {
            val file = File(uri.path.orEmpty())
            context.applicationContext.contentResolver.openInputStream(uri).use { inputStream ->
                val byteArray = inputStream?.readBytes()
                val requestFile = byteArray?.toRequestBody("image/*".toMediaTypeOrNull())
                requestFile?.let { MultipartBody.Part.createFormData("image_file", file.name, requestFile) }
            }
        }
    }

    fun convertPostTextToRequestBody(text: String): RequestBody {
        return text.toRequestBody("text/plain".toMediaTypeOrNull())
    }
}
