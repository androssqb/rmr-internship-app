package com.redmadrobot.app.di.modules

import com.redmadrobot.data.interceptors.AppAuthenticator
import com.redmadrobot.data.interceptors.AuthHeaderInterceptor
import com.redmadrobot.data.interceptors.ErrorInterceptor
import com.redmadrobot.domain.constants.Constants.BASE_URL
import com.redmadrobot.domain.constants.Constants.HEADER_AUTHORIZATION
import com.redmadrobot.domain.di.Logout
import com.redmadrobot.domain.di.Main
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
            redactHeader(HEADER_AUTHORIZATION)
        }
    }

    @Singleton
    @Provides
    @Logout
    fun provideLogoutOkHttpClient(
        authHeaderInterceptor: AuthHeaderInterceptor,
        errorInterceptor: ErrorInterceptor,
        logging: HttpLoggingInterceptor,
        certificatePinner: CertificatePinner
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authHeaderInterceptor)
            .addInterceptor(errorInterceptor)
            .certificatePinner(certificatePinner)
            .build()
    }

    @Singleton
    @Provides
    @Main
    fun provideMainOkHttpClient(
        authHeaderInterceptor: AuthHeaderInterceptor,
        errorInterceptor: ErrorInterceptor,
        logging: HttpLoggingInterceptor,
        authenticator: AppAuthenticator,
        certificatePinner: CertificatePinner
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authHeaderInterceptor)
            .addInterceptor(errorInterceptor)
            .authenticator(authenticator)
            .certificatePinner(certificatePinner)
            .build()
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Singleton
    @Provides
    @Main
    fun providesMainRetrofit(moshi: Moshi, @Main client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    @Logout
    fun providesLogoutRetrofit(moshi: Moshi, @Logout client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }
}
