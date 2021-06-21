package com.redmadrobot.app.di.modules

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.location.Geocoder
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.redmadrobot.app.utils.providers.ApplicationResourceProvider
import com.redmadrobot.app.utils.providers.PermissionCheckImpl
import com.redmadrobot.app.utils.providers.PhotoFileGeneratorImpl
import com.redmadrobot.data.prefs.UserPreferences
import com.redmadrobot.domain.providers.PermissionCheck
import com.redmadrobot.domain.providers.PhotoFileGenerator
import com.redmadrobot.domain.providers.PreferencesProvider
import com.redmadrobot.domain.providers.ResourceProvider
import com.redmadrobot.mapmemory.MapMemory
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.datetime.Clock
import java.util.*
import javax.inject.Singleton

@Module
abstract class DataModule {

    companion object {
        private const val PREFS_NAME = "app_prefs"

        @Singleton
        @Provides
        fun provideSharedPreferences(context: Context): SharedPreferences {
            val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            return EncryptedSharedPreferences.create(
                PREFS_NAME,
                masterKey,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        @Singleton
        @Provides
        fun provideUserPreferences(prefs: SharedPreferences): PreferencesProvider = UserPreferences(prefs)

        @Singleton
        @Provides
        fun provideMapMemory(): MapMemory = MapMemory()

        @Singleton
        @Provides
        fun provideClock(): Clock = Clock.System

        @Singleton
        @Provides
        fun provideResources(context: Context): Resources = context.resources

        @Singleton
        @Provides
        fun provideGeocoder(context: Context): Geocoder = Geocoder(context, Locale.getDefault())
    }

    @Singleton
    @Binds
    abstract fun bindOriginResources(resourceProvider: ApplicationResourceProvider): ResourceProvider

    @Singleton
    @Binds
    abstract fun bindPhotoFileGenerator(generator: PhotoFileGeneratorImpl): PhotoFileGenerator

    @Singleton
    @Binds
    abstract fun bindPermissionsCheck(permissionsCheck: PermissionCheckImpl): PermissionCheck
}
