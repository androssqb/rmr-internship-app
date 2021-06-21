package com.redmadrobot.app.di.modules

import com.redmadrobot.data.usecase.AuthUseCaseImpl
import com.redmadrobot.domain.usecases.AuthUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Singleton
    @Provides
    fun provideAuthUseCase(authUseCase: AuthUseCaseImpl): AuthUseCase = authUseCase
}
