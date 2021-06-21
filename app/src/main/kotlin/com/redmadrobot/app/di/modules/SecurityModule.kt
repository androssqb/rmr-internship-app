package com.redmadrobot.app.di.modules

import android.content.Context
import com.redmadrobot.domain.constants.Constants.HOST_NAME
import com.redmadrobot.domain.constants.Constants.HOST_PIN
import com.scottyab.rootbeer.RootBeer
import dagger.Module
import dagger.Provides
import okhttp3.CertificatePinner
import javax.inject.Singleton

@Module
class SecurityModule {

    @Singleton
    @Provides
    fun provideCertificatePinner(): CertificatePinner {
        return CertificatePinner.Builder()
            .add(HOST_NAME, HOST_PIN)
            .build()
    }

    @Singleton
    @Provides
    fun provideRootBeer(context: Context): RootBeer = RootBeer(context)
}
