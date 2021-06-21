package com.redmadrobot.data.security

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.redmadrobot.domain.model.UserProfile
import com.squareup.moshi.Moshi
import java.io.File
import java.net.URLEncoder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Encrypter @Inject constructor(
    private val context: Context,
    moshi: Moshi,
) {

    private companion object {
        const val FILE_NAME = "profile_data"
    }

    val fileName = FILE_NAME
    private val profileAdapter = moshi.adapter(UserProfile::class.java)

    private fun getEncryptedFile(name: String): EncryptedFile {
        val file = File(context.filesDir, URLEncoder.encode(name, "UTF-8"))
        return EncryptedFile.Builder(
            file,
            context,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }

    fun encryptProfile(profile: UserProfile) {
        deleteFile()
        val string = profileAdapter.toJson(profile)
        val encryptedFile = getEncryptedFile(fileName)
        encryptedFile.openFileOutput().use { stream ->
            stream.write(string.toByteArray())
            stream.flush()
            stream.close()
        }
    }

    fun decryptProfile(fileName: String): UserProfile? {
        val encryptedFile = getEncryptedFile(fileName)
        encryptedFile.openFileInput().use { stream ->
            val string = String(stream.readBytes(), Charsets.UTF_8)
            stream.close()
            return profileAdapter.fromJson(string)
        }
    }

    fun deleteFile() {
        val file = File(context.filesDir, URLEncoder.encode(fileName, "UTF-8"))
        if (file.exists()) file.delete()
    }
}
