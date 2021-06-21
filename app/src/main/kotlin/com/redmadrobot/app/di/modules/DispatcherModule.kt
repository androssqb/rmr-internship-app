package com.redmadrobot.app.di.modules

import com.redmadrobot.domain.providers.DispatcherProvider
import com.redmadrobot.domain.providers.impl.DispatcherProviderImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DispatcherModule {

    @Singleton
    @Binds
    fun provideCoroutineDispatcherProvider(dispatcherProvider: DispatcherProviderImpl): DispatcherProvider
}
